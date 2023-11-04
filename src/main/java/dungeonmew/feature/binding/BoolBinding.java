package dungeonmew.feature.binding;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.naming.OperationNotSupportedException;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public final class BoolBinding extends Binding<Boolean> {
    public BoolBinding(Boolean defaultValue) {
        super(defaultValue);
    }

    @Override
    public void write(JsonWriter writer) {
        try {
            writer.name("version");
            writer.value("1.0.0");

            writer.name("value");
            writer.value(getValue());
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
                    bind(reader.nextBoolean());
                }
                else {
                    throw new OperationNotSupportedException();
                }
            }
            // Legacy support
            else {
                // Reads just a value
                bind(reader.nextBoolean());
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to read binding! Stack trace: %s", e);
        }
    }
}
