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

public class GetCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, ValueWithExpiry> store) {

        ValueWithExpiry v = store.get(cmd.key); // ✅ Get value from the store

        if (v == null || v.isExpired()) {
            store.remove(cmd.key); // Clean up if expired
            writer.println("nil");
        } else {
            writer.println("Value: " + v.value);
            if (v.expiryTimestamp != Long.MAX_VALUE) {
                String formattedExpiry = Instant.ofEpochMilli(v.expiryTimestamp)
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                writer.println("Expiry: " + formattedExpiry);
            } else {
                writer.println("Expiry: Never");
            }
        }
    }
}
