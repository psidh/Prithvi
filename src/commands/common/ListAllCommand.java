package src.commands.common;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import src.Command;
import src.CommandExecutor;
import src.db.ValueWithExpiry;

public class ListAllCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) {
        if (store.isEmpty()) {
            writer.println("No Keys in database");
            return;
        }
        writer.println("Keys in the database currently.");
        int index = 1;

        for (Map.Entry<String, ValueWithExpiry> entry : store.entrySet()) {
            ValueWithExpiry valueWithExpiry = entry.getValue();
            String expiryOutput;

            if (valueWithExpiry.expiryTimestamp == Long.MAX_VALUE ||
                    Instant.ofEpochMilli(valueWithExpiry.expiryTimestamp).atZone(ZoneId.systemDefault())
                            .getYear() > 9999) {
                expiryOutput = "Never";
            } else {
                expiryOutput = Instant.ofEpochMilli(valueWithExpiry.expiryTimestamp)
                        .atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }

            writer.println(index + ". " + entry.getKey() + " : " + expiryOutput);
            index++;
        }
    }
}