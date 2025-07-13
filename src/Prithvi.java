package src;
import java.net.ServerSocket;
import java.net.Socket;

public class Prithvi {
    private static final int PORT = 1902;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

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