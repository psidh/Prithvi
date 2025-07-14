package src.commands;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;
import src.db.ValueWithExpiry;

public class SetCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, ValueWithExpiry> store) {

        ValueWithExpiry value = (cmd.ttlSeconds != null && cmd.ttlSeconds != Long.MAX_VALUE)
                ? new ValueWithExpiry(cmd.value, cmd.ttlSeconds)
                : new ValueWithExpiry(cmd.value);

        store.put(cmd.key, value);
        writer.println("OK");
    }
}
