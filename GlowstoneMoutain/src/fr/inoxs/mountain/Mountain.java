package fr.inoxs.mountain;

import javafx.scene.paint.Material;
import lombok.Getter;

public class Mountain {
   @Getter
    private Cuboid mountainCuboid;
    @Getter
    private String name;
    @Getter
    private long spawnDelay;
    private String world;
    private MountainType type;
    private org.bukkit.Material material;

    public Mountain(Cuboid mountainCuboid, String world, String name, long spawnDelay, MountainType type, org.bukkit.Material material) {
        this.mountainCuboid = mountainCuboid;
        this.name = name;
        this.spawnDelay = spawnDelay;
        this.world = world;
        this.type = type;
        this.material = material;
    }

    public org.bukkit.Material getMaterial() {
        return material;
    }

    public MountainType getType() {
        return type;
    }

    public String getWorld() {
        return world;
    }

    public Cuboid getMountainCuboid() {
        return mountainCuboid;
    }

    public long getSpawnDelay() {
        return spawnDelay;
    }


    public String getName() {
        return name;
    }
}
