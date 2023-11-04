package dungeonmew.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.logging.LogUtils;
import dungeonmew.feature.Feature;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@Environment(EnvType.CLIENT)
public final class FeatureIO {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "dungeonmew/options.json");

    public static void init() {
        create();
    }

    public static void create() {
        FILE.getParentFile().mkdirs();
        try {
            if (FILE.createNewFile()) {
                save();
            } else {
                load();
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to create options! Stack trace: %s", e);
        }
    }

    public static void save() {
        try (var fileWriter = new FileWriter(FILE);
            var jsonWriter = GSON.newJsonWriter(fileWriter)) {
            jsonWriter.beginObject();
            for (var feature : Feature.getFeatures()) {
                jsonWriter.name(feature.getId());

                jsonWriter.beginObject();
                feature.getBinding().write(jsonWriter);
                jsonWriter.endObject();
            }
            jsonWriter.endObject();
        }
        catch (Exception e) {
            LOGGER.error("Failed to save option file! Stack trace: %s", e);
        }
    }

    public static void load() {
        try (var fileReader = new FileReader(FILE);
            var jsonReader = GSON.newJsonReader(fileReader)) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                var id = jsonReader.nextName();

                jsonReader.beginObject();
                Feature.getFeatureById(id).getBinding().read(jsonReader);
                jsonReader.endObject();
            }
            jsonReader.endObject();
        }
        catch (Exception e) {
            LOGGER.error("Failed to load option file! Stack trace: %s", e);
        }
    }
}
