package src.db;

import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> get() {
        return store;
    }
}
