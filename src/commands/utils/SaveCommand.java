package src.commands.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import src.commands.Command;
import src.commands.CommandExecutor;
import src.db.ValueWithExpiry;

public class SaveCommand implements CommandExecutor {
    private static final String FILE_PATH = "data/store.json";
    private static final String DIRECTORY = "data";

    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) throws IOException {
        try {
            File dir = new File(DIRECTORY);
            if (!dir.exists())
                dir.mkdir();

            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(FILE_PATH))) {
                fileWriter.write("{\n");
                int index = 0;
                int size = store.size();

                for (Map.Entry<String, ValueWithExpiry> entry : store.entrySet()) {
                    ValueWithExpiry data = entry.getValue();

                    if (data.isExpired()) {
                        continue;
                    }
                    String key = escapeJson(entry.getKey());
                    if (!(data.value instanceof String)) {
                        continue;
                    }
                    String value = escapeJson((String) data.value);

                    long expiry = entry.getValue().expiryTimestamp;

                    fileWriter.write("  \"" + key + "\": {\n");
                    fileWriter.write("    \"value\": \"" + value + "\",\n");
                    fileWriter.write("    \"expiry\": " + expiry + "\n");
                    fileWriter.write("  }");

                    if (++index < size) {
                        fileWriter.write(",");
                    }
                    fileWriter.write("\n");
                }

                fileWriter.write("}");
                writer.println("Store saved to disk.");
            }

        } catch (IOException e) {
            writer.println("Failed to save: " + e.getMessage());
        }
    }

    private String escapeJson(String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
