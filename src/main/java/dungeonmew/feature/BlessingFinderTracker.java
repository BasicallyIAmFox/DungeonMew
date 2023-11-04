package dungeonmew.feature;

import ddapi.player.DungeonTracker;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public final class BlessingFinderTracker {
    private static final Set<BlockPos> foundBlessings = new HashSet<>();

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!DungeonTracker.inDungeon()) {
                clear();
            }
        });
    }

    public static boolean isMarked(BlockPos blockPos) {
        return foundBlessings.contains(blockPos);
    }

    public static void mark(BlockPos blockPos) {
        foundBlessings.add(blockPos);
    }

    public static void clear() {
        foundBlessings.clear();
    }
}
