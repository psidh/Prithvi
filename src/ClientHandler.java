package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private static final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

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

                switch (cmd.type) {
                    case SET -> {
                        store.put(cmd.key, cmd.value);
                        writer.println("OK");
                    }
                    case GET -> {
                        String val = store.get(cmd.key);
                        writer.println(val != null ? val : "nil");
                    }
                    case DEL -> {
                        if (store.isEmpty()) {
                            writer.println("Database is empty. Set a value first");
                            continue;
                        }
                        if (!store.containsKey(cmd.key)) {
                            writer.println("Key-Value doesn't exist.");
                            continue;
                        }

                        store.remove(cmd.key);
                        writer.println("Removed Key : " + cmd.key);
                    }
                    case FLUSH -> {
                        if (store.isEmpty()) {
                            writer.println("Database is empty. Set a value first");
                            continue;
                        }

                        writer.println("⚠️ WARNING: This will delete all keys. Type YES to confirm.");

                        String confirmation = reader.readLine();
                        if (confirmation.equals("YES")) {
                            store.clear();
                            writer.println("✅ Store flushed.");
                        } else {
                            writer.println("❎ FLUSH aborted.");
                        }
                    }

                    case UNKNOWN -> {
                        writer.println("Enter valid Prithvi command. See docs");
                    }
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
