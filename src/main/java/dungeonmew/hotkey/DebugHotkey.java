package dungeonmew.hotkey;

import ddapi.item.ItemFacts;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public final class DebugHotkey {
    private static KeyBinding keyBinding;
    static Pattern extractLevel = Pattern.compile("b(\\d+)&"); // get the level from the brackets or something

    public static void init() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.dungeonmew.debug",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                "category.dungeonmew"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
                assert client.player != null;
                System.out.print(Text.of(getTeamPrefixAndSuffix(client.player.getScoreboard(), (client.player.getName().getString()))).getString());
                client.player.sendMessage(Text.literal("Custom Item ID is: " + ItemFacts.getCustomModelData(client.player.getMainHandStack())));

                Matcher matcher = extractLevel.matcher(getTeamPrefixAndSuffix(client.player.getScoreboard(), (client.player.getName().getString())));
                StringBuilder result = new StringBuilder();
                while (matcher.find()){
                    result.append(matcher.group(1)).append("|");
                }

                System.out.println(result);
                client.player.sendMessage(Text.of(getTeamPrefixAndSuffix(client.player.getScoreboard(), (client.player.getName().getString()))));
            }
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
