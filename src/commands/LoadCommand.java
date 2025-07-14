package src.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;

public class LoadCommand implements CommandExecutor {
    private static final String FILE_PATH = "data/store.json";
    private static final String DIRECTORY = "data";

    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, String> store) throws IOException {

        try {
            File file = new File(DIRECTORY);

            if (!file.exists()) {
                writer.println("Failed to load. No path");
                return;
            }

            StringBuilder loadedString = new StringBuilder();

            try (BufferedReader fileReader = new BufferedReader(new FileReader(FILE_PATH))) {
                String openingBracket = fileReader.readLine();
                if (!"{".equals(openingBracket)) {
                    writer.println("Error parsing json.");
                    return;
                }

                loadedString.append(openingBracket);
                String line;
                while ((line = fileReader.readLine()) != null) {
                    loadedString.append(line);
                }
            }
            if (loadedString.length() <= 2) {
                writer.println("Empty JSON");
                return;
            }
            loadedString.deleteCharAt(0);
            loadedString.deleteCharAt(loadedString.length() - 1);

            String[] pairs = loadedString.toString().trim().split("\\,+");
            for (String pair : pairs) {
                String[] onePair = pair.trim().split(":");
                String key = clean(onePair[0]);
                String value = clean(onePair[1]);
                store.put(key, value);
                System.out.println(key);
                System.out.println(value);
            }

            for (Map.Entry<String, String> entry : store.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }

            writer.println("✅ Store loaded from disk.");

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private String clean(String raw) {
        return raw.trim()
                .replaceAll("^\"|\"$", "") // remove surrounding quotes
                .replace("\\\"", "\"") // unescape quotes
                .replace("\\\\", "\\"); // unescape backslashes
    }

}
