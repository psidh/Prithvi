package src.db;

import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final ConcurrentHashMap<String, ValueWithExpiry> store = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ValueWithExpiry> get() {
        return store;
    }
}
