
package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

public interface CommandExecutor {
    abstract void execute(Command cmd, PrintWriter writer, BufferedReader reader, ConcurrentHashMap<String, String> store)
            throws IOException;
}
