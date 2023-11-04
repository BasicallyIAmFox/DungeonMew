package dungeonmew.tracker;

import dungeonmew.DungeonMewClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@Environment(EnvType.CLIENT)
public final class ArrowTracker {
    private static int arrowCount = -1;

    public static int getCount() {
        return arrowCount;
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!DungeonMewClient.isConnectedToDungeonDodge() || client.player == null)
                return;

            var player = client.player;
            if (player.isHolding(Items.BOW)) {
                arrowCount = player.getInventory().count(Items.ARROW);
            } else {
                arrowCount = -1;
            }
        });

        HudRenderCallback.EVENT.register((ctx, tickDelta) -> {
            if (!DungeonMewClient.isConnectedToDungeonDodge())
                return;

            MinecraftClient.getInstance().getProfiler().push("dungeonmew$arrowCounter");

            int scaledWidth = ctx.getScaledWindowWidth();
            int scaledHeight = ctx.getScaledWindowHeight();
            render(ctx, scaledWidth, scaledHeight);

            MinecraftClient.getInstance().getProfiler().pop();
        });
    }

    private static void render(DrawContext ctx, int scaledWidth, int scaledHeight) {
        if (arrowCount == -1) return;

        ItemStack stack = new ItemStack(Items.ARROW, Math.max(arrowCount, 1));
        int x = scaledWidth - 16;
        int y = 0;

        ctx.drawItem(stack, x, y);
        ctx.drawItemInSlot(
                MinecraftClient.getInstance().textRenderer,
                stack, x, y,
                arrowCount == 0 ? "0" : (arrowCount == 1 ? "1" : null)
        );
    }
}
