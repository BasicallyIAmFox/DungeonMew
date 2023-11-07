package dungeonmew.mixin;

import dungeonmew.DungeonMewClient;
import dungeonmew.feature.Features;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "getArmor", at = @At("RETURN"), cancellable = true)
    private void dungeonmew$getArmor(CallbackInfoReturnable<Integer> cir) {
        if (!Features.DISPLAY_ARMOR_BAR.getValue()) {
            cir.setReturnValue(0);
        }
    }

    @Inject(
            method = "hasStatusEffect",
            at = @At("HEAD"),
            cancellable = true)
    private void dungeonmew$hasStatusEffect(StatusEffect effect, CallbackInfoReturnable<Boolean> cir) {
        if (effect == StatusEffects.DARKNESS && Features.HIDE_DARKNESS.getValue()) {
            cir.setReturnValue(false);
        }
    }
}
