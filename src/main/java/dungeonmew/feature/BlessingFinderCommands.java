package dungeonmew.feature;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.awt.*;

import static dungeonmew.DungeonMewClient.systemText;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public final class BlessingFinderCommands {
    // usage: blessingFinder (get|set (true|false))
    public static void registerBool(LiteralCommandNode<FabricClientCommandSource> rootNode) {
        var mainNode = literal(Features.BLESSING_FINDER.getName()).build();

        var getNode = literal("get")
                .executes(BlessingFinderCommands::getBoolState)
                .build();

        var setNode = literal("set").build();
        var valueNode = argument("value", BoolArgumentType.bool())
                .executes(context -> setBoolState(context, BoolArgumentType.getBool(context, "value")))
                .build();

        setNode.addChild(valueNode);

        mainNode.addChild(getNode);
        mainNode.addChild(setNode);

        rootNode.addChild(mainNode);
    }

    // usage: blessingFinderColor (get|set red green blue <alpha>)
    public static void registerColor(LiteralCommandNode<FabricClientCommandSource> rootNode) {
        var mainNode = literal(Features.BLESSING_FINDER_COLOR.getName()).build();

        var getNode = literal("get")
                .executes(BlessingFinderCommands::getColorState)
                .build();

        var setNode = literal("set").build();
        var redNode = argument("red", IntegerArgumentType.integer(0, 255)).build();
        var greenNode = argument("green", IntegerArgumentType.integer(0, 255)).build();
        var blueNode = argument("blue", IntegerArgumentType.integer(0, 255))
                .executes(context -> {
                    var r = IntegerArgumentType.getInteger(context, "red");
                    var g = IntegerArgumentType.getInteger(context, "green");
                    var b = IntegerArgumentType.getInteger(context, "blue");

                    return setColorState(context, new Color(r, g, b));
                })
                .build();

        var alphaNode = argument("alpha", IntegerArgumentType.integer(0, 255))
                .executes(context -> {
                    var r = IntegerArgumentType.getInteger(context, "red");
                    var g = IntegerArgumentType.getInteger(context, "green");
                    var b = IntegerArgumentType.getInteger(context, "blue");
                    var a = IntegerArgumentType.getInteger(context, "alpha");

                    return setColorState(context, new Color(r, g, b, a));
                })
                .build();

        setNode.addChild(redNode);
        redNode.addChild(greenNode);
        greenNode.addChild(blueNode);
        blueNode.addChild(alphaNode);

        mainNode.addChild(getNode);
        mainNode.addChild(setNode);

        rootNode.addChild(mainNode);
    }

    private static int getBoolState(CommandContext<FabricClientCommandSource> ctx) {
        sendBoolStateMessage(ctx.getSource(), Features.BLESSING_FINDER.getValue().enabled());

        return Command.SINGLE_SUCCESS;
    }

    private static int setBoolState(CommandContext<FabricClientCommandSource> ctx, boolean value) {
        Features.BLESSING_FINDER.bind(new BlessingFinderConfiguration(value, Features.BLESSING_FINDER.getValue().color()));
        sendBoolStateMessage(ctx.getSource(), value);

        return Command.SINGLE_SUCCESS;
    }

    private static void sendBoolStateMessage(FabricClientCommandSource source, boolean value) {
        if (value)
            source.sendFeedback(systemText(Text.translatable("dungeonmew.feature.blessing_finder.enabled")));
        else
            source.sendFeedback(systemText(Text.translatable("dungeonmew.feature.blessing_finder.disabled")));
    }

    private static int getColorState(CommandContext<FabricClientCommandSource> ctx) {
        sendColorStateMessage(ctx.getSource(), Features.BLESSING_FINDER_COLOR.getValue());

        return Command.SINGLE_SUCCESS;
    }

    private static int setColorState(CommandContext<FabricClientCommandSource> ctx, Color value) {
        Features.BLESSING_FINDER_COLOR.bind(value);
        sendColorStateMessage(ctx.getSource(), value);

        return Command.SINGLE_SUCCESS;
    }

    private static void sendColorStateMessage(FabricClientCommandSource source, Color value) {
        source.sendFeedback(systemText(Text.translatable("dungeonmew.feature.blessing_finder_color.set",
                Text.translatable("dungeonmew.feature.blessing_finder_color.this").fillStyle(Style.EMPTY.withColor(value.getRGB()))
                )));
    }
}
