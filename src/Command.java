package src;

public class Command {
    public enum Type {
        SET, GET, DEL, UNKNOWN, FLUSH, EXISTS, LISTALL, SAVE, LOAD, QUIT, FLUSHALL, HELP, INFO, LPUSH, RPUSH, LPOP,
        RPOP, GETLIST, HSET, HGET, HDEL, HGETALL, SADD, SREM, SMEMBERS, AUTH, TOKEN;

        public static Type fromString(String s) {
            try {
                return Type.valueOf(s.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNKNOWN;
            }
        }
    }

    public Type type;
    public String key;
    public String value;
    public Long ttlSeconds;

    public Command(Type type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
        this.ttlSeconds = Long.MAX_VALUE;
    }

    public Command(Type type, String key, String value, Long ttlSeconds) {
        this.type = type;
        this.key = key;
        this.value = value;
        this.ttlSeconds = ttlSeconds;
    }

    public static Command unknown() {
        return new Command(Type.UNKNOWN, null, null);
    }
}
