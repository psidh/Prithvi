package src.db;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExpiredKeyRemover implements Runnable {
    private final int intervalSeconds;
    private final ConcurrentHashMap<String, ValueWithExpiry> store;

    public ExpiredKeyRemover(int intervalSeconds, ConcurrentHashMap<String, ValueWithExpiry> map) {
        this.intervalSeconds = intervalSeconds;
        this.store = map;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(intervalSeconds * 1000L);
                int removed = 0;

                Iterator<Map.Entry<String, ValueWithExpiry>> it = store.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry<String, ValueWithExpiry> entry = it.next();
                    if (entry.getValue().isExpired()) {
                        it.remove();
                        removed++;
                    }
                }

                if (removed > 0) {
                    System.out.println("🧹 Removed " + removed + " expired keys.");
                }

            } catch (Exception e) {
                System.err.println(" ExpiredKeyRemover failed: " + e.getMessage());
            }
        }
    }

}
