package src.commands.queue;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Deque;
import java.util.Map;

import src.commands.Command;
import src.commands.CommandExecutor;
import src.db.ValueType;
import src.db.ValueWithExpiry;

public class RPopCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) {

        ValueWithExpiry existing;

        synchronized (store) {
            existing = store.get(cmd.key);
        }

        if (existing == null) {
            writer.println(" Key not found.");
            return;
        }

        if (existing.type != ValueType.LIST) {
            writer.println(" Type mismatch: Expected LIST but found " + existing.type);
            return;
        }

        Deque<String> list = (Deque<String>) existing.value;
        String popped;
        synchronized (list) {
            popped = list.pollLast(); // Correct usage
        }

        if (popped == null) {
            writer.println(" List is empty.");
        } else {
            writer.println(" RPOP: " + popped);
        }
    }
}
