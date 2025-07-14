package src.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;

public class SaveCommand implements CommandExecutor {
    private static final String FILE_PATH = "data/store.json";
    private static final String DIRECTORY = "data";

    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, String> store) throws IOException {
        try {
            File dir = new File(DIRECTORY);

            if (!dir.exists())
                dir.mkdir();

            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(FILE_PATH))) {
                fileWriter.write("{\n");
                int index = 0;
                int size = store.size();
                for (Map.Entry<String, String> entry : store.entrySet()) {
                    String key = escapeJson(entry.getKey());
                    String value = escapeJson(entry.getValue());
                    fileWriter.write("  \"" + key + "\": \"" + value + "\"");
                    if (++index < size) {
                        fileWriter.write(",");
                    }
                    fileWriter.write("\n");
                }

                fileWriter.write("}");
                writer.println("✅ Store saved to disk.");
            }

        } catch (IOException e) {
            writer.println("❌ Failed to save: " + e.getMessage());
        }
    }

    private String escapeJson(String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
