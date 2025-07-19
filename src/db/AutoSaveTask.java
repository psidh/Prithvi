package src.db;

import java.io.PrintWriter;

import src.commands.utils.SaveCommand;

public class AutoSaveTask implements Runnable {
    private final int intervalSeconds;

    public AutoSaveTask(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    @Override
    public void run() {
        SaveCommand saveCommand = new SaveCommand();

        while (true) {
            try {
                Thread.sleep(intervalSeconds * 1000L);

                saveCommand.execute(
                        null,
                        new PrintWriter(System.out, true),
                        null,
                        Store.get());

            } catch (Exception e) {
                System.err.println("AutoSave failed: " + e.getMessage());
            }
        }
    }
}
