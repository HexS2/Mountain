package fr.inoxs.mountain;

import org.bukkit.*;

import java.util.HashMap;
import java.util.Iterator;

public class Utils {

    public static HashMap<Integer, Mountain> list = new HashMap<Integer, Mountain>();
    private GlowstoneMountain plugin;

    public Utils(GlowstoneMountain plugin) {
        this.plugin = plugin;
    }

    public int getAvalibleId() {
        int id = 0;
        while (Utils.list.containsKey(Integer.valueOf(id))) {
            id++;
        }
        return id;
    }

    public void saveMountains() {
        if (Utils.list.isEmpty()) {
            return;
        }
        for (Iterator<Integer> i$ = Utils.list.keySet().iterator(); i$.hasNext(); ) {
            int id = ((Integer) i$.next()).intValue();
            Mountain gm = (Mountain) Utils.list.get(Integer.valueOf(id));
            if (gm != null) {
                plugin.getConfig().set("mountain." + id + ".cuboid", cuboidToString(gm.getMountainCuboid()));
                plugin.getConfig().set("mountain." + id + ".world", gm.getWorld());
                plugin.getConfig().set("mountain." + id + ".name", gm.getName());
                plugin.getConfig().set("mountain." + id + ".delay", gm.getSpawnDelay());
                plugin.getConfig().set("mountain." + id + ".type", gm.getType().toString());
                plugin.getConfig().set("mountain." + id + ".material", gm.getMaterial().name());
            }
        }
        plugin.saveConfig();
    }

    public void reloadMountain() {
        if (plugin.getConfig() == null) {
            return;
        }
        Utils.list.clear();
        if (plugin.getConfig().contains("mountain")) {
            for (String id : plugin.getConfig().getConfigurationSection("mountain").getValues(false).keySet()) {
                String path = "mountain." + id;
                Cuboid cuboid = stringToCuoib(plugin.getConfig().getString(path + ".cuboid"));
                String world = plugin.getConfig().getString(path + ".world");
                String name = plugin.getConfig().getString(path + ".name");
                Long spawnDelay = plugin.getConfig().getLong(path + ".delay");
                MountainType type = MountainType.valueOf(plugin.getConfig().getString(path + ".type"));
                Material material = Material.getMaterial(plugin.getConfig().getString(path + ".material"));
                if ((cuboid == null) || (name == null) || (spawnDelay == -1)) {
                    plugin.getConfig().set(path, null);
                    plugin.saveConfig();
                } else {
                    Mountain mountain = new Mountain(cuboid, world, name, spawnDelay, type, material);
                    Utils.list.put(Integer.valueOf(Integer.parseInt(id)),
                            mountain);
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            createBlockCuboid(mountain);
                            Bukkit.broadcastMessage(plugin.getConfiguration().MOUNTAIN_SPAWN_MESSAGE.replace("%name", mountain.getName()).replace("%loc", serializeLocation(mountain.getMountainCuboid().getLocation())).replace("%type", mountain.getType().toString()));
                        }
                    }, 0L, spawnDelay * 20L * 60L);
                }
            }
        }
    }

    public boolean isRegistered(String s) {
        Mountain m = getByName(s);
        if(m != null)
        {
            return true;
        }
        return false;
    }

    public Mountain getByName(String name) {
        Mountain m = null;
        for (Mountain mountain : list.values()) {
            if (mountain.getName().equals(name)) {
                m = mountain;
            }
        }
        return m;
    }

    public String serializeLocation(Location location) {
        String s = null;
        World world = location.getWorld();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        s = "World: " + world.getName() + " X: " + x + " Y: " + y + " Z: " + z;
        return s;
    }

    public boolean startByName(String name) {
        for (Mountain mountain : list.values()) {
            if (mountain.getName().equals(name)) {
                createBlockCuboid(mountain);
                Bukkit.broadcastMessage(plugin.getConfiguration().MOUNTAIN_SPAWN_MESSAGE.replace("%name", mountain.getName()).replace("%loc", serializeLocation(mountain.getMountainCuboid().getLocation())).replace("%type", mountain.getType().toString()));
                return true;
            }
        }

        return false;
    }

    public void createBlockCuboid(Mountain mountain) {
        Cuboid c = mountain.getMountainCuboid();
        int minX = c.minLocation.getBlockX();
        int minY = c.minLocation.getBlockY();
        int minZ = c.minLocation.getBlockZ();

        int maxX = c.maxLocation.getBlockX();
        int maxY = c.maxLocation.getBlockY();
        int maxZ = c.maxLocation.getBlockZ();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location loc = new Location(Bukkit.getWorld(mountain.getWorld()), x, y, z);
                    loc.getBlock().setType(mountain.getMaterial());
                }
            }
        }

    }

    public Cuboid stringToCuoib(String str) {
        Cuboid cube = null;
        String[] a = str.split(",");
        World w = Bukkit.getServer().getWorld(a[0]);
        double maxX = Double.parseDouble(a[1]);
        double maxY = Double.parseDouble(a[2]);
        double maxZ = Double.parseDouble(a[3]);
        double minX = Double.parseDouble(a[4]);
        double minY = Double.parseDouble(a[5]);
        double minZ = Double.parseDouble(a[6]);

        Location maxLocation = new Location(w, maxX, maxY, maxZ);
        Location minLocation = new Location(w, minX, minY, minZ);
        cube = new Cuboid(minLocation, maxLocation);
        return cube;
    }

    public String cuboidToString(Cuboid cuboid) {
        String ret = cuboid.getLocation().getWorld() + "," + cuboid.maxLocation.getX() + "," + cuboid.maxLocation.getY() + "," + cuboid.maxLocation.getZ() + "," + cuboid.minLocation.getX() + "," + cuboid.minLocation.getY() + "," + cuboid.minLocation.getZ();
        return ret;
    }
}
