package dungeonmew.feature;

import java.util.Objects;

public final class Modifier {
    private final float base;
    private final float additive;
    private final float multiplicative;
    private final float flat;

    public Modifier(float additive, float multiplicative, float flat, float base)
    {
        this.additive = additive;
        this.multiplicative = multiplicative;
        this.flat = flat;
        this.base = base;
    }

    public Modifier(float additive, float multiplicative) {
        this(additive, multiplicative, 0, 0);
    }

    public Modifier add(float add) {
        return new Modifier(additive + add, multiplicative, flat, base);
    }

    public Modifier sub(float sub) {
        return new Modifier(additive - sub, multiplicative, flat, base);
    }

    public Modifier mul(float mul) {
        return new Modifier(additive, multiplicative * mul, flat, base);
    }

    public Modifier div(float div) {
        return new Modifier(additive, multiplicative / div, flat, base);
    }

    public float applyTo(float baseValue) {
        return (baseValue + base) * additive * multiplicative + flat;
    }

    public Modifier combineWith(Modifier other) {
        return new Modifier(additive + other.additive - 1, multiplicative * other.multiplicative, flat + other.flat, base + other.base);
    }

    public Modifier scale(float scale) {
        return new Modifier(1 + (additive - 1) * scale, 1 + (multiplicative - 1) * scale, flat * scale, base * scale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Modifier modifier = (Modifier) o;
        return Float.compare(modifier.base, base) == 0 && Float.compare(modifier.additive, additive) == 0 && Float.compare(modifier.multiplicative, multiplicative) == 0 && Float.compare(modifier.flat, flat) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, additive, multiplicative, flat);
    }
}
