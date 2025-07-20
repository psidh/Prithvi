package src.commands.set;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Set;

import src.Command;
import src.CommandExecutor;
import src.db.ValueType;
import java.util.Map;
import src.db.ValueWithExpiry;

public class SRemCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) {

        ValueWithExpiry existing = store.get(cmd.key);

        if (existing != null && existing.type != ValueType.SET) {
            writer.println("Type conflict: key '" + cmd.key + "' holds a " + existing.type + " value.");
            return;
        }

        Set<Object> set;

        if (existing == null) {
            writer.println("Key does not exist");
            return;
        } else if (existing.isExpired()) {
            writer.println("Key is expired.");
            return;
        } else {
            set = existing.getSet();
        }

        boolean removed = set.remove(cmd.value);

        ValueWithExpiry updatedValue = (cmd.ttlSeconds != null && cmd.ttlSeconds != Long.MAX_VALUE)
                ? new ValueWithExpiry(set, ValueType.SET, cmd.ttlSeconds)
                : new ValueWithExpiry(set, ValueType.SET);

        store.put(cmd.key, updatedValue);

        writer.println(removed ? "(integer) 1" : "(integer) 0");
    }
}
