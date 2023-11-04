package dungeonmew.feature;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static dungeonmew.DungeonMewClient.systemText;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

@Environment(EnvType.CLIENT)
public final class CooldownNotificationsCommand {
    // usage: cooldownNotifications (get|set (true|false))
    public static void register(String name, LiteralCommandNode<FabricClientCommandSource> rootNode) {
        var cooldownNotificationsNode = literal(name).build();

        var getNode = literal("get")
                .executes(CooldownNotificationsCommand::getState)
                .build();

        var setNode = literal("set").build();
        var valueNode = argument("value", BoolArgumentType.bool())
                .executes(context -> setState(context, BoolArgumentType.getBool(context, "value")))
                .build();

        setNode.addChild(valueNode);

        cooldownNotificationsNode.addChild(getNode);
        cooldownNotificationsNode.addChild(setNode);

        rootNode.addChild(cooldownNotificationsNode);
    }

    private static int getState(CommandContext<FabricClientCommandSource> ctx) {
        sendStateMessage(ctx.getSource(), Features.COOLDOWN_NOTIFICATIONS.getValue());

        return Command.SINGLE_SUCCESS;
    }

    private static int setState(CommandContext<FabricClientCommandSource> ctx, boolean value) {
        Features.COOLDOWN_NOTIFICATIONS.bind(value);
        sendStateMessage(ctx.getSource(), value);

        return Command.SINGLE_SUCCESS;
    }

    private static void sendStateMessage(FabricClientCommandSource source, boolean value) {
        if (value)
            source.sendFeedback(systemText(Text.translatable("dungeonmew.feature.cooldown_notifications.enabled")));
        else
            source.sendFeedback(systemText(Text.translatable("dungeonmew.feature.cooldown_notifications.disabled")));
    }
}
