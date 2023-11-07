package ddapi.player;

import com.mojang.logging.LogUtils;
import ddapi.event.SentMessageEvents;
import dungeonmew.DungeonMewClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.slf4j.Logger;

import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public final class ExperienceTracker {
    private enum State {
        NONE,
        WAIT,
        ERROR,
        SUCCESS
    }

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Pattern EXPERIENCE_LEVELS_REGEX = Pattern.compile("Your Level Experience: (\\d*\\.?\\d*)\\.");
    private static final Pattern EXPERIENCE_OVERLAY_REGEX = Pattern.compile("\\(\\+(\\d*\\.?\\d*) exp\\) \\(\\d*.?\\d*%\\)");
    private static final Pattern EXPERIENCE_STASH_REGEX = Pattern.compile("Treasure: You have found a Experience Stash! \\((\\d*.?\\d*)\\)");

    private static State state = State.NONE;
    private static float experience;
    private static int levelOrdinal = -1;

    public static float getExperience() {
        return experience;
    }

    public static Level getCurrentLevel() {
        if (levelOrdinal == -1) {
            return null;
        }
        return Level.values()[levelOrdinal];
    }

    public static Level getNextLevel() {
        if (levelOrdinal == Level.COUNT.ordinal()) {
            return null;
        }
        return Level.values()[levelOrdinal + 1];
    }

    public static void init() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            levelOrdinal = -1;
            state = State.NONE;
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            levelOrdinal = -1;
            state = State.NONE;
        });

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.getNetworkHandler() != null && state == State.NONE) {
                client.getNetworkHandler().sendCommand("exp");
                state = State.WAIT;
            }
        });

        SentMessageEvents.CHAT_ON.register((client, message) -> {
            try {
                var matcher = EXPERIENCE_LEVELS_REGEX.matcher(message);
                if (state == State.WAIT && matcher.find()) {
                    experience = Float.parseFloat(matcher.group(1));
                    state = State.SUCCESS;
                    searchLevel();

                    LOGGER.info("Successfully obtained current total experience!");
                    return SentMessageEvents.ReturnState.CANCEL;
                }
            }
            catch (Exception ignored) {
                LOGGER.error(String.format("Invalid experience message! Got: %s", message));
                state = State.ERROR;
                return SentMessageEvents.ReturnState.CANCEL;
            }

            return SentMessageEvents.ReturnState.DEFAULT;
        });

        SentMessageEvents.CHAT_ON.register((client, message) -> {
            var matcher = EXPERIENCE_STASH_REGEX.matcher(message);

            if (matcher.find()) {
                increaseExperience(Float.parseFloat(matcher.group(1)));
            }

            return SentMessageEvents.ReturnState.DEFAULT;
        });

        SentMessageEvents.OVERLAY_ON.register((client, message) -> {
            try {
                var matcher = EXPERIENCE_OVERLAY_REGEX.matcher(message);
                if (matcher.find() && state == State.SUCCESS) {
                    increaseExperience(Float.parseFloat(matcher.group(1)));
                }
            }
            catch (Exception ignored) {
                LOGGER.error(String.format("Invalid experience overlay! Got: %s", message));
            }

            return SentMessageEvents.ReturnState.DEFAULT;
        });
    }

    public static void increaseExperience(float amount) {
        experience += amount;

        if (getNextLevel() != null && experience >= getNextLevel().getTotalExperience()) {
            levelOrdinal++;
        }
    }

    public static void searchLevel() {
        int index = 0;

        Level[] levels = Level.values();
        for (int i = 0; i < levels.length - 2; i++) {
            float totalExp = levels[i].getTotalExperience();
            float nextTotalExp = levels[i + 1].getTotalExperience();

            if (experience >= totalExp && experience < nextTotalExp) {
                index = i;
                break;
            }
        }

        levelOrdinal = index;
    }
}
