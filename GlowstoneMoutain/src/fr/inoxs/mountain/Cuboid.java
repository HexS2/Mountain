package fr.inoxs.mountain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
public class Cuboid {

    public int maxY;
    public int minY;
    public Location mid, minLocation, maxLocation;
    private int maxX;
    private int maxZ;
    private int minX;
    private int minZ;
    private Material ignore;

    public Cuboid(Location l, Location l2) {

        if (l.getBlockX() > l2.getBlockX()) {
            maxX = l.getBlockX();
            minX = l2.getBlockX();
        } else {
            maxX = l2.getBlockX();
            minX = l.getBlockX();
        }
        if (l.getBlockY() > l2.getBlockY()) {
            maxY = l.getBlockY();
            minY = l2.getBlockY();
        } else {
            maxY = l2.getBlockY();
            minY = l.getBlockY();
        }
        if (l.getBlockZ() > l2.getBlockZ()) {
            maxZ = l.getBlockZ();
            minZ = l2.getBlockZ();
        } else {
            maxZ = l2.getBlockZ();
            minZ = l.getBlockZ();
        }

        int x1 = Math.max(l.getBlockX(), l2.getBlockX()) + 1;
        int y1 = Math.max(l.getBlockY(), l2.getBlockY()) + 1;
        int z1 = Math.max(l.getBlockZ(), l2.getBlockZ()) + 1;
        mid = new Location(l.getWorld(), minX + (x1 - minX) / 2.0D, minY + (y1 - minY) / 2.0D, minZ + (z1 - minZ) / 2.0D);
        minLocation = new Location(l.getWorld(), minX, minY, minZ);
        maxLocation = new Location(l.getWorld(), maxX, maxY, maxZ);
    }

    public boolean isInCube(Location b) {
        if (((int)b.getX() <= maxX) && ((int)b.getX() >= minX)
                && ((int)b.getY() <= maxY) && ((int)b.getY() >= minY)
                && ((int)b.getZ() <= maxZ) && ((int)b.getZ() >= minZ)) {
            return true;
        }
        return false;
    }

    public void redirect(Entity e) {
        Location loc = e.getLocation();
        //bottom
        if ((loc.getX() > minX && loc.getX() < maxX) && (loc.getZ() > minZ && loc.getZ() > maxZ))
            e.setVelocity(e.getVelocity().add(new Vector(0, 0, -1)));
        //top
        if ((loc.getX() > minX && loc.getX() < maxX) && (loc.getZ() < minZ && loc.getZ() < maxZ))
            e.setVelocity(e.getVelocity().add(new Vector(0, 0, 1)));
        //left
        if ((loc.getZ() > minZ && loc.getZ() < maxZ) && (loc.getX() < minX && loc.getX() < maxX))
            e.setVelocity(e.getVelocity().add(new Vector(1, 0, 0)));
        //right
        if ((loc.getZ() > minZ && loc.getZ() < maxZ) && (loc.getX() > minX && loc.getX() > maxX))
            e.setVelocity(e.getVelocity().add(new Vector(-1, 0, 0)));
    }

    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block b = new Location(Bukkit.getWorlds().get(0), x, y, z).getBlock();
                    if (b != null) {
                        if (b.getType() == ignore) continue;
                        blocks.add(b);
                    }
                }
            }
        }
        return blocks;
    }

    public Location getLocation(){
        Location loc = null;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block b = new Location(Bukkit.getWorlds().get(0), x, y, z).getBlock();
                    if (b != null) {
                        if (b.getType() == ignore) continue;
                        loc = b.getLocation();
                    }
                }
            }
        }
        return  loc;
    }
    public Material getIgnoredBlock() {
        return ignore;
    }

    public void setIgnoredBlock(Material ignore) {
        this.ignore = ignore;
    }

    public static Cuboid createCuboid(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        return new Cuboid(new Location(world, x1, y1, z1), new Location(world, x2, y2, z2));
    }
}
