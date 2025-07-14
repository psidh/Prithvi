package src.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;

public class FlushCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, String> store) throws IOException {
        if (store.isEmpty()) {
            writer.println("⚠️ Database is empty. Set a value first");
            return;
        }

        writer.println("⚠️ WARNING: This will delete all keys. Type YES to confirm.");

        String confirmation = reader.readLine();
        if (confirmation.equals("YES")) {
            store.clear();
            writer.println("✅ Store flushed.");
        } else {
            writer.println("❌ FLUSH aborted.");
        }
    }
}