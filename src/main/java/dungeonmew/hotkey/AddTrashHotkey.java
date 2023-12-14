package dungeonmew.hotkey;

import ddapi.item.ItemFacts;
import dungeonmew.DungeonMewClient;
import dungeonmew.feature.Features;
import dungeonmew.io.FeatureIO;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;

// adds an item to trash config
public class AddTrashHotkey {
    private static KeyBinding keyBinding;
    private static HashSet<Integer> TrashSet;

    public static void init(){
        TrashSet = Features.QUICK_TRASH.getValue().getIDs();
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.dungeonmew.add_trash",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_BACKSLASH,
                "category.dungeonmew"
        ));

        //TODO: add custom items dict with <id: name> pairs so we can get the names of the items later
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
                assert client.player != null;
                int heldItemID = ItemFacts.getCustomModelData(client.player.getMainHandStack());
                if (TrashSet.contains(heldItemID)){
                    client.player.sendMessage(DungeonMewClient.systemText(Text.literal("Removing held item from quick-trash list.")));
                    TrashSet.remove(heldItemID);
                }
                else{
                    client.player.sendMessage(DungeonMewClient.systemText(Text.literal("Adding held item to quick-trash list.")));
                    TrashSet.add(heldItemID);
                }

            }
        });
    }
}
