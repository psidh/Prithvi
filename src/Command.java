package src;

public class Command {
    public enum Type {
        SET, GET, DEL, UNKNOWN, FLUSH, EXISTS, LISTALL, SAVE;

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

    public Command(Type type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public static Command unknown() {
        return new Command(Type.UNKNOWN, null, null);
    }
}
