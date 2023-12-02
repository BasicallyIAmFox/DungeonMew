package ddapi.player;

import ddapi.dungeon.DungeonType;
import ddapi.event.DungeonCallbacks;
import ddapi.event.SentMessageEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public final class DungeonTracker {
    private static final Pattern ENTER_DUNGEON_REGEX = Pattern.compile("You have entered the (\\w*) dungeon!");

    private static boolean isInDungeon = false;
    private static DungeonType dungeonType = DungeonType.UNKNOWN;

    public static boolean inDungeon() {
        return isInDungeon;
    }

    public static DungeonType getDungeonType() {
        return dungeonType;
    }

    public static void init() {
        SentMessageEvents.CHAT_ON.register((client, message) -> {
            var matcher = ENTER_DUNGEON_REGEX.matcher(message);
            if (matcher.find()) {
                isInDungeon = true;
                dungeonType = DungeonType.valueOf(matcher.group(1).toUpperCase());
                client.getNetworkHandler().sendCommand("selectclass");

                DungeonCallbacks.ENTER.invoker().onEnterDungeon(client, dungeonType);
            }
            else if (message.equals("Dungeon failed! The whole team died!")) {
                isInDungeon = false;
                DungeonCallbacks.DEATH.invoker().onDeathDungeon(client, dungeonType);
            }
            else if (message.equals("Teleported you to spawn!") || message.equals("The boss has been defeated! The dungeon will end in 10 seconds!")) {
                isInDungeon = false;
                DungeonCallbacks.LEAVE.invoker().onLeaveDungeon(client, dungeonType);
            }

            return SentMessageEvents.ReturnState.DEFAULT;
        });
    }
}
