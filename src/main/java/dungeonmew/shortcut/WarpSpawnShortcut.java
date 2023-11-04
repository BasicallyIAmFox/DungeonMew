package dungeonmew.shortcut;

import com.mojang.brigadier.Command;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public final class WarpSpawnShortcut {
    public static void init() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var warpSpawnCommand = literal("ws")
                    .executes(context -> {
                        context.getSource().getPlayer().networkHandler.sendCommand("warp");
                        new Thread(WarpSpawnShortcut::tryClickSpawnSlot).start();

                        return Command.SINGLE_SUCCESS;
                    })
                    .build();

            dispatcher.getRoot().addChild(warpSpawnCommand);
        });
    }

    private static void tryClickSpawnSlot() {
        var client = MinecraftClient.getInstance();

        while (!(client.currentScreen instanceof GenericContainerScreen containerScreen)) {
            try {
                //noinspection BusyWait
                Thread.sleep(1);
            }
            catch (Exception ignored) {
            }
        }

        int syncId = containerScreen.getScreenHandler().syncId;

        assert client.interactionManager != null && client.player != null;
        client.interactionManager.clickSlot(syncId, 12, 0, SlotActionType.PICKUP, client.player);
    }
}
