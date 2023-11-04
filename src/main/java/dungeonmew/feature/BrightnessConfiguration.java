package dungeonmew.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.MutableText;

@Environment(EnvType.CLIENT)
public final class BrightnessConfiguration {
    public static final BrightnessConfiguration DEFAULT = new BrightnessConfiguration();

    private final int level;
    private final BrightnessType type;

    public BrightnessConfiguration() {
        this(-1, BrightnessType.ADDITIVE);
    }

    public BrightnessConfiguration(int level, BrightnessType type) {
        this.level = level;
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public BrightnessType getType() {
        return type;
    }

    public boolean isDefault() {
        return getLevel() == -1;
    }

    public float modifyLight(float originalLight) {
        return type.modifyLight(originalLight, level / 15f);
    }

    public MutableText feedback() {
        return type.feedback();
    }
}
