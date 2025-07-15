package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import src.commands.*;
import src.db.Store;
import src.db.ValueWithExpiry;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private static final ConcurrentHashMap<String, ValueWithExpiry> store = Store.get();

    private static final Map<Command.Type, CommandExecutor> commandMap = Map.of(
            Command.Type.SET, new SetCommand(),
            Command.Type.GET, new GetCommand(),
            Command.Type.DEL, new DelCommand(),
            Command.Type.FLUSH, new FlushCommand(),
            Command.Type.EXISTS, new ExistsCommand(),
            Command.Type.LISTALL, new ListAllCommand(),
            Command.Type.SAVE, new SaveCommand(),
            Command.Type.LOAD, new LoadCommand(),
            Command.Type.QUIT, new QuitCommand());

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            writer.println("""


                     ██████╗ ██████╗ ██╗████████╗██╗  ██╗██╗   ██╗██╗
                     ██╔══██╗██╔══██╗██║╚══██╔══╝██║  ██║██║   ██║██║
                     ██████╔╝██████╔╝██║   ██║   ███████║██║   ██║██║
                     ██╔═══╝ ██╔══██╗██║   ██║   ██╔══██║╚██╗ ██╔╝██║
                     ██║     ██║  ██║██║   ██║   ██║  ██║ ╚████╔╝ ██║
                     ╚═╝     ╚═╝  ╚═╝╚═╝   ╚═╝   ╚═╝  ╚═╝  ╚═══╝  ╚═╝

                    ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                    Prithvi     : Lightweight Key-Value Store (Alpha)
                    Author      : Philkhana Sidharth
                    Language    : Java (no frameworks)
                    Port        :  1902
                    Launched    : 14 Jul 2025
                    Persistence : True
                    Security    : No auth

                    ⚠️  Warning: This is an experimental build.
                    ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                    """);

            writer.println("🚀 PrithviServer listening on port 1902");
            Parser parser = new Parser(reader);

            Command cmd;

            while (true) {
                cmd = parser.parseNextCommand();

                if (cmd.type == Command.Type.UNKNOWN) {
                    writer.println("ERR unknown or malformed command");
                    continue;
                }

                CommandExecutor executor = commandMap.get(cmd.type);

                if (executor != null) {
                    executor.execute(cmd, writer, reader, store);
                } else {
                    writer.println("Enter valid Prithvi command. See docs");
                }
            }
        } catch (QuitException qe) {
            // NO op handled here
        }

        catch (Exception e) {
            System.err.println("⚠️ Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
