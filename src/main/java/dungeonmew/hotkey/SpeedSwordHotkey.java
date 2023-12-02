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

// switch to heal wand on key press and then switch back to previous hand slot
@Environment(EnvType.CLIENT)
public class SpeedSwordHotkey {
    private static KeyBinding keyBinding;
    private static int savedSwordSlot;
    private static int savedHandSlot;

    public static void init() {
        savedSwordSlot = -2;
        savedHandSlot = -1;
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.dungeonmew.speed_sword",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.dungeonmew"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
                assert client.player != null;
                PlayerInventory inv = client.player.getInventory();

                if (inv.selectedSlot == savedSwordSlot){// switch back to previous slot before hotkey was pressed
                    int diff = inv.selectedSlot - savedHandSlot;

                    for(int j = 0; j <  Math.abs(diff); j++) {
                        inv.scrollInHotbar((diff));
                    }
                }
                else {
                    for (int i = 0; i < 9; i++) {
                        ItemStack item = inv.getStack(i);
                        if (item.isOf(Items.DIAMOND_SWORD) && (ItemFacts.getBaseAbilitySpeedAmount(ItemFacts.getCustomModelData(item)) > 0)){
                            savedSwordSlot = i;
                            savedHandSlot = inv.selectedSlot;
                            int diff = inv.selectedSlot - savedSwordSlot;
                            for(int j = 0; j <  Math.abs(diff); j++) {
                                inv.scrollInHotbar((diff));
                            }
                            break;
                        }

                    }
                }
            }
        });
    }
}
