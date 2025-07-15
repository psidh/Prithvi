package src.db;

import java.io.PrintWriter;

import src.commands.LoadCommand;

public class AutoLoad implements Runnable {
    @Override
    public void run() {
        try {
            LoadCommand loadCmd = new LoadCommand();
            
            loadCmd.execute(null, new PrintWriter(System.out, true), null, Store.get());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
