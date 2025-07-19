package src.commands.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;
import src.db.ValueWithExpiry;

public class FlushAllCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, ValueWithExpiry> store) throws IOException {
        if (store.isEmpty()) {
            writer.println(" Database is empty. Set a value first");
            return;
        }
        store.clear();
    }
}
