package src.commands;

public class QuitException extends RuntimeException {
    public QuitException() {
        super("Client request to quit.");
    }
}