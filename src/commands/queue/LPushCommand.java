package src.commands.queue;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import src.commands.Command;
import src.commands.CommandExecutor;
import src.db.ValueType;
import src.db.ValueWithExpiry;

public class LPushCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) {
        ValueWithExpiry existing;
        Deque<String> list;
        synchronized (store) {
            existing = store.get(cmd.key);

            if (existing == null) {
                list = new ArrayDeque<>();
                ValueWithExpiry newVal = new ValueWithExpiry(list, ValueType.LIST);
                store.put(cmd.key, newVal);
            } else if (existing.type != ValueType.LIST) {
                writer.println(" Type mismatch: Expected LIST but found " + existing.type);
                return;
            } else {
                list = (Deque<String>) existing.value;
            }

        }
        synchronized (store) {
            list.addFirst(cmd.value);
        }
        writer.println(" LPUSH: " + cmd.key + " <-> " + cmd.value);
    }
}
