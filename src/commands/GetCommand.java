package src.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;

public class GetCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, String> store) {
        String val = store.get(cmd.key);
        writer.println(val != null ? val : "nil");
    }
}
