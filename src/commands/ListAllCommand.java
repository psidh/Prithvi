package src.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;

public class ListAllCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, String> store) {
        writer.println("Keys in the database currently.");
        int index = 1;

        for (Map.Entry<String, String> entry : store.entrySet()) {
            writer.println(index + ". " + entry.getKey() + " : " + entry.getValue());
            index++;
        }
    }
}
