package dungeonmew.feature;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.logging.LogUtils;
import ddapi.event.SentMessageEvents;
import dungeonmew.feature.binding.BlessingFinderConfigurationBinding;
import dungeonmew.feature.binding.BoolBinding;
import dungeonmew.feature.binding.BrightnessConfigurationBinding;
import dungeonmew.feature.binding.ColorBinding;
import dungeonmew.util.FormattingUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public final class Features {
    public static final Feature<BlessingFinderConfiguration> BLESSING_FINDER = new Feature<>(
            "blessing_finder",
            "blessingFinder",
            new BlessingFinderConfigurationBinding(new BlessingFinderConfiguration(true, new Color(220, 180, 60)))
    ) {
        @Override
        public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
            BlessingFinderCommands.registerBool(rootNode);
        }
    };

    public static final Feature<Color> BLESSING_FINDER_COLOR = new Feature<>(
            "blessing_finder_color",
            "blessingFinderColor",
            new ColorBinding(new Color(220, 180, 60))
    ) {
        @Override
        public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
            BlessingFinderCommands.registerColor(rootNode);
        }
    };
    public static final Feature<BlessingFinderConfiguration> ESSENCE_FINDER = new Feature<>(
            "essence_finder",
            "essenceFinder",
            new BlessingFinderConfigurationBinding(new BlessingFinderConfiguration(true, new Color(101, 31, 73, 255)))
    ) {
        @Override
        public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
            EssenceFinderCommands.registerBool(rootNode);
        }
    };

    public static final Feature<Color> ESSENCE_FINDER_COLOR = new Feature<>(
            "essence_finder_color",
            "essenceFinderColor",
            new ColorBinding(new Color(101, 31, 73))
    ) {
        @Override
        public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
            EssenceFinderCommands.registerColor(rootNode);
        }
    };

    public static final Feature<BrightnessConfiguration> CONFIGURE_BRIGHTNESS = new Feature<>(
            "configure_brightness",
            "configureBrightness",
            new BrightnessConfigurationBinding(BrightnessConfiguration.DEFAULT)
    ) {
        @Override
        public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
            BrightnessCommand.register(getName(), rootNode);
        }
    };

    public static final Feature<Boolean> COOLDOWN_NOTIFICATIONS = new Feature<>(
            "cooldown_notifications",
            "cooldownNotifications",
            new BoolBinding(true)
    ) {
        @Override
        public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
            CooldownNotificationsCommand.register(getName(), rootNode);
        }
    };

    public static final Feature<Boolean> DISPLAY_ARMOR_BAR = new Feature<>(
            "display_armor_bar",
            "displayArmorBar",
            new BoolBinding(false)
    ) {
        @Override
        public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
            DisplayArmorBarCommand.register(getName(), rootNode);
        }
    };

    public static final Feature<Boolean> DISPLAY_EXPERIENCE_ORB = new Feature<>(
            "display_experience_orb",
            "displayExperienceOrb",
            new BoolBinding(true)
    ) {
        @Override
        public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
            DisplayExperienceOrbCommand.register(getName(), rootNode);
        }
    };

    public static final Feature<Boolean> HIDE_DARKNESS = new Feature<>(
            "hide_darkness",
            "hideDarkness",
            new BoolBinding(true)
    ) {
        @Override
        public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
            HideDarknessCommand.register(getName(), rootNode);
        }
    };

    public static final Feature<Boolean> SHOW_CUSTOM_MODEL_DATA = new Feature<>(
            "show_custom_model_data",
            "showCustomModelData",
            new BoolBinding(false)
    ) {
        @Override
        public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
            ShowCMDCommand.register(getName(), rootNode);
        }
    };

    public static void init() {
    }
}
