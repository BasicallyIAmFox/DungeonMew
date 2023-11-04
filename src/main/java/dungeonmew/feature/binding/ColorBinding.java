package dungeonmew.feature.binding;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dungeonmew.feature.BrightnessConfiguration;
import dungeonmew.feature.BrightnessType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.naming.OperationNotSupportedException;
import java.awt.*;

@Environment(EnvType.CLIENT)
public final class ColorBinding extends Binding<Color> {
    public ColorBinding(Color defaultValue) {
        super(defaultValue);
    }

    @Override
    public void write(JsonWriter writer) {
        try {
            writer.name("version");
            writer.value("1.0.0");

            writer.name("red");
            writer.value(getValue().getRed());
            writer.name("green");
            writer.value(getValue().getGreen());
            writer.name("blue");
            writer.value(getValue().getBlue());
            writer.name("alpha");
            writer.value(getValue().getAlpha());
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
                    var r = reader.nextInt();
                    reader.nextName();
                    var g = reader.nextInt();
                    reader.nextName();
                    var b = reader.nextInt();
                    reader.nextName();
                    var a = reader.nextInt();

                    bind(new Color(r, g, b, a));
                }
                else {
                    throw new OperationNotSupportedException();
                }
            }
            // Legacy support
            else {
                var r = reader.nextInt();
                reader.nextName();
                var g = reader.nextInt();
                reader.nextName();
                var b = reader.nextInt();
                reader.nextName();
                var a = reader.nextInt();

                bind(new Color(r, g, b, a));
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to read binding! Stack trace: %s", e);
        }
    }
}
