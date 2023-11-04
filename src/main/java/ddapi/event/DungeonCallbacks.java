package ddapi.event;

import ddapi.dungeon.DungeonType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public final class DungeonCallbacks {
    @FunctionalInterface
    public interface OnEnterDungeon {
        void onEnterDungeon(MinecraftClient client, DungeonType dungeonType);
    }

    public static final Event<OnEnterDungeon> ENTER = EventFactory.createArrayBacked(OnEnterDungeon.class, (callbacks) -> (client, dungeonType) -> {
        for (OnEnterDungeon callback : callbacks) {
            callback.onEnterDungeon(client, dungeonType);
        }
    });

    @FunctionalInterface
    public interface OnDeathDungeon {
        void onDeathDungeon(MinecraftClient client, DungeonType dungeonType);
    }

    public static final Event<OnDeathDungeon> DEATH = EventFactory.createArrayBacked(OnDeathDungeon.class, (callbacks) -> (client, dungeonType) -> {
        for (OnDeathDungeon callback : callbacks) {
            callback.onDeathDungeon(client, dungeonType);
        }
    });

    @FunctionalInterface
    public interface OnLeaveDungeon {
        void onLeaveDungeon(MinecraftClient client, DungeonType dungeonType);
    }

    public static final Event<OnLeaveDungeon> LEAVE = EventFactory.createArrayBacked(OnLeaveDungeon.class, (callbacks) -> (client, dungeonType) -> {
        for (OnLeaveDungeon callback : callbacks) {
            callback.onLeaveDungeon(client, dungeonType);
        }
    });
}
