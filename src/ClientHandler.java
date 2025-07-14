package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import src.commands.*;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private static final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

    private static final Map<Command.Type, CommandExecutor> commandMap = Map.of(
            Command.Type.SET, new SetCommand(),
            Command.Type.GET, new GetCommand(),
            Command.Type.DEL, new DelCommand(),
            Command.Type.FLUSH, new FlushCommand(),
            Command.Type.EXISTS, new ExistsCommand(),
            Command.Type.LISTALL, new ListAllCommand(),
            Command.Type.SAVE, new SaveCommand(),
            Command.Type.LOAD, new LoadCommand());

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            writer.println("Namaste");
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
