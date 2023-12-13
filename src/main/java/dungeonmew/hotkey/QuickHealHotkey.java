package dungeonmew.hotkey;

import ddapi.item.ItemFacts;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

// switch to speed sword on key press and then switch back to previous hand slot
@Environment(EnvType.CLIENT)
public class QuickHealHotkey {
    private static KeyBinding keyBinding;
    private static int savedSwordSlot;
    private static int savedHandSlot;

    public static void init() {
        savedSwordSlot = -2;
        savedHandSlot = -1;

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.dungeonmew.quick_heal",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.dungeonmew"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
                assert client.player != null;
                PlayerInventory inv = client.player.getInventory();

                if (inv.selectedSlot == savedSwordSlot) { // switch back to previous slot before hotkey was pressed
                    scrollToSlot(inv, savedHandSlot);
                }
                else {
                    int largestMaxHeal = 0;
                    int invSlot = inv.selectedSlot;

                    for (int i = 0; i < 9; i++) {
                        var item = inv.getStack(i);
                        int healAmount = ItemFacts.getBaseHealAmount(item);
                        if (healAmount > largestMaxHeal) {
                            invSlot = i;
                            largestMaxHeal = healAmount;
                        }
                    }

                    savedHandSlot = inv.selectedSlot;
                    savedSwordSlot = invSlot;

                    scrollToSlot(inv, savedSwordSlot);
                }
            }
        });
    }

    public static void scrollToSlot(PlayerInventory inv, int slot) {
        int diff = inv.selectedSlot - slot;
        int dist = Math.abs(diff);
        for(int j = 0; j < dist; j++) {
            inv.scrollInHotbar(diff);
        }
    }
}
