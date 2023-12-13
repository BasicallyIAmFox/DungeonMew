package dungeonmew.feature.binding;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dungeonmew.feature.QuickTrashConfiguration;

import java.io.IOException;

public class QuickTrashConfigBinding extends Binding<QuickTrashConfiguration>{

    public QuickTrashConfigBinding(QuickTrashConfiguration defaultValue) {
        super(defaultValue);
    }

    @Override
    public void write(JsonWriter writer) {
        try{
            writer.name("version");
            writer.value("1.0.0");
        }catch (IOException e){
            LOGGER.error("Failed to write binding! Stack trace: %s", e);
        }
    }

    @Override
    public void read(JsonReader reader) {

    }
}
