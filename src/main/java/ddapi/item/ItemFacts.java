package ddapi.item;

import net.minecraft.item.ItemStack;

public final class ItemFacts {
    public static int getCustomModelData(ItemStack itemStack) {
        if (itemStack.getNbt() != null && itemStack.getNbt().contains("CustomModelData"))
            return itemStack.getNbt().getInt("CustomModelData");

        return 0;
    }

    public static boolean isValidCustomModelData(ItemStack itemStack) {
        return isValidCustomModelData(getCustomModelData(itemStack));
    }

    public static boolean isValidCustomModelData(int customModelData) {
        if (customModelData >= 1_000_000 && customModelData <= 1_000_042
                || customModelData == 1_900_001
                || customModelData == 1_900_002) {
            return true;
        }
        else if (customModelData >= 3_000_000 && customModelData <= 3_000_071) {
            return true;
        }
        else if (customModelData >= 5_000_000 && customModelData <= 5_000_029
                || customModelData == 5_100_000
                || customModelData >= 5_200_000 && customModelData <= 5_200_009
                || customModelData >= 5_300_000 && customModelData <= 5_300_007
                || customModelData >= 5_400_000 && customModelData <= 5_400_002
                || customModelData >= 5_500_000 && customModelData <= 5_500_005
                || customModelData >= 5_600_000 && customModelData <= 5_600_011
                || customModelData == 56_000_010) {
            return true;
        }
        else if (customModelData >= 4_000_000 && customModelData <= 4_000_001) {
            return true;
        }
        else {
            return false;
        }
    }

    public static int getBaseHealAmount(ItemStack itemStack) {
        return getBaseHealAmount(getCustomModelData(itemStack));
    }

    public static int getBaseHealAmount(int customModelData) {
        return switch (customModelData) {
            // Healing Wand T1
            case 5_000_023 -> 10;
            // Healing Wand T2
            case 5_000_024 -> 25;
            // Healing Wand T3
            case 5_000_025 -> 50;
            // Healing Wand T4
            case 5_000_026 -> 75;
            // Healing Wand T5
            case 5_000_027 -> 100;
            default -> 0;
        };
    }

    public static int getBaseAbilitySpeedAmount(ItemStack itemStack) {
        return getBaseAbilitySpeedAmount(getCustomModelData(itemStack));
    }

    public static int getBaseAbilitySpeedAmount(int customModelData) {
        return switch (customModelData) {
            // Enchanted Sword
            case 1_000_003 -> 10;
            // Magical Sword
            case 1_000_042 -> 30;
            default -> 0;
        };
    }
}
