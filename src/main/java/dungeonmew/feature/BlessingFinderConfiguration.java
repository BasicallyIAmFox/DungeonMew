package dungeonmew.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.awt.*;

@Environment(EnvType.CLIENT)
public record BlessingFinderConfiguration(boolean enabled, Color color) {
    public boolean isDisabled() {
        return !enabled;
    }
}
