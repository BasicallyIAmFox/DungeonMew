package ddapi.dungeon;

public enum DungeonType {
    UNKNOWN(-1),
    CASTLE(0),
    ICE(1),
    JUNGLE(2);

    private final int id;

    DungeonType(int id) {
        this.id = id;
    }

    public int identifier() {
        return id;
    }
}
