package src.db.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WALManager {
    private static final String WAL_FILE = "wal.log";
    private final BufferedWriter writer;
    private static WALManager instance;

    public WALManager(String fileName) throws IOException {
        FileWriter fw = new FileWriter(fileName, true);
        this.writer = new BufferedWriter(fw);
    }

    public synchronized void log(String command) {
        try {
            writer.write(System.currentTimeMillis() + " " + command);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readLogs() throws IOException {
        List<String> logs = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(WAL_FILE));
        String line;

        while ((line = reader.readLine()) != null) {
            logs.add(line);
        }
        reader.close();
        return logs;
    }

    public void close() throws IOException {
        writer.close();
    }

    public static WALManager getInstance() throws IOException {
        if (instance == null) {
            instance = new WALManager(WAL_FILE);
        }
        return instance;
    }
}
