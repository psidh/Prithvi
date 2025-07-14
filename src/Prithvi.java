package src;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import src.commands.SaveCommand;
import src.db.Store;

public class Prithvi {
    private static final int PORT = 1902;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("""


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

            System.out.println("🚀 PrithviServer listening on port " + PORT);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n--Caught SIGINT (Ctrl+C). Saving store to disk...");
                try {
                    new SaveCommand().execute(
                            new Command(Command.Type.SAVE, null, null),
                            new PrintWriter(System.out, true),
                            null,
                            Store.get());

                    System.out.println("✅ Store saved successfully.");

                } catch (Exception e) {
                    System.err.println("Failed to save on shutdown: " + e.getMessage());
                }
                System.out.println("👋 Shutdown complete. Exiting...");
            }));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("⚡ New client connected: " + clientSocket.getInetAddress());

                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}