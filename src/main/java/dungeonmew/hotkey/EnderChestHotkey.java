package dungeonmew.hotkey;

import dungeonmew.DungeonMewClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public final class EnderChestHotkey {
    private static KeyBinding keyBinding;

    public static void init() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.dungeonmew.ender_chest",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.dungeonmew"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (DungeonMewClient.isConnectedToDungeonDodge() && keyBinding.wasPressed()) {
                assert client.player != null;
                client.player.networkHandler.sendCommand("ec");
            }
        });
    }
}
