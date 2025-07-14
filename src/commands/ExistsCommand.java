package src.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import src.Command;
import src.CommandExecutor;
import src.db.ValueWithExpiry;

public class ExistsCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd,
            PrintWriter writer,
            BufferedReader reader,
            ConcurrentHashMap<String, ValueWithExpiry> store) throws IOException {

        if (store.isEmpty()) {
            writer.println("No keys found in the database.");
            return;
        }

        ValueWithExpiry v = store.get(cmd.key);
        if (v == null || v.isExpired()) {
            writer.println("Key does not exist");
        } else {
            writer.println("Key exists");
            writer.println("KEY: " + cmd.key);
            writer.println("VALUE: " + v.value);
        }
    }
}
