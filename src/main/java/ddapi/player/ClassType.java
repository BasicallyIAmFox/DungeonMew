package ddapi.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum ClassType {
    WARRIOR ("Warrior Strike",
            "You powered up your warrior strike! Your next melee hit will deal double damage!",
            15),
    ARCHER  ("Sharp Arrows",
            "Sharp arrows activated! You will deal extra damage for 10 seconds!",
            60),
    MAGE    ("Magical Shield",
            "Activated Magic Shield!",
            30),
    HEALER  ("Revive Teammate",
            "TODO: Figure out the message lmao",
            -1),
    TANK    ("Sponge",
            "Damage sponge ability activated!",
            30);

    private final String classAbility;
    private final String classMessage;
    private final int classCooldown;

    ClassType(String classAbility, String classMessage, int classCooldown) {
        this.classAbility = classAbility;
        this.classMessage = classMessage;
        this.classCooldown = classCooldown;
    }

    public String getClassAbility() {
        return classAbility;
    }

    public String getClassMessage() {
        return classMessage;
    }

    public int getClassCooldown() {
        return classCooldown;
    }
}
