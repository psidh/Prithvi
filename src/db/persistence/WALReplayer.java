package src.db.persistence;

import src.commands.Command;
import src.commands.CommandExecutor;
import src.db.Store;

import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class WALReplayer {
    private static final String WAL_FILE = "wal.log";
    private final Map<Command.Type, CommandExecutor> commandMap;

    public WALReplayer(Map<Command.Type, CommandExecutor> commandMap) {
        this.commandMap = commandMap;
    }

    public void replay() {
        File file = new File(WAL_FILE);
        if (!file.exists()) {
            System.out.println("[WAL] No WAL file found. Skipping replay.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(WAL_FILE))) {
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                try {
                    Command cmd = parseLineToCommand(line);
                    CommandExecutor executor = commandMap.get(cmd.type);
                    if (executor != null) {
                        executor.execute(cmd, new PrintWriter(OutputStream.nullOutputStream(), true), null,
                                Store.get());
                        count++;
                    }
                } catch (Exception e) {
                    System.err.println("[WAL] Skipping malformed line: " + line);
                }
            }

            System.out.println("WAL replay completed. Commands replayed: " + count);
        } catch (IOException e) {
            System.err.println("[WAL] Error during replay: " + e.getMessage());
        }
    }

    private Command parseLineToCommand(String line) {
        String[] parts = line.trim().split(" ", 2);
        if (parts.length < 2)
            throw new IllegalArgumentException("No command found");

        String[] tokens = parts[1].split(" ");
        Command.Type type = Command.Type.valueOf(tokens[0]);

        String key = tokens.length > 1 ? tokens[1] : null;
        String value = tokens.length > 2 ? tokens[2] : null;
        Long ttl = null;

        for (int i = 3; i < tokens.length - 1; i++) {
            if (tokens[i].equalsIgnoreCase("EX")) {
                ttl = Long.parseLong(tokens[i + 1]);
            }
        }

        return new Command(type, key, value, ttl);
    }

}
