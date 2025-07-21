package src.commands;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;

import src.commands.common.*;
import src.commands.map.*;
import src.commands.utils.*;
import src.commands.set.*;
import src.commands.queue.*;

public class CommandMap {

    private static <K, V> Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    public static Map<Command.Type, CommandExecutor> getCommandMap() {
        return Map.ofEntries(
                entry(Command.Type.SET, new SetCommand()),
                entry(Command.Type.GET, new GetCommand()),
                entry(Command.Type.DEL, new DelCommand()),
                entry(Command.Type.FLUSH, new FlushCommand()),
                entry(Command.Type.EXISTS, new ExistsCommand()),
                entry(Command.Type.LISTALL, new ListAllCommand()),
                entry(Command.Type.SAVE, new SaveCommand()),
                entry(Command.Type.LOAD, new LoadCommand()),
                entry(Command.Type.QUIT, new QuitCommand()),
                entry(Command.Type.FLUSHALL, new FlushAllCommand()),
                entry(Command.Type.HELP, new HelpCommand()),
                entry(Command.Type.INFO, new InfoCommand()),
                entry(Command.Type.LPUSH, new LPushCommand()),
                entry(Command.Type.RPUSH, new RPushCommand()),
                entry(Command.Type.RPOP, new RPopCommand()),
                entry(Command.Type.LPOP, new LPopCommand()),
                entry(Command.Type.GETLIST, new GETLISTCommand()),
                entry(Command.Type.SADD, new SAddCommand()),
                entry(Command.Type.SMEMBERS, new SMembersCommand()),
                entry(Command.Type.SREM, new SRemCommand()));
    }
}
