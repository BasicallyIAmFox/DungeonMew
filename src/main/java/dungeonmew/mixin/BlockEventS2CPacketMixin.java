package dungeonmew.mixin;

import ddapi.player.DungeonTracker;
import dungeonmew.feature.BlessingFinderTracker;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEventS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEventS2CPacket.class)
public abstract class BlockEventS2CPacketMixin {
    @Shadow @Final private int type;

    @Shadow @Final private int data;

    @Shadow @Final private Block block;

    @Shadow @Final private BlockPos pos;

    @Inject(
            method = "apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V",
            at = @At("TAIL")
    )
    private void dungeonmew$apply(ClientPlayPacketListener clientPlayPacketListener, CallbackInfo ci) {
        if (DungeonTracker.inDungeon() && type == 1 && data == 1 && block == Blocks.CHEST) {
            BlessingFinderTracker.mark(pos);
        }
    }
}
