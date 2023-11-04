package dungeonmew.feature.binding;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dungeonmew.feature.BlessingFinderConfiguration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.naming.OperationNotSupportedException;
import java.awt.*;

@Environment(EnvType.CLIENT)
public final class BlessingFinderConfigurationBinding extends Binding<BlessingFinderConfiguration> {
    private static final ColorBinding COLOR_BINDING = new ColorBinding(Color.BLACK);

    public BlessingFinderConfigurationBinding(BlessingFinderConfiguration defaultValue) {
        super(defaultValue);
    }

    @Override
    public void write(JsonWriter writer) {
        try {
            writer.name("version");
            writer.value("1.0.0");

            writer.name("enabled");
            writer.value(getValue().enabled());
            writer.name("color");
            writer.beginObject();

            COLOR_BINDING.bind(getValue().color());
            COLOR_BINDING.write(writer);

            writer.endObject();
        }
        catch (Exception e) {
            LOGGER.error("Failed to write binding! Stack trace: %s", e);
        }
    }

    @Override
    public void read(JsonReader reader) {
        try {
            reader.nextName();
            String versionNumber = reader.nextString();

            if (versionNumber.equals("1.0.0")) {
                reader.nextName();
                var enabled = reader.nextBoolean();

                reader.nextName();
                reader.beginObject();

                COLOR_BINDING.read(reader);
                var color = COLOR_BINDING.getValue();

                reader.endObject();

                bind(new BlessingFinderConfiguration(enabled, color));
            }
            else {
                throw new OperationNotSupportedException();
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to read binding! Stack trace: %s", e);
        }
    }
}
