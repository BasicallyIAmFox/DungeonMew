package dungeonmew.hud;

import ddapi.player.Stats;
import dungeonmew.Constants;
import ddapi.player.ExperienceTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public final class StatusBarRenderer {
    private static final Identifier RESOURCES = new Identifier(Constants.MOD_ID, "textures/gui/resources.png");

    public static void renderManaBar(DrawContext ctx, int x, int y) {
        float manaRatio = Stats.getManaValue() / Stats.getManaMax() * 10f;
        int manaLast = 7 - ((int) Math.floor((manaRatio - Math.floor(manaRatio)) * 8f) % 8);

        for (int i = 0; i < 10; i++) {
            int posX = x - i * 8 - 9;
            ctx.drawTexture(RESOURCES, posX, y, 9, 0, 9, 9);

            int offset = 7; // Always empty
            if (manaRatio >= i && manaRatio < (i + 1)) {
                offset = manaLast;
            }
            else if (manaRatio >= (i + 1)) {
                offset = 0;
            }

            ctx.drawTexture(RESOURCES, posX + offset + 1, y, offset + 1, 0, 7 - offset, 9);
        }
    }

    public static void renderExperienceOrb(DrawContext ctx, int scaledWidth, int scaledHeight) {
        int x = (scaledWidth - 12) / 2;
        int y = scaledHeight - 48 - 3;

        ctx.drawTexture(RESOURCES, x, y, 18, 0, 12, 12);

        float offset;
        if (ExperienceTracker.getCurrentLevel() != null && ExperienceTracker.getNextLevel() != null) {
            float current = ExperienceTracker.getExperience() - ExperienceTracker.getCurrentLevel().getTotalExperience();
            float required = ExperienceTracker.getNextLevel().getTotalExperience() - ExperienceTracker.getCurrentLevel().getTotalExperience();

            offset = current / required;
        } else {
            offset = 1f;
        }

        float innerHeight = 12 * offset;
        float innerUvY = 12 - innerHeight;
        float innerY = y + innerUvY;

        ctx.drawTexture(RESOURCES, x, (int) innerY, 30, (int) innerUvY, 12, (int) innerHeight);
    }
}
