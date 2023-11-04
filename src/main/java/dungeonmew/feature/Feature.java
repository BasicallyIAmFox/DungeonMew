package dungeonmew.feature;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dungeonmew.feature.binding.Binding;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

@Environment(EnvType.CLIENT)
public abstract class Feature<T> {
    private static final ArrayList<Feature<?>> FEATURES = new ArrayList<>();
    private static final HashMap<String, Feature<?>> FEATURE_BY_ID = new HashMap<>();
    
    public static ArrayList<Feature<?>> getFeatures() {
        return FEATURES;
    }
    
    public static @Nullable Feature<?> getFeatureById(String id) {
        if (FEATURE_BY_ID.containsKey(id)) {
            return FEATURE_BY_ID.get(id);
        }
        return null;
    }

    private final String id;
    private final String name;
    private final Binding<T> binding;

    public Feature(String id, String name, Binding<T> binding) {
        this.id = id;
        this.name = name;
        this.binding = binding;

        FEATURES.add(this);
        FEATURE_BY_ID.put(id, this);
    }

    public void bind(T value) {
        binding.bind(value);
    }

    public void reset() {
        bind(binding.getDefaultValue());
    }

    public T getValue() {
        return binding.getValue();
    }
    public T getDefaultValue() {
        return binding.getDefaultValue();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public @NotNull Binding<T> getBinding() {
        return binding;
    }

    public void init() {
    }

    public void registerAsCommand(LiteralCommandNode<FabricClientCommandSource> rootNode) {
    }

    public Text getText() {
        return Text.translatable(String.format("dungeonmew.feature.%s", id));
    }

    @Override
    public String toString() {
        return name;
    }
}
