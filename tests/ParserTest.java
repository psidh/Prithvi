package tests;

import src.Command;
import src.Parser;

import java.io.BufferedReader;
import java.io.StringReader;

public class ParserTest {
    public static void main(String[] args) throws Exception {
        test("SET foo bar", Command.Type.SET, "foo", "bar");
        test("GET foo", Command.Type.GET, "foo", null);
        test("SET onlyonearg", Command.Type.UNKNOWN, null, null);
        test("FLUSH something", Command.Type.UNKNOWN, null, null);
        test("", Command.Type.UNKNOWN, null, null);
    }

    private static void test(String input, Command.Type expectedType, String expectedKey, String expectedValue)
            throws Exception {
        Parser parser = new Parser(new BufferedReader(new StringReader(input)));
        Command cmd = parser.parseNextCommand();

        boolean pass = cmd.type == expectedType &&
                safeEquals(cmd.key, expectedKey) &&
                safeEquals(cmd.value, expectedValue);

        if (pass) {
            System.out.println("✅ PASS: " + input);
        } else {
            System.out.println("❌ FAIL: " + input);
            System.out.println(
                    "    Expected -> type: " + expectedType + ", key: " + expectedKey + ", value: " + expectedValue);
            System.out.println("    Got      -> type: " + cmd.type + ", key: " + cmd.key + ", value: " + cmd.value);
        }
    }

    private static boolean safeEquals(String a, String b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
}
