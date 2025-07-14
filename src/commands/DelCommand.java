package src.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;

public class DelCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, String> store) {
        if (store.isEmpty()) {
            writer.println("⚠️ Database is empty. Set a value first");
            return;
        }
        if (!store.containsKey(cmd.key)) {
            writer.println("Key-Value doesn't exist.");
            return;
        }

        store.remove(cmd.key);
        writer.println("Removed Key : " + cmd.key);
    }
}