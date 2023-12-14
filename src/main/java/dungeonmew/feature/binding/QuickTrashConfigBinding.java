package dungeonmew.feature.binding;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dungeonmew.feature.QuickTrashConfiguration;

import javax.naming.OperationNotSupportedException;
import java.awt.*;
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
            writer.name("DeleteIDs");
            writer.beginArray();
            for(int i : getValue().getIDs().stream().toList()){
                writer.value(i);
            }
            writer.endArray();

        }catch (IOException e){
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
                    String deleteIDName = reader.nextName();
                    if (deleteIDName.equals("DeleteIDs")) {
                        reader.beginArray();
                        while (reader.hasNext()) {
                            int id = reader.nextInt();
                            System.out.println("Adding item id " + id);
                            getValue().getIDs().add(id);
                        }
                        reader.endArray();
                    }
                } else {
                    throw new OperationNotSupportedException();
                }
            }
        }catch (Exception e){
            LOGGER.error("Failed to read binding! Stack trace: %s", e);
        }
    }
}
