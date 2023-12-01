package dungeonmew.hotkey;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.mixin.item.client.ClientPlayerInteractionManagerMixin;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import static dungeonmew.DungeonMewClient.LOGGER;

// switch to speed sword on key press and then switch back to previous hand slot
@Environment(EnvType.CLIENT)
public class SpeedSwordHotkey {
    private static KeyBinding keyBinding;
    static int savedSwordSlot;
    static int savedHandSlot;

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

                if (inv.selectedSlot == savedSwordSlot){// switch back
                    int diff = savedSwordSlot - savedHandSlot;

                    for(int j = 0; j <  Math.abs(diff); j++) {
                        inv.scrollInHotbar((diff) / Math.abs(diff));
                    }
                }
                else {
                    for (int i = 0; i < 9; i++) {
                        ItemStack item = inv.getStack(i);
                        if (item.getItem().asItem().equals(Items.DIAMOND_SWORD)){
//                                &&
//                                ( item.getItem().getName().toString().equals("Enchanted Sword") ||
//                                        item.getItem().getName().toString().equals("Magical Sword") )) { // check if hotbar has item
                            savedSwordSlot = i;
                            client.player.sendMessage(Text.literal(item.getName().getString()));
                            if (savedHandSlot == -1) {
                                savedHandSlot = inv.selectedSlot;
                            }
                            int diff = savedHandSlot - savedSwordSlot;
                            for(int j = 0; j <  Math.abs(diff); j++) {
                                inv.scrollInHotbar((diff) / Math.abs(diff));
                            }
                            break;
                        }

                    }
                }
            }
        });
    }
}
