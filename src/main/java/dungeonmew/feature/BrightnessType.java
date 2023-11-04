package dungeonmew.feature;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public enum BrightnessType {
    ADDITIVE("additive") {
        @Override
        public float modifyLight(float originalLight, float userDefinedLight) {
            return MathHelper.clamp(originalLight + userDefinedLight, 0f, 1f);
        }
    },
    OVERRIDE("override") {
        @Override
        public float modifyLight(float originalLight, float userDefinedLight) {
            return userDefinedLight;
        }
    };

    private final String id;
    private final String name;

    BrightnessType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    BrightnessType(String name) {
        this(name, name);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract float modifyLight(float originalLight, float userDefinedLight);

    public MutableText feedback() {
        return Text.translatable(String.format("dungeonmew.feature.configure_brightness.%s", getId()), Features.CONFIGURE_BRIGHTNESS.getValue().getLevel());
    }
}
