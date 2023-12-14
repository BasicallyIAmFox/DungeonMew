package dungeonmew.hotkey;

import com.mojang.logging.LogUtils;
import ddapi.item.ItemFacts;
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
    private static final int[] test = {
        // TODO: add these to their own config file, maybe add an itemdict class in ddapi
            // Weapons
            1000002, // bone blade
            1000009, // undead archer bow
            1000031, // stringshot
            1000032, // slime orb
            1000034, // spade
            1000035, // stone dagger

            3000053, // skeletal warrier helm
            3000054, // slime boots
            3000055, // 55 - 58 undead knight armor set
            3000056,
            3000057,
            3000058,
            3000059, // undead archer helm
            3000060, // undead soldier chest
            3000061, // undead archer legs
            3000062, // rotten heart
            3000063, // rotten healer chest
            3000064, // bloody healer legs

            5000000, // flesh
            5000001, // bone
            5000002, // stick
            5000003, // slime
            5000004, // rock
            5000006, // string
            5000007, // nugget



            5600001, // zombie pet
            5600005, // skele pet
            5600010, // slime pet
            5600006, // spider pet



    };

    private static Set<Integer> TrashSet;

    public static void init() {
        TrashSet = new HashSet<>();
        Arrays.stream(test).forEach(s->{
            TrashSet.add(s);
        });
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
            System.out.println("Slot #" + i + " contains " + inv.getStack(i));
            int adjusted = i + 27;
            if (TrashSet.contains(ItemFacts.getCustomModelData(inv.getStack(i)))) {
                System.out.println("Attempting delete on slot " + i + " with adjusted slot value " + adjusted + " with item " + inv.getStack(i) + " with custom item data " + ItemFacts.getCustomModelData(inv.getStack(i)));
                client.interactionManager.clickSlot(syncId, adjusted, 0, SlotActionType.QUICK_MOVE, client.player);
            }
        }
        for(int i = 0; i < 9; i++){
            System.out.println("Slot #" + i + " contains " + inv.getStack(i));
            int adjusted = i + 63;
            if (TrashSet.contains(ItemFacts.getCustomModelData(inv.getStack(i)))){
                System.out.println("Attempting delete on slot " + i + " with adjusted slot value " + adjusted + " with item " + inv.getStack(i) + " with custom item data " + ItemFacts.getCustomModelData(inv.getStack(i)));
                client.interactionManager.clickSlot(syncId, adjusted, 0, SlotActionType.QUICK_MOVE, client.player);
            }
        }

    }
}
