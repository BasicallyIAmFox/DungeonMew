package dungeonmew.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import dungeonmew.DungeonMewClient;
import ddapi.player.DungeonTracker;
import dungeonmew.feature.BlessingFinderTracker;
import dungeonmew.feature.Features;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntityRenderer.class)
public abstract class ChestBlockEntityRendererMixin<T extends BlockEntity> implements BlockEntityRenderer<T> {
    @Unique
    private static OutlineVertexConsumerProvider outlineVertexConsumerProvider = null;

    @Unique
    private static <T extends BlockEntity> boolean isValidChest(T entity) {
        if (DungeonTracker.inDungeon() && Features.BLESSING_FINDER.getValue().enabled()) {
            return entity.getCachedState().isOf(Blocks.CHEST) && !BlessingFinderTracker.isMarked(entity.getPos());
        }
        else return false;
    }

    @Inject(
            method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At("HEAD")
    )
    private void dungeonmew$isValidChest(T entity,
                                         float tickDelta,
                                         MatrixStack matrices,
                                         VertexConsumerProvider vertexConsumers,
                                         int light,
                                         int overlay,
                                         CallbackInfo ci,
                                         @Share("dungeonmew$validChest") LocalBooleanRef validChest) {
        validChest.set(isValidChest(entity));
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At("HEAD"),
            index = 4,
            argsOnly = true
    )
    private VertexConsumerProvider dungeonmew$modifyVertexConsumer(VertexConsumerProvider value,
                                                                   @Share("dungeonmew$validChest") LocalBooleanRef validChest) {
        if (outlineVertexConsumerProvider == null) {
            outlineVertexConsumerProvider = new OutlineVertexConsumerProvider((VertexConsumerProvider.Immediate) value);
        }

        if (validChest.get()) {
            var color = Features.BLESSING_FINDER.getValue().color();
            outlineVertexConsumerProvider.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            return outlineVertexConsumerProvider;
        }

        return value;
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(value = "STORE"),
            index = 17
    )
    private int dungeonmew$modifyChestRenderLight(int light,
                                                  @Share("dungeonmew$validChest") LocalBooleanRef validChest) {
        if (validChest.get()) {
            return 15728880;
        }

        return light;
    }

    @Inject(
            method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V",
                    shift = At.Shift.BEFORE
            )
    )
    private void dungeonmew$drawVertexConsumer(T entity,
                                               float tickDelta,
                                               MatrixStack matrices,
                                               VertexConsumerProvider vertexConsumers,
                                               int light,
                                               int overlay,
                                               CallbackInfo ci,
                                               @Share("dungeonmew$validChest") LocalBooleanRef validChest) {
        if (validChest.get()) {
            outlineVertexConsumerProvider.draw();
        }
    }
}
