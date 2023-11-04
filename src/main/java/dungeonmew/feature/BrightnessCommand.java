package dungeonmew.feature;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static dungeonmew.DungeonMewClient.systemText;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public final class BrightnessCommand {
    // usage: brightness (get|set (additive|override) (0..15)|reset)
    public static void register(String name, LiteralCommandNode<FabricClientCommandSource> rootNode) {
        var brightnessNode = literal(name).build();

        var getNode = literal("get")
                .executes(BrightnessCommand::getBrightness)
                .build();

        var setNode = literal("set").build();
        for (var lightType : BrightnessType.values()) {
            var typeNode = literal(lightType.getName()).build();
            var levelNode = argument("level", IntegerArgumentType.integer(0, 15))
                    .executes((ctx) -> setBrightness(ctx, lightType))
                    .build();

            typeNode.addChild(levelNode);
            setNode.addChild(typeNode);
        }

        var resetNode = literal("reset")
                .executes(BrightnessCommand::resetBrightness)
                .build();

        brightnessNode.addChild(getNode);
        brightnessNode.addChild(setNode);
        brightnessNode.addChild(resetNode);

        rootNode.addChild(brightnessNode);
    }

    private static int getBrightness(CommandContext<FabricClientCommandSource> ctx) {
        var systemMessage = systemText(Features.CONFIGURE_BRIGHTNESS.getValue().feedback());
        ctx.getSource().sendFeedback(systemMessage);

        return Command.SINGLE_SUCCESS;
    }

    private static int setBrightness(CommandContext<FabricClientCommandSource> ctx, BrightnessType lightType) {
        int lightLevel = IntegerArgumentType.getInteger(ctx, "level");
        Features.CONFIGURE_BRIGHTNESS.bind(new BrightnessConfiguration(lightLevel, lightType));

        getBrightness(ctx);

        return Command.SINGLE_SUCCESS;
    }

    private static int resetBrightness(CommandContext<FabricClientCommandSource> ctx) {
        Features.CONFIGURE_BRIGHTNESS.reset();

        ctx.getSource().sendFeedback(systemText(Text.translatable("dungeonmew.feature.configure_brightness.reset")));

        return Command.SINGLE_SUCCESS;
    }
}
