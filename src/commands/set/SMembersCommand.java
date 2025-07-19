package src.commands.set;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;
import src.db.ValueType;
import src.db.ValueWithExpiry;

public class SMembersCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, ValueWithExpiry> store) {

        for (Map.Entry<String, ValueWithExpiry> entry : store.entrySet()) {
            ValueWithExpiry value = entry.getValue();
            int index = 1;
            if (value.type == ValueType.SET && !value.isExpired()) {
                writer.println(index++ + ")" + value.value);
            }
        }
    }
}
