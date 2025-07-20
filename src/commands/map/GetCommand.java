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

public class GetCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) {

        ValueWithExpiry v;

        synchronized (store) {
            v = store.get(cmd.key);

            if (v == null || v.isExpired()) {
                store.remove(cmd.key);
                writer.println("nil");
                return;
            }
        }

        if (v.type != ValueType.STRING) {
            writer.println(" Type mismatch: Expected STRING, found " + v.type);
            return;
        }

        writer.println("Value: " + v.value);

        if (v.expiryTimestamp == Long.MAX_VALUE
                || Instant.ofEpochMilli(v.expiryTimestamp).atZone(ZoneId.systemDefault()).getYear() > 9999) {
            writer.println("Expiry: Never");
        } else {
            String formattedExpiry = Instant.ofEpochMilli(v.expiryTimestamp)
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.println("Expiry: " + formattedExpiry);
        }
    }
}
