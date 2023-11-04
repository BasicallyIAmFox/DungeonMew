package dungeonmew.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import dungeonmew.DungeonMewClient;
import dungeonmew.feature.BlessingFinderTracker;
import dungeonmew.feature.Features;
import ddapi.player.DungeonTracker;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Objects;

@Mixin(SkullBlockEntityRenderer.class)
public abstract class SkullBlockEntityRendererMixin {
    @Unique
    private static OutlineVertexConsumerProvider outlineVertexConsumerProvider = null;

    @Unique
    private static final String essenceId = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjgwZDQ0Y2ExNWUzMDNhMTcxNGQ4ZDY4OGJjM2QwYzQ4NDhhZjQ4YmJlMTZiMzg4OTNlNjQyOThkZGNmZTEwZSJ9fX0=";

    @Unique
    private static boolean tryMatchTexture(SkullBlockEntity state, String texture) {
        Block block = state.getCachedState().getBlock();
        if (block == Blocks.PLAYER_HEAD || block == Blocks.PLAYER_WALL_HEAD) {
            var owner = state.getOwner();
            if (owner == null || !owner.getProperties().containsKey("textures")) {
                return false;
            }

            var find = owner.getProperties().get("textures").stream().findFirst();
            return find.isPresent() && Objects.equals(find.get().getValue(), texture);
        }
        return false;
    }

    @Unique
    private static boolean isValidSkull(SkullBlockEntity state) {
        if (DungeonMewClient.isConnectedToDungeonDodge() && DungeonTracker.inDungeon() && Features.ESSENCE_FINDER.getValue().enabled()) {
            return !BlessingFinderTracker.isMarked(state.getPos()) && tryMatchTexture(state, essenceId);
        }
        else return false;
    }

    @Unique
    @Inject(
            method = "render(Lnet/minecraft/block/entity/SkullBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At("HEAD")
    )
    private void modifyRenderSkull(SkullBlockEntity skullBlockEntity,
                                   float f,
                                   MatrixStack matrixStack,
                                   VertexConsumerProvider vertexConsumerProvider,
                                   int i,
                                   int j,
                                   CallbackInfo ci,
                                   @Share("dungeonmew$validSkull") LocalBooleanRef validSkull) {
        validSkull.set(isValidSkull(skullBlockEntity));
    }

    @Unique
    @ModifyArgs(
            method = "render(Lnet/minecraft/block/entity/SkullBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/entity/SkullBlockEntityRenderer;renderSkull(Lnet/minecraft/util/math/Direction;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/block/entity/SkullBlockEntityModel;Lnet/minecraft/client/render/RenderLayer;)V"
            )
    )
    private void modifyRenderSkull_RenderSkull(Args args, @Share("dungeonmew$validSkull") LocalBooleanRef validSkull) {
        if (outlineVertexConsumerProvider == null) {
            outlineVertexConsumerProvider = new OutlineVertexConsumerProvider(
                    (VertexConsumerProvider.Immediate) args.<VertexConsumerProvider>get(4)
            );
        }

        if (validSkull.get()) {
            var color = Features.ESSENCE_FINDER_COLOR.getValue();
            outlineVertexConsumerProvider.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

            args.set(4, (VertexConsumerProvider)outlineVertexConsumerProvider);
            args.set(5, 15728880);
        }
    }

    @Inject(
            method = "renderSkull",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/entity/SkullBlockEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void modifyRenderSkull_DrawOutline(Direction direction,
                                                      float yaw,
                                                      float animationProgress,
                                                      MatrixStack matrices,
                                                      VertexConsumerProvider vertexConsumers,
                                                      int light,
                                                      SkullBlockEntityModel model,
                                                      RenderLayer renderLayer,
                                                      CallbackInfo ci) {
        if (outlineVertexConsumerProvider != null) {
            outlineVertexConsumerProvider.draw();
        }
    }
}
