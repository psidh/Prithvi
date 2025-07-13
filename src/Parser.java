package src;

import java.io.BufferedReader;
import java.io.IOException;

public class Parser {
    private final BufferedReader reader;

    public Parser(BufferedReader reader) {
        this.reader = reader;
    }

    public Command parseNextCommand() throws IOException {

        String input = reader.readLine();

        if (input == null || input.isEmpty()) {
            return Command.unknown();
        }

        String[] parts = input.trim().split("\\s+", 3);
        Command.Type type = Command.Type.fromString(parts[0]);

        return switch (type) {
            case SET -> (parts.length == 3)
                    ? new Command(type, parts[1], parts[2])
                    : Command.unknown();

            case GET -> (parts.length == 2)
                    ? new Command(type, parts[1], null)
                    : Command.unknown();
            case DEL -> (parts.length == 2) ? new Command(type, parts[1], null) : Command.unknown();
            case FLUSH -> (parts.length == 1) ? new Command(type, null, null) : Command.unknown();
            default -> Command.unknown();
        };
    }

}
