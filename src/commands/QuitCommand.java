package src.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import src.Command;
import src.CommandExecutor;

public class QuitCommand implements CommandExecutor {

    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            ConcurrentHashMap<String, String> store) throws IOException {
        writer.println("Are you sure you want to terminate the connection? Type \"NO\" to confirm");
        String line = reader.readLine();
        if ("NO".equals(line)) {
            writer.println("👋 Goodbye! Connection closed.");
            writer.flush(); 
            throw new QuitException();
        } else {
            writer.println("❌ Quit aborted.");
        }
    }

}
