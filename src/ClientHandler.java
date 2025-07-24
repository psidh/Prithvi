/**
 * Prithvi – An in-memory key-value database
 * with TTL, LRU eviction, disk persistence, lists, sets,
 * and multithreaded client support.
 *
 * © 2025 PHILKHANA SIDHARTH
 * Licensed under the Apache License, Version 2.0.
 * You may obtain a copy at:
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * You must retain this header in any redistribution or modification.
 */
package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import src.auth.JWTAuth;
import src.commands.*;
import src.commands.utils.*;
import src.db.Store;
import src.db.ValueWithExpiry;
import src.db.persistence.WALManager;
import src.metrics.MetricsCollector;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private static final Map<String, ValueWithExpiry> store = Store.get();
    String currentToken = null;
    boolean isAuthenticated = false;

    private static final Map<Command.Type, CommandExecutor> commandMap = CommandMap.getCommandMap();

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
            writer.println("🚀 PrithviServer listening on port 1902");
            Parser parser = new Parser(reader);
            WALManager walManager = WALManager.getInstance();

            Command cmd;

            while (true) {

                cmd = parser.parseNextCommand();

                if (cmd.type == Command.Type.UNKNOWN) {
                    writer.println("ERR unknown or malformed command");
                    continue;
                }

                if (cmd.type == Command.Type.AUTH) {
                    if (cmd.key == null || cmd.key.isBlank()) {
                        writer.println("ERR Provide username: AUTH <username>");
                    } else {
                        long ttlMillis = 3600_000;
                        currentToken = JWTAuth.createToken(cmd.key, ttlMillis);
                        isAuthenticated = true;
                        writer.println("TOKEN " + currentToken);
                    }
                    continue;
                }

                if (cmd.type == Command.Type.TOKEN) {
                    if (cmd.key == null || !JWTAuth.verifyToken(cmd.key)) {
                        writer.println("ERR Invalid Token");
                        isAuthenticated = false;
                    } else {
                        isAuthenticated = true;
                        currentToken = cmd.key;
                        writer.println("OK Authenticated");
                    }
                    continue;
                }

                if (cmd.type == Command.Type.QUIT)
                    throw new QuitException();

                if (!isAuthenticated) {
                    writer.println("ERR You must authenticate first using AUTH <username> or TOKEN <jwt>");
                    continue;
                }

                switch (cmd.type) {
                    case GET:
                    case GETLIST:
                    case SMEMBERS:
                    case EXISTS:
                        MetricsCollector.recordRead();
                        break;
                    case SET:
                    case LPUSH:
                    case RPUSH:
                    case SADD:
                        MetricsCollector.recordWrite();
                        break;
                    default:
                        break;
                }

                CommandExecutor executor = commandMap.get(cmd.type);

                if (executor != null) {

                    boolean isWriteCommand = switch (cmd.type) {
                        case SET, DEL, LPUSH, RPUSH, SADD, SREM, FLUSH, FLUSHALL -> true;
                        default -> false;
                    };

                    if (isWriteCommand) {
                        String rawCommand = cmd.type.toString();
                        if (cmd.key != null)
                            rawCommand += " " + cmd.key;
                        if (cmd.value != null)
                            rawCommand += " " + cmd.value;
                        if (cmd.ttlSeconds > 0)
                            rawCommand += " EX " + cmd.ttlSeconds;
                        walManager.log(rawCommand);
                        System.out.println("[WAL] Logged: " + rawCommand);
                    }

                    long start = System.nanoTime();
                    executor.execute(cmd, writer, reader, store);
                    long end = System.nanoTime();
                    long micros = (end - start) / 1_000;
                    MetricsCollector.recordLatency(micros);
                    MetricsCollector.recordRequest();

                    if (cmd.type == Command.Type.GET || cmd.type == Command.Type.EXISTS)
                        MetricsCollector.recordRead();
                    else
                        MetricsCollector.recordWrite();
                } else {
                    MetricsCollector.recordError();
                    writer.println("Enter valid Prithvi command. See docs");
                }
            }

        } catch (QuitException qe) {
            // NO op handled here
        }

        catch (Exception e) {
            System.err.println(" Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
