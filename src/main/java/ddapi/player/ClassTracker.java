package ddapi.player;

import ddapi.event.DungeonCallbacks;
import ddapi.event.SentMessageEvents;
import dungeonmew.feature.CooldownTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public final class ClassTracker {
    private static final Pattern SELECTED_CLASS_REGEX = Pattern.compile("You have selected the (\\w*) class!");

    private static ClassType classType = null;

    public static ClassType getClassType() {
        return classType;
    }

    public static void init() {
        SentMessageEvents.CHAT_ON.register((client, message) -> {
            if (!DungeonTracker.inDungeon())
                return SentMessageEvents.ReturnState.DEFAULT;

            var matcher = SELECTED_CLASS_REGEX.matcher(message);
            if (matcher.find()) {
                setClass(ClassType.valueOf(matcher.group(1).toUpperCase(Locale.ROOT)));
            }
            else if (classType != null && message.equals(classType.getClassMessage())) {
                CooldownTracker.addAbilityWithCooldown(classType.getClassAbility(), classType.getClassCooldown() * 20);
            }

            return SentMessageEvents.ReturnState.DEFAULT;
        });

        DungeonCallbacks.ENTER.register((client, dungeonType) -> setClass(ClassType.WARRIOR));
        DungeonCallbacks.LEAVE.register((client, dungeonType) -> setClass(null));
    }

    private static void setClass(ClassType type) {
        classType = Objects.requireNonNullElse(type, ClassType.WARRIOR);
    }
}
