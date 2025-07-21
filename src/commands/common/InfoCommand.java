package src.commands.common;

import java.io.BufferedReader;
import java.io.PrintWriter;

import src.Prithvi;
import src.commands.Command;
import src.commands.CommandExecutor;
import src.db.ValueWithExpiry;
import java.util.Map;

public class InfoCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) {

        long currentTime = System.currentTimeMillis();
        long uptimeMillis = currentTime - Prithvi.START_TIME;
        long uptimeSeconds = uptimeMillis / 1000;
        long uptimeMinutes = uptimeSeconds / 60;
        long uptimeHours = uptimeMinutes / 60;

        int keyCount = (int) store.entrySet().stream()
                .filter(e -> !e.getValue().isExpired())
                .count();

        long memoryUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        writer.println("Server Info:");
        writer.println("---------------------------");
        writer.println("Total Keys      : " + keyCount);
        writer.println("Memory Used     : " + formatMemory(memoryUsed));
        writer.println("Uptime          : " + uptimeHours + "h " + (uptimeMinutes % 60) + "m " + (uptimeSeconds % 60)
                + "s");
        writer.println("Persistence     : Enabled (JSON file)");
        writer.println("Expiry Thread   : Running every 5 sec");
        writer.println("---------------------------");
    }

    private String formatMemory(long bytes) {
        return String.format("%.2f MB", bytes / (1024.0 * 1024));
    }
}
