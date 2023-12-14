package dungeonmew.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Arrays;
import java.util.HashSet;

@Environment(EnvType.CLIENT)
public record QuickTrashConfiguration(HashSet<Integer> idsToRemove) {
    private static final int[] rawTrashDefaults = {
            // Weapons
            1000002, // bone blade
            1000009, // undead archer bow
            1000031, // stringshot
            1000032, // slime orb
            1000034, // spade
            1000035, // stone dagger

            3000053, // skeletal warrier helm
            3000054, // slime boots
            3000055, // 55 - 58 undead knight armor set
            3000056,
            3000057,
            3000058,
            3000059, // undead archer helm
            3000060, // undead soldier chest
            3000061, // undead archer legs
            3000062, // rotten heart
            3000063, // rotten healer chest
            3000064, // bloody healer legs

            5000000, // flesh
            5000001, // bone
            5000002, // stick
            5000003, // slime
            5000004, // rock
            5000006, // string
            5000007, // nugget

            5600001, // zombie pet
            5600005, // skele pet
            5600010, // slime pet
            5600006, // spider pet

    };
    public static HashSet<Integer> getDefault(){
        HashSet<Integer> defaultSet = new HashSet<>();
        Arrays.stream(rawTrashDefaults).forEach(defaultSet::add);
        return defaultSet;
    }
    public HashSet<Integer> getIDs (){ return idsToRemove;}
}
