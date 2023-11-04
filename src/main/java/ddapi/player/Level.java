package ddapi.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum Level {LEVEL_1(1, 0),
    LEVEL_2(2, 100),
    LEVEL_3(3, 200),
    LEVEL_4(4, 300),
    LEVEL_5(5, 400),
    LEVEL_6(6, 500),
    LEVEL_7(7, 750),
    LEVEL_8(8, 1000),
    LEVEL_9(9, 1500),
    LEVEL_10(10, 2500),
    LEVEL_11(11, 5000),
    LEVEL_12(12, 7500),
    LEVEL_13(13, 10000),
    LEVEL_14(14, 12500),
    LEVEL_15(15, 15000),
    LEVEL_16(16, 17500),
    LEVEL_17(17, 20000),
    LEVEL_18(18, 30000),
    LEVEL_19(19, 40000),
    LEVEL_20(20, 50000),
    LEVEL_21(21, 75000),
    LEVEL_22(22, 100000),
    LEVEL_23(23, 150000),
    LEVEL_24(24, 200000),
    LEVEL_25(25, 250000),
    LEVEL_26(26, 300000),
    LEVEL_27(27, 350000),
    LEVEL_28(28, 400000),
    LEVEL_29(29, 450000),
    LEVEL_30(30, 500000),
    LEVEL_31(31, 600000),
    LEVEL_32(32, 700000),
    LEVEL_33(33, 800000),
    LEVEL_34(34, 900000),
    LEVEL_35(35, 1000000),
    LEVEL_36(36, 1100000),
    LEVEL_37(37, 1200000),
    LEVEL_38(38, 1300000),
    LEVEL_39(39, 1400000),
    LEVEL_40(40, 1500000),
    LEVEL_41(41, 1750000),
    LEVEL_42(42, 2000000),
    LEVEL_43(43, 2250000),
    LEVEL_44(44, 2500000),
    LEVEL_45(45, 2750000),
    LEVEL_46(46, 3000000),
    LEVEL_47(47, 3500000),
    LEVEL_48(48, 4000000),
    LEVEL_49(49, 4500000),
    LEVEL_50(50, 5000000),
    COUNT(-1, Float.MAX_VALUE);

    private final int level;
    private final float totalExperience;

    Level(int level, int totalExperience) {
        this(level, (float) totalExperience);
    }

    Level(int level, float totalExperience) {
        this.level = level;
        this.totalExperience = totalExperience;
    }

    public int getLevel() {
        return level;
    }

    public float getTotalExperience() {
        return totalExperience;
    }
}
