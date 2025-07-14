package src;

import java.net.ServerSocket;
import java.net.Socket;

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
                    PrithviDB — Lightweight Key-Value Store (Alpha)
                    Author      : Philkhana Sidharth
                    Language    : Java (no frameworks)
                    Listening   : Port 1902
                    Launched    : %s
                    Persistence : Not yet implemented
                    Security    : No auth (dev only)

                    ⚠️  Warning: This is an experimental build.
                    ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                    """.formatted(java.time.LocalDateTime.now()));

            System.out.println("🚀 PrithviServer listening on port " + PORT);

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