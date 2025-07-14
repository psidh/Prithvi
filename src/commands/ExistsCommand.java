
package src.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;

public class ExistsCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, String> store) throws IOException {

        if (store.isEmpty()) {
            writer.println("No keys found in the database.");
            return;
        }
        if (!store.containsKey(cmd.key)) {
            writer.println("Key does not exist");
            return;
        }
        String value = store.get(cmd.key);
        if (value != null) {
            writer.println("Key exists");
            writer.println("KEY: " + cmd.key);
            writer.println("VALUE: " + value);
        } else {
            writer.println("Key does not exist");
        }
    }
}
