package src.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.Command;
import src.CommandExecutor;
import src.db.ValueWithExpiry;

public class LoadCommand implements CommandExecutor {
    private static final String FILE_PATH = "data/store.json";

    private static final Pattern ENTRY_PATTERN = Pattern.compile(
            "\"(.*?)\"\\s*:\\s*\\{\\s*\"value\"\\s*:\\s*\"(.*?)\",\\s*\"expiry\"\\s*:\\s*(\\d+)\\s*\\}");

    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, ValueWithExpiry> store) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            writer.println("❌ Failed to load: File not found");
            return;
        }

        StringBuilder rawJson = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                rawJson.append(line);
            }
        }

        String json = rawJson.toString().trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            writer.println("❌ Invalid JSON structure");
            return;
        }

        int loadedCount = 0;
        Matcher matcher = ENTRY_PATTERN.matcher(json);
        while (matcher.find()) {
            String key = unescape(matcher.group(1));
            String value = unescape(matcher.group(2));
            long expiry = Long.parseLong(matcher.group(3));

            ValueWithExpiry data = new ValueWithExpiry(value, expiry);
            if (System.currentTimeMillis() > data.expiryTimestamp)
                continue;
            if (!data.isExpired()) {
                store.put(key, data);
                loadedCount++;
            }
        }
        if (loadedCount == 0) {
            writer.println("There are no keys in the store");
            return;
        }
        writer.println("✅ Loaded " + loadedCount + " key(s) into memory.");
    }

    private String unescape(String str) {
        return str.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
