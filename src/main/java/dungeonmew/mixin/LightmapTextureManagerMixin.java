package dungeonmew.mixin;

import dungeonmew.feature.Features;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {
	@Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
	private static void dungeonmew$overrideBrightness(DimensionType type, int lightLevel, CallbackInfoReturnable<Float> cir) {
		var brightnessConfiguration = Features.CONFIGURE_BRIGHTNESS.getValue();
		if (!brightnessConfiguration.isDefault()) {
			cir.setReturnValue(brightnessConfiguration.modifyLight(cir.getReturnValueF()));
		}
	}
}
