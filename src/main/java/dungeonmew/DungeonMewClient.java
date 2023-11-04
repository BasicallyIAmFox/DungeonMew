package dungeonmew;

import com.mojang.logging.LogUtils;
import ddapi.player.ClassTracker;
import ddapi.player.DungeonTracker;
import ddapi.player.ExperienceTracker;
import ddapi.player.Stats;
import dungeonmew.tracker.*;
import dungeonmew.feature.Feature;
import dungeonmew.feature.Features;
import dungeonmew.hotkey.EnderChestHotkey;
import dungeonmew.io.FeatureIO;
import dungeonmew.shortcut.WarpSpawnShortcut;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.slf4j.Logger;

import java.util.Objects;

public class DungeonMewClient implements ClientModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();

    private static boolean isConnectedToDungeonDodge = false;

    public static boolean isConnectedToDungeonDodge() {
        return isConnectedToDungeonDodge;
    }

    @Override
    public void onInitializeClient() {
        initFeatures();
        registerConnectionEvents();

        registerHotkeys();
        registerShortcuts();

        registerTrackers();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var rootNode = ClientCommandManager.literal(Constants.MOD_ID).build();

            for (var feature : Feature.getFeatures()) {
                feature.registerAsCommand(rootNode);
            }

            dispatcher.getRoot().addChild(rootNode);
        });
    }

    private static void initFeatures() {
        Features.init();
        FeatureIO.create();

        for (var feature : Feature.getFeatures()) {
            feature.init();
        }

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            FeatureIO.save();
        });
    }

    private static void registerConnectionEvents() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.getCurrentServerEntry() != null && !client.getCurrentServerEntry().isLocal()) {
                isConnectedToDungeonDodge = Objects.equals(client.getCurrentServerEntry().address, Constants.DUNGEON_DODGE_IP);
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            isConnectedToDungeonDodge = false;
        });
    }

    private static void registerTrackers() {
        ArrowTracker.init();
        ClassTracker.init();
        DungeonTracker.init();
        ExperienceTracker.init();
        Stats.init();
    }

    private static void registerHotkeys() {
        EnderChestHotkey.init();
    }

    private static void registerShortcuts() {
        WarpSpawnShortcut.init();
    }

    private static final int BRACKETS_COLOR = 0x2F_4F_4F;
    private static final int MOD_NAME_COLOR = 0xDA_70_D6;
    private static final int MESSAGE_COLOR = 0xC0_C0_C0;
    private static final MutableText commandText =
            Text.literal("[").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(BRACKETS_COLOR)))
                    .append(Text.literal("DungeonMew").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(MOD_NAME_COLOR))))
                    .append(Text.literal("] ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(BRACKETS_COLOR))));

    public static Text systemText(MutableText text) {
        return commandText.copy().append(text.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(MESSAGE_COLOR))));
    }
}
