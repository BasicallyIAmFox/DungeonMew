package dungeonmew.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import ddapi.event.SentMessageEvents;
import dungeonmew.DungeonMewClient;
import dungeonmew.feature.Features;
import dungeonmew.hud.StatusBarRenderer;
import dungeonmew.util.FormattingUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Inject(
            method = "setOverlayMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void dungeonmew$setOverlayMessage(Text message, boolean tinted, CallbackInfo ci) {
        if (client.player == null)
            return;

        String literalMessage = FormattingUtils.removeFormatting(message.getString());

        var value = SentMessageEvents.OVERLAY_ON.invoker().onSentMessage(client, literalMessage);
        if (value == SentMessageEvents.ReturnState.CANCEL) {
            ci.cancel();
        }
    }

    @ModifyVariable(
            method = "renderStatusBars",
            at = @At("HEAD"),
            index = 1,
            argsOnly = true
    )
    private DrawContext dungeonmew$renderStatusBars_holdContextRef(DrawContext ctx, @Share("dungeonmew$ctx") LocalRef<DrawContext> ref) {
        ref.set(ctx);
        return ctx;
    }

    @ModifyVariable(
            method = "renderStatusBars",
            at = @At("STORE"),
            ordinal = 4
    )
    private int dungeonmew$renderStatusBars_holdWidthRef(int width, @Share("dungeonmew$width") LocalIntRef ref) {
        ref.set(width);
        return width;
    }

    @ModifyVariable(
            method = "renderStatusBars",
            at = @At("STORE"),
            ordinal = 5
    )
    private int dungeonmew$renderStatusBars_holdHeightRef(int height, @Share("dungeonmew$height") LocalIntRef ref) {
        ref.set(height);
        return height;
    }

    @ModifyConstant(
            method = "renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V",
            constant = @Constant(intValue = 0, ordinal = 2)
    )
    private int dungeonmew$renderStatusBars(int i,
                                            @Share("dungeonmew$width") LocalIntRef widthRef,
                                            @Share("dungeonmew$height") LocalIntRef heightRef,
                                            @Share("dungeonmew$ctx") LocalRef<DrawContext> ctxRef) {
        client.getProfiler().swap("dungeonmew$manaBar");
        StatusBarRenderer.renderManaBar(ctxRef.get(), widthRef.get(), heightRef.get());

        return Integer.MAX_VALUE;
    }

    @Inject(
            method = "renderExperienceBar",
            at = @At("TAIL")
    )
    private void dungeonmew$renderExpOrb(DrawContext context, int x, CallbackInfo ci) {
        if (Features.DISPLAY_EXPERIENCE_ORB.getValue()) {
            client.getProfiler().push("dungeonmew$experienceOrb");
            StatusBarRenderer.renderExperienceOrb(context, scaledWidth, scaledHeight);
            client.getProfiler().pop();
        }
    }
}
