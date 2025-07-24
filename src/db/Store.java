/**
 * Prithvi – An in-memory key-value database
 * with TTL, LRU eviction, disk persistence, lists, sets,
 * and multithreaded client support.
 *
 * © 2025 PHILKHANA SIDHARTH
 * Licensed under the Apache License, Version 2.0.
 * You may obtain a copy at:
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * You must retain this header in any redistribution or modification.
 */

package src.db;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Store {
    private static final int MAX_CAPACITY = 10000;

    private static final Map<String, ValueWithExpiry> store = Collections.synchronizedMap(
            new LinkedHashMap<String, ValueWithExpiry>(MAX_CAPACITY, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, ValueWithExpiry> eldest) {
                    return size() > MAX_CAPACITY;
                }
            });

    public static Map<String, ValueWithExpiry> get() {
        return store;
    }
}
