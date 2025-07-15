package src.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;
import src.db.ValueWithExpiry;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SetCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, ValueWithExpiry> store) {

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
