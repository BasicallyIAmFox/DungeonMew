package dungeonmew.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import ddapi.event.SentMessageEvents;
import dungeonmew.DungeonMewClient;
import dungeonmew.util.FormattingUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Shadow @Final private MinecraftClient client;

    @ModifyVariable(
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private Text dungeonmew$obtainTextRef(Text value) {
        Text replacement = SentMessageEvents.CHAT_MODIFY.invoker().modifySentMessage(client, value);
        if (replacement != null) {
            return replacement;
        }
        return value;
    }

    @Inject(
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void dungeonmew$maybeCancelSendingMessage(Text message,
                                                      MessageSignatureData signature,
                                                      int ticks,
                                                      MessageIndicator indicator,
                                                      boolean refresh,
                                                      CallbackInfo ci,
                                                      @Share("dungeonmew$text") LocalRef<Text> localRef) {
        if (!DungeonMewClient.isConnectedToDungeonDodge() || client.player == null)
            return;

        String literalMessage = FormattingUtils.removeFormatting(message.getString());
        SentMessageEvents.ReturnState value = SentMessageEvents.CHAT_ON.invoker().onSentMessage(client, literalMessage);
        if (value == SentMessageEvents.ReturnState.CANCEL) {
            ci.cancel();
        }
    }
}
