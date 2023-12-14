package dungeonmew.datagen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

@Environment(EnvType.CLIENT)
public class TranslationGenerator extends FabricLanguageProvider {
    public TranslationGenerator(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        generateKeyBinds(translationBuilder);
        generateFeatures(translationBuilder);
    }

    private void generateKeyBinds(TranslationBuilder translationBuilder) {
        translationBuilder.add("category.dungeonmew", "DungeonMew");
        translationBuilder.add("key.dungeonmew.ender_chest", "Ender Chest Shortcut");
        translationBuilder.add("key.dungeonmew.speed_sword", "Quick Speed");
        translationBuilder.add("key.dungeonmew.quick_heal", "Quick Heal");
        translationBuilder.add("key.dungeonmew.trash", "Trash Shortcut");

    }

    private void generateFeatures(TranslationBuilder translationBuilder) {
        translationBuilder.add("dungeonmew.feature.blessing_finder.enabled", "Blessing Finder is §aenabled! §cThis may or may not get you banned!");
        translationBuilder.add("dungeonmew.feature.blessing_finder.disabled", "Blessing Finder is §cdisabled!");
        translationBuilder.add("dungeonmew.feature.blessing_finder_color.set", "Blessing Finder color is set to %s!");
        translationBuilder.add("dungeonmew.feature.blessing_finder_color.this", "this color");

        translationBuilder.add("dungeonmew.feature.configure_brightness.reset", "Brightness has been reset to default value");
        translationBuilder.add("dungeonmew.feature.configure_brightness.additive", "Brightness is increased by %s levels");
        translationBuilder.add("dungeonmew.feature.configure_brightness.override", "Brightness is set to %s levels");

        translationBuilder.add("dungeonmew.feature.cooldown_notifications.ability.ready", "%s can be used again!");
        translationBuilder.add("dungeonmew.feature.cooldown_notifications.class.ready", "%s can be activated again!");
        translationBuilder.add("dungeonmew.feature.cooldown_notifications.enabled", "Cooldown notifications are §aenabled!");
        translationBuilder.add("dungeonmew.feature.cooldown_notifications.disabled", "Cooldown notifications are §cdisabled!");

        translationBuilder.add("dungeonmew.feature.display_armor_bar.enabled", "Armor bar is §adisplayed!");
        translationBuilder.add("dungeonmew.feature.display_armor_bar.disabled", "Armor bar is §ahidden!");

        translationBuilder.add("dungeonmew.feature.display_experience_orb.enabled", "Experience orb is §adisplayed!");
        translationBuilder.add("dungeonmew.feature.display_experience_orb.disabled", "Experience orb is §chidden!");

        translationBuilder.add("dungeonmew.feature.essence_finder.enabled", "Essence Finder is §aenabled! §cThis may or may not get you banned!");
        translationBuilder.add("dungeonmew.feature.essence_finder.disabled", "Essence Finder is §cdisabled!");
        translationBuilder.add("dungeonmew.feature.essence_finder_color.set", "Essence Finder color is set to %s!");
        translationBuilder.add("dungeonmew.feature.essence_finder_color.this", "this color");

        translationBuilder.add("dungeonmew.feature.hide_darkness.enabled", "Darkness and blindness effects are §ashown!");
        translationBuilder.add("dungeonmew.feature.hide_darkness.disabled", "Darkness and blindness effects are §chidden!");

        translationBuilder.add("dungeonmew.feature.show_custom_model_data.enabled", "Items now show their CustomModelData!");
        translationBuilder.add("dungeonmew.feature.show_custom_model_data.disabled", "Items don't show their CustomModelData anymore!");
    }
}
