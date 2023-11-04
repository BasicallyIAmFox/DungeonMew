package dungeonmew.feature;

import ddapi.event.SentMessageEvents;
import dungeonmew.DungeonMewClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dungeonmew.DungeonMewClient.systemText;

public final class CooldownTracker {
    private static final HashMap<String, Integer> abilitiesInCooldown = new HashMap<>();
    private static final ArrayList<String> abilitiesToRemove = new ArrayList<>();

    private static final Pattern ITEM_ABILITY_RIGHT_CLICK_REGEX = Pattern.compile(" Item Ability: ([\\w ]*) \\(RIGHT-CLICK\\)");
    private static final Pattern USED_ABILITY_REGEX = Pattern.compile("Used ([\\w ]*)!");
    private static final Pattern ITEM_COOLDOWN_REGEX = Pattern.compile("Cooldown: (\\d*) (\\w*)");

    public static void init() {
        SentMessageEvents.CHAT_ON.register((client, message) -> {
            var matcher = USED_ABILITY_REGEX.matcher(message);
            if (client.player != null && matcher.find()) {
                tryAddAbilityToList(client.player, matcher);
            }

            return SentMessageEvents.ReturnState.DEFAULT;
        });

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!DungeonMewClient.isConnectedToDungeonDodge() || client.player == null)
                return;

            for (var ability : abilitiesInCooldown.entrySet()) {
                if (ability.getValue() <= 0) {
                    abilitiesToRemove.add(ability.getKey());
                    continue;
                }

                ability.setValue(ability.getValue() - 1);
            }

            for (var ability : abilitiesToRemove) {
                abilitiesInCooldown.remove(ability);

                if (!Features.COOLDOWN_NOTIFICATIONS.getValue()) continue;
                client.player.sendMessage(systemText(Text.translatable("dungeonmew.feature.cooldown_notifications.ability.ready", ability)));
            }
            abilitiesToRemove.clear();
        });
    }

    public static void addAbilityWithCooldown(String abilityName, int cooldownInTicks) {
        if (cooldownInTicks < 0)
            return;

        abilitiesInCooldown.put(abilityName, cooldownInTicks);
    }

    private static void tryAddAbilityToList(PlayerEntity player, Matcher matcher) {
        var abilityNameFromMsg = matcher.group(1);
        var itemsToCheck = new ArrayList<ItemStack>(5);
        itemsToCheck.add(player.getMainHandStack());
        for (var i : player.getArmorItems()) {
            itemsToCheck.add(i);
        }

        for (var item : itemsToCheck) {
            if (item.isEmpty())
                return;

            List<Text> tooltips = item.getTooltip(player, TooltipContext.Default.BASIC);
            String abilityName = null;
            int cooldownValue = -1;
            int cooldownIndex = -1;
            int abilityIndex = -1;

            for (int i = 0; i < tooltips.size(); i++) {
                Text text = tooltips.get(i);
                String content = text.getString();

                matcher = ITEM_ABILITY_RIGHT_CLICK_REGEX.matcher(content);
                if (abilityIndex == -1 && matcher.find()) {
                    abilityIndex = i;
                    abilityName = matcher.group(1);
                }

                matcher = ITEM_COOLDOWN_REGEX.matcher(content);
                if (cooldownIndex == -1 && matcher.find()) {
                    cooldownIndex = i;

                    cooldownValue = parseTime(Integer.parseInt(matcher.group(1)), matcher.group(2));
                }

                if (abilityIndex != -1 && cooldownIndex != -1) {
                    break;
                }
            }

            if (abilityIndex == -1 || cooldownIndex == -1
                    || abilityName == null || cooldownValue == -1
                    || !abilityName.equals(abilityNameFromMsg)) {
                continue;
            }

            addAbilityWithCooldown(abilityName, cooldownValue);
            break;
        }
    }

    private static int parseTime(int value, String age) {
        if (Objects.equals(age, "second") || Objects.equals(age, "seconds")) {
            return value * 20;
        }
        else if (Objects.equals(age, "minute") || Objects.equals(age, "minutes")) {
            return value * 20 * 60;
        }

        return -1;
    }
}
