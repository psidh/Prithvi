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

        String[] parts = input.trim().split("\\s+", 5);
        Command.Type type = Command.Type.fromString(parts[0]);

        switch (type) {
            case SET:
                if (parts.length == 3) {
                    return new Command(type, parts[1], parts[2]);
                } else if (parts.length == 5 && parts[3].equalsIgnoreCase("EX")) {
                    try {
                        long ttl = Long.parseLong(parts[4]);
                        return new Command(type, parts[1], parts[2], ttl);
                    } catch (NumberFormatException e) {
                        return Command.unknown();
                    }
                } else {
                    return Command.unknown();
                }

            case GET:
                return (parts.length == 2)
                        ? new Command(type, parts[1], null)
                        : Command.unknown();

            case DEL:
                return (parts.length == 2)
                        ? new Command(type, parts[1], null)
                        : Command.unknown();

            case FLUSH:
            case FLUSHALL:
            case LISTALL:
            case SAVE:
            case LOAD:
            case HELP:
            case QUIT:
                return (parts.length == 1)
                        ? new Command(type, null, null)
                        : Command.unknown();

            case EXISTS:
                return (parts.length == 2)
                        ? new Command(type, parts[1], null)
                        : Command.unknown();

            default:
                return Command.unknown();
        }
    }

}
