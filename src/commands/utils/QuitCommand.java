package src.commands.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import src.commands.Command;
import src.commands.CommandExecutor;
import src.db.ValueWithExpiry;
import java.util.Map;

public class QuitCommand implements CommandExecutor {

    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) throws IOException {
        writer.println("Are you sure you want to terminate the connection? Type \"YES\" to confirm:");

        String line = reader.readLine();
        if ("YES".equalsIgnoreCase(line.trim())) {
            writer.println("Goodbye! Connection closed.");
            throw new QuitException();
        } else {
            writer.println("Quit aborted.");
        }
    }
}
