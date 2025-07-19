package src.commands.set;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;
import src.db.ValueType;
import src.db.ValueWithExpiry;

public class SAddCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, ValueWithExpiry> store) {

        ValueWithExpiry existing = store.get(cmd.key);

        if (existing != null && existing.type != ValueType.SET) {
            writer.println("Type conflict: key '" + cmd.key + "' holds a " + existing.type + " value.");
            return;
        }

        Set<Object> set;

        if (existing == null || existing.isExpired()) {
            set = new HashSet<>();
        } else {
            set = existing.getSet();
        }

        boolean added = set.add(cmd.value);

        ValueWithExpiry updatedValue = (cmd.ttlSeconds != null && cmd.ttlSeconds != Long.MAX_VALUE)
                ? new ValueWithExpiry(set, ValueType.SET, cmd.ttlSeconds)
                : new ValueWithExpiry(set, ValueType.SET);

        store.put(cmd.key, updatedValue);

        writer.println(added ? "(integer) 1" : "(integer) 0");
    }
}
