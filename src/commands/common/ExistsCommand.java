package src.commands.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import src.Command;
import src.CommandExecutor;
import src.db.ValueWithExpiry;
import java.util.Map;

public class ExistsCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd,
            PrintWriter writer,
            BufferedReader reader,
            Map<String, ValueWithExpiry> store) throws IOException {

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
