package src.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;
import src.db.ValueWithExpiry;

public class ListAllCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, ValueWithExpiry> store) {
        if (store.isEmpty()) {
            writer.println("No Keys in database");
            return;
        }
        writer.println("Keys in the database currently.");
        int index = 1;

        for (Map.Entry<String, ValueWithExpiry> entry : store.entrySet()) {
            writer.println(index + ". " + entry.getKey() + " : " + entry.getValue());
            index++;
        }
    }
}
