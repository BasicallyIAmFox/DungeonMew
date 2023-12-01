package dungeonmew.hotkey;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ScheduleCommand;
import org.lwjgl.glfw.GLFW;

import static dungeonmew.DungeonMewClient.LOGGER;

// switch to speed sword on key press and then switch back to previous hand slot
@Environment(EnvType.CLIENT)
public class SpeedSwordHotkey {
    private static KeyBinding keyBinding;

    static class switchBack extends Thread{
        static int savedHandSlot;

        public void run() {
            try {
                Thread.sleep(50); // idk how im supposed to do client side scheduling wth

            }
            catch (Exception e) {
                LOGGER.error("Error occured trying to delay in thread (" + Thread.currentThread() + ")");
            }
        }
    }

    public static void init() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.dungeonmew.speed_sword",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.dungeonmew"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
                assert client.player != null;

                // we switch back after we done
                switchBack.savedHandSlot = client.player.getInventory().selectedSlot;
                client.player.getInventory().getSlotWithStack(new ItemStack()))

            }
        });
    }
}
