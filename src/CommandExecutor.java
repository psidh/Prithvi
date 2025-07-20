package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import src.db.ValueWithExpiry;

public interface CommandExecutor {
    void execute(Command cmd,
            PrintWriter writer,
            BufferedReader reader,
            Map<String, ValueWithExpiry> store) throws IOException;
}
