package src.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;

public class SetCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, String> store) {
        store.put(cmd.key, cmd.value);
        writer.println("OK");
    }
}
