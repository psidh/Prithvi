package src.commands.map;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Map;

import src.commands.Command;
import src.commands.CommandExecutor;
import src.db.ValueWithExpiry;

public class DelCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) {

        String message;
        synchronized (store) {
            ValueWithExpiry entry = store.get(cmd.key);

            if (entry == null) {
                message = "Key-Value doesn't exist.";
            } else if (entry.isExpired()) {
                store.remove(cmd.key);
                message = "Key-Value was expired and removed.";
            } else {
                store.remove(cmd.key);
                message = "Removed Key : " + cmd.key;
            }
        }
        writer.println(message);
    }
}
