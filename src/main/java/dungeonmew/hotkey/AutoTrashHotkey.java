package dungeonmew.hotkey;

import com.mojang.logging.LogUtils;
import ddapi.item.ItemFacts;
import dungeonmew.feature.Features;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public final class AutoTrashHotkey {
    private static KeyBinding keyBinding;
    private static Set<Integer> TrashSet;

    public static void init() {
        TrashSet = Features.QUICK_TRASH.getValue().getIDs();

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.dungeonmew.quick_trash",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DELETE,
                "category.dungeonmew"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
                assert client.player != null;
                client.player.networkHandler.sendCommand("trash");
                new Thread(AutoTrashHotkey::tryClickTrashItems).start();
            }
        });
    }
    private static void tryClickTrashItems() {
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
        PlayerInventory inv = client.player.getInventory();

        for(int i = 9; i < 36; i++){
            int adjusted = i + 27;
            if (TrashSet.contains(ItemFacts.getCustomModelData(inv.getStack(i)))) {
                client.interactionManager.clickSlot(syncId, adjusted, 0, SlotActionType.QUICK_MOVE, client.player);
            }
        }
        for(int i = 0; i < 9; i++){
            int adjusted = i + 63;
            if (TrashSet.contains(ItemFacts.getCustomModelData(inv.getStack(i)))){
                client.interactionManager.clickSlot(syncId, adjusted, 0, SlotActionType.QUICK_MOVE, client.player);
            }
        }

    }
}
