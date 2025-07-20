package src.commands.map;

import java.io.BufferedReader;
import java.io.PrintWriter;

import src.Command;
import src.CommandExecutor;
import src.db.ValueType;
import java.util.Map;
import src.db.ValueWithExpiry;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SetCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) {

        ValueWithExpiry existing = store.get(cmd.key);

        if (existing != null && existing.type != ValueType.STRING) {
            writer.println("Type conflict: key '" + cmd.key + "' holds a " + existing.type + " value.");
            return;
        }

        ValueWithExpiry value = (cmd.ttlSeconds != null && cmd.ttlSeconds != Long.MAX_VALUE)
                ? new ValueWithExpiry(cmd.value, cmd.ttlSeconds)
                : new ValueWithExpiry(cmd.value);

        store.put(cmd.key, value);
        writer.print("Key : " + cmd.key + " : " + value.value);
        writer.println();

        if (value.expiryTimestamp != Long.MAX_VALUE) {
            String formattedExpiry = Instant.ofEpochMilli(value.expiryTimestamp)
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.println("Expiry : " + formattedExpiry);
        } else {
            writer.println("Expiry : Never");
        }
        writer.flush();
    }
}
