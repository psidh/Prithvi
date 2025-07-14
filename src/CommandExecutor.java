package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import src.db.ValueWithExpiry;

public interface CommandExecutor {
    void execute(Command cmd,
            PrintWriter writer,
            BufferedReader reader,
            ConcurrentHashMap<String, ValueWithExpiry> store) throws IOException;
}
