package src.commands.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import src.commands.Command;
import src.commands.CommandExecutor;
import src.db.ValueWithExpiry;

public class FlushCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) throws IOException {

        synchronized (store) {
            if (store.isEmpty()) {
                writer.println("Database is empty. Set a value first");
                return;
            }
        }

        writer.println("WARNING: This will delete all keys. Type YES to confirm.");
        String confirmation = reader.readLine();

        if ("YES".equalsIgnoreCase(confirmation)) {
            store.clear();
            writer.println("Store flushed.");
        } else {
            writer.println("FLUSH aborted.");
        }
    }
}
