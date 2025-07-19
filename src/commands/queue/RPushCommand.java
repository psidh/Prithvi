package src.commands.queue;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;
import src.db.ValueType;
import src.db.ValueWithExpiry;

public class RPushCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, ValueWithExpiry> store) {

        ValueWithExpiry existing = store.get(cmd.key);
        Deque<String> list;

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

        list.addLast(cmd.value);
        writer.println(" LPUSH: " + cmd.key + " <-> " + cmd.value);
    }
}
