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

    public static void init() {
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
                int largestMaxHeal = 0;
                int invslot = inv.selectedSlot;
                for (int i = 0; i < 9; i++) {
                    ItemStack item = inv.getStack(i);
                    int healAmount = ItemFacts.getBaseHealAmount(item);
                    if (healAmount > largestMaxHeal){
                        invslot = i;
                        largestMaxHeal = healAmount;
                    }

                }
                int savedHandSlot = inv.selectedSlot;
                int savedSwordSlot = invslot;
                scrollToSlot(inv, savedSwordSlot);
                assert client.interactionManager != null;
                client.interactionManager.interactItem(client.player, client.player.getActiveHand()); // test?
                scrollToSlot(inv, savedHandSlot);
            }
        });
    }


    private static void scrollToSlot(PlayerInventory inv, int slot){

        int diff = inv.selectedSlot - slot;
        System.out.println(diff);
        int dist = Math.abs(diff);
        for(int j = 0; j <  dist; j++) {
            inv.scrollInHotbar(diff);
        }
    }
}
