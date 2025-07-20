package src.commands.map;

import java.io.BufferedReader;
import java.io.PrintWriter;

import src.Command;
import src.CommandExecutor;
import java.util.Map;
import src.db.ValueWithExpiry;

public class DelCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) {

        ValueWithExpiry entry = store.get(cmd.key);

        if (entry == null) {
            writer.println("Key-Value doesn't exist.");
            return;
        }

        if (entry.isExpired()) {
            store.remove(cmd.key);
            writer.println("Key-Value was expired and removed.");
            return;
        }

        store.remove(cmd.key);
        writer.println("Removed Key : " + cmd.key);
    }
}
