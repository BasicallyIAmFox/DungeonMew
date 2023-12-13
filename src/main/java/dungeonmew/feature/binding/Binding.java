package dungeonmew.feature.binding;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

/**
 * Bindings for config
 * @param <T>
 */
@Environment(EnvType.CLIENT)
public abstract class Binding<T> {
    protected static final Logger LOGGER = LogUtils.getLogger();

    private final T defaultValue;
    @Nullable
    private T bindValue;

    public Binding(T defaultValue) {
        this.defaultValue = defaultValue;
        bindValue = null;
    }

    public T getValue() {
        return bindValue != null ? bindValue : defaultValue;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    protected T getBindValue() {
        return bindValue;
    }

    public void bind(@Nullable T value) {
        bindValue = value;
    }

    public abstract void write(JsonWriter writer);

    public abstract void read(JsonReader reader);
}
