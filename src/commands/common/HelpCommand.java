package src.commands.common;

import java.io.BufferedReader;
import java.io.PrintWriter;

import src.Command;
import src.CommandExecutor;
import src.db.ValueWithExpiry;
import java.util.Map;

public class HelpCommand implements CommandExecutor {
    @Override
    public void execute(Command cmd, PrintWriter writer, BufferedReader reader,
            Map<String, ValueWithExpiry> store) {

        writer.println("\nPRITHVI COMMANDS — Quick Reference");
        writer.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        writer.println("SET <key> <value>                → Store key with value");
        writer.println("SET <key> <value> EX <seconds>   → Store key with TTL (expiry in seconds)");
        writer.println("GET <key>                        → Retrieve value and expiry for a key");
        writer.println("DEL <key>                        → Delete a specific key");
        writer.println("EXISTS <key>                     → Check if a key exists");
        writer.println("LPUSH <key>                      → Pushes data into a Double Ended queue from the left");
        writer.println("RPUSH <key>                      → Pushes data into a Double Ended queue from the right");
        writer.println("RPOP <key>                       → Pops data from a Double Ended queue from the right");
        writer.println("LPOP <key>                       → Pops data from a Double Ended queue from the left");
        writer.println("GETLIST <key>                    → Shows Keys for the available list in the store");
        writer.println("KEYS                             → List all keys in the store");
        writer.println("FLUSH                            → Clear all keys (with confirmation)");
        writer.println("FLUSH FALL                       → Clear all keys without confirmation");
        writer.println("SAVE                             → Persist current store to disk");
        writer.println("LOAD                             → Load store from disk");
        writer.println("QUIT                             → Gracefully close client connection");
        writer.println("HELP                             → Display this help menu");

        writer.println("\n⚙ FEATURES");
        writer.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        writer.println("In-Memory Key-Value Store       (No frameworks, pure Java)");
        writer.println("Persistence to Disk             (JSON-based store with expiry)");
        writer.println("TTL Expiry Support             (Auto-remove expired keys in background)");
        writer.println("AutoSave + AutoLoad            (Periodic save & load on startup)");
        writer.println("Multi-threaded Client Handling  (Each connection runs in a separate thread)");
        writer.println("Graceful Shutdown               (SIGINT handler saves store)");

        writer.println("\nNOTE");
        writer.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        writer.println("- TTL is optional in SET. Without EX, keys live forever.");
        writer.println("- Expired keys are removed automatically in background.");
        writer.println("- Use SAVE before shutdown if AutoSave is disabled.");
        writer.println("- Data is saved to 'data/store.json'");

        writer.println("\n🔚 END OF HELP\n");
        writer.flush();
    }
}
