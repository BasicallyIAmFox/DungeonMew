package dungeonmew.feature;

import com.mojang.brigadier.Command;
import ddapi.item.ItemFacts;
import dungeonmew.DungeonMewClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;


@Environment(EnvType.CLIENT)
public final class DebugCommand {
    public static void init() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var debugCommand = literal("dmdebug")
                    .executes(context -> {
                            ClientPlayerEntity player = context.getSource().getPlayer();
                            assert player != null;
                            int i = 0;
                            for (Team team : player.getScoreboard().getTeams()) {
                                DungeonMewClient.LOGGER.debug("Index " + i + ": " + team.getPrefix().getString() + team.getSuffix().getString());
                                i++;
                            }
                            String rawString = Text.of(getTeamPrefixAndSuffix(player.getScoreboard(), (player.getName().getString()))).getString();
                            player.sendMessage(DungeonMewClient.systemText(Text.literal("Held Custom Item ID is: " + ItemFacts.getCustomModelData(player.getMainHandStack()))));
                            String result = rawString.substring(5,rawString.length()-6);
                            DungeonMewClient.LOGGER.debug(result);
                            player.sendMessage(DungeonMewClient.systemText(Text.literal("Player Level is: " + result)));

                        return Command.SINGLE_SUCCESS;
                    })
                    .build();
            dispatcher.getRoot().addChild(debugCommand);
        });
    }

    private static String getTeamPrefixAndSuffix(Scoreboard scoreboard, String playerName) {
        String teamName = null;
        for (Team team : scoreboard.getTeams()) {
            if (team.getPlayerList().contains(playerName)) {
                teamName = team.getPrefix().getString() + team.getSuffix().getString();
                break;
            }
        }
        return (teamName == null ? "" : teamName);
    }
}
