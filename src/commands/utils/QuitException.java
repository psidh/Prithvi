package src.commands.utils;

public class QuitException extends RuntimeException {
    public QuitException() {
        super("Client request to quit.");
    }
}