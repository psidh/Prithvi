package src.db;

public class ValueWithExpiry {
    public final String value;
    public final long expiryTimestamp; 

    public ValueWithExpiry(String value) {
        this.value = value;
        this.expiryTimestamp = Long.MAX_VALUE; 
    }

    public ValueWithExpiry(String value, long ttlSeconds) {
        this.value = value;
        this.expiryTimestamp = (ttlSeconds == Long.MAX_VALUE)
                ? Long.MAX_VALUE
                : System.currentTimeMillis() + ttlSeconds * 1000;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTimestamp;
    }
}
