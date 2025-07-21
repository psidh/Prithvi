package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.AbstractMap;
import java.util.Map;

import src.auth.JWTAuth;
import src.commands.common.*;
import src.commands.map.*;
import src.commands.queue.*;
import src.commands.set.*;
import src.commands.utils.*;
import src.db.Store;
import java.util.Map.Entry;
import src.db.ValueWithExpiry;
import src.metrics.MetricsCollector;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private static final Map<String, ValueWithExpiry> store = Store.get();
    String currentToken = null;
    boolean isAuthenticated = false;

    private static final Map<Command.Type, CommandExecutor> commandMap = Map.ofEntries(
            entry(Command.Type.SET, new SetCommand()),
            entry(Command.Type.GET, new GetCommand()),
            entry(Command.Type.DEL, new DelCommand()),
            entry(Command.Type.FLUSH, new FlushCommand()),
            entry(Command.Type.EXISTS, new ExistsCommand()),
            entry(Command.Type.LISTALL, new ListAllCommand()),
            entry(Command.Type.SAVE, new SaveCommand()),
            entry(Command.Type.LOAD, new LoadCommand()),
            entry(Command.Type.QUIT, new QuitCommand()),
            entry(Command.Type.FLUSHALL, new FlushAllCommand()),
            entry(Command.Type.HELP, new HelpCommand()),
            entry(Command.Type.INFO, new InfoCommand()),
            entry(Command.Type.LPUSH, new LPushCommand()),
            entry(Command.Type.RPUSH, new RPushCommand()),
            entry(Command.Type.RPOP, new RPopCommand()),
            entry(Command.Type.LPOP, new LPopCommand()),
            entry(Command.Type.GETLIST, new GETLISTCommand()),
            entry(Command.Type.SADD, new SAddCommand()),
            entry(Command.Type.SMEMBERS, new SMembersCommand()),
            entry(Command.Type.SREM, new SRemCommand()));

    private static <K, V> Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

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
