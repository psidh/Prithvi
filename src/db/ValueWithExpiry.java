package src.db;

import java.util.Deque;

public class ValueWithExpiry {
    public final Object value;
    public final ValueType type;
    public final long expiryTimestamp;

    public ValueWithExpiry(Object value) {
        this.value = value;
        this.expiryTimestamp = Long.MAX_VALUE;
        this.type = ValueType.STRING;
    }

    public ValueWithExpiry(Object value, long ttlSeconds) {
        this.value = value;
        this.type = ValueType.STRING;
        this.expiryTimestamp = (ttlSeconds == Long.MAX_VALUE)
                ? Long.MAX_VALUE
                : System.currentTimeMillis() + ttlSeconds * 1000;
    }

    public ValueWithExpiry(Object value, ValueType type) {
        this.value = value;
        this.expiryTimestamp = Long.MAX_VALUE;
        this.type = ValueType.LIST;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTimestamp;
    }

    @SuppressWarnings("unchecked")
    public Deque<String> getList() {
        return (Deque<String>) value;
    }

    public String getString() {
        return (String) value;
    }
}
