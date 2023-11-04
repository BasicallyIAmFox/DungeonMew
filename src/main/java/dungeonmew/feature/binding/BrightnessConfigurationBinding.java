package dungeonmew.feature.binding;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dungeonmew.feature.BrightnessConfiguration;
import dungeonmew.feature.BrightnessType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.naming.OperationNotSupportedException;

@Environment(EnvType.CLIENT)
public final class BrightnessConfigurationBinding extends Binding<BrightnessConfiguration> {
    public BrightnessConfigurationBinding(BrightnessConfiguration defaultValue) {
        super(defaultValue);
    }

    @Override
    public void write(JsonWriter writer) {
        try {
            writer.name("version");
            writer.value("1.0.0");

            writer.name("level");
            writer.value(getValue().getLevel());
            writer.name("type");
            writer.value(getValue().getType().toString());
        }
        catch (Exception e) {
            LOGGER.error("Failed to write binding! Stack trace: %s", e);
        }
    }

    @Override
    public void read(JsonReader reader) {
        try {
            String versionName = reader.nextName();
            if (versionName.equals("version")) {
                String versionNumber = reader.nextString();

                if (versionNumber.equals("1.0.0")) {
                    reader.nextName();
                    var level = reader.nextInt();

                    reader.nextName();
                    var type = BrightnessType.valueOf(reader.nextString());

                    bind(new BrightnessConfiguration(level, type));
                }
                else {
                    throw new OperationNotSupportedException();
                }
            }
            // Legacy support
            else {
                var level = reader.nextInt();

                reader.nextName();
                var type = BrightnessType.valueOf(reader.nextString());

                bind(new BrightnessConfiguration(level, type));
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to read binding! Stack trace: %s", e);
        }
    }
}
