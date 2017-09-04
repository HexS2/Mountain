package fr.inoxs.mountain;

import com.mysql.jdbc.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;
import java.util.HashMap;

public class GlowstoneMountain extends JavaPlugin implements CommandExecutor {
    public static HashMap<Integer, Mountain> list = new HashMap<Integer, Mountain>();
    private Utils utils;
    private SelectionManager manager;
    private Configuration configuration;

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        utils = new Utils(this);
        manager = new SelectionManager(this);
        getUtils().reloadMountain();
        configuration = new Configuration(this);
        configuration.setupConfig(getConfig());
        getCommand("mountain").setExecutor(this);
    }

    public Utils getUtils() {
        return utils;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage("§e§m------------------------------");
                if(Utils.list != null) {
                    for (Mountain mountain : Utils.list.values()) {
                        p.sendMessage(getConfiguration().INFO_MESSAGE.replace("%name", mountain.getName()).replace("%loc", getUtils().serializeLocation(mountain.getMountainCuboid().getLocation())).replace("%delay",mountain.getSpawnDelay()+"").replace("%type", mountain.getType().toString()));
                    }
                }
                p.sendMessage("§e§m------------------------------");
            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("reload")) {
                    if (p.isOp()) {
                        reloadConfig();
                        saveConfig();
                        Bukkit.getPluginManager().disablePlugin(this);
                        Bukkit.getPluginManager().enablePlugin(this);
                        p.sendMessage("§7[§aGlowstoneMountain§7] §aReload complete");
                    }
                }
            }
            if(args.length == 2){
                if(args[0].equalsIgnoreCase("forcestart")){
                    if(p.isOp()) {
                        String name = args[1].toLowerCase();
                        if(getUtils().isRegistered(name)) {
                            if (!getUtils().startByName(name)) {
                                p.sendMessage("§cERROR: name unknown");
                            }
                        }else{
                            p.sendMessage("§cERROR: unknown mountain");
                        }
                    }
                }
            }

            if (args.length == 5) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (p.isOp()) {
                        if (manager.getSelection(p.getUniqueId()) != null) {
                            Cuboid c = new Cuboid(manager.getSelection(p.getUniqueId()).getLocation1(), manager.getSelection(p.getUniqueId()).getLocation2());
                            Long spawnDelay = 0L;
                            MountainType type = null;
                            Material material = null;
                            if (!getUtils().isRegistered(args[1].toLowerCase())) {
                                try {
                                    spawnDelay = Long.parseLong(args[2]);
                                    type = MountainType.valueOf(args[3]);
                                    if(Material.matchMaterial(args[4]) != null) {
                                        material = Material.matchMaterial(args[4]);
                                    }else{
                                        p.sendMessage("§cArguments error : /mountain create <name> <delay> <type> <material>");
                                        return true;
                                    }
                                } catch (IllegalArgumentException nfe) {
                                    p.sendMessage("§cArguments error : /mountain create <name> <delay> <type> <material>");
                                    return true;
                                }

                                Mountain mountain = new Mountain(c, p.getWorld().getName(), args[1].toLowerCase(), spawnDelay, type, material);
                                Utils.list.put(Integer.valueOf(getUtils().getAvalibleId()), mountain);
                                getUtils().saveMountains();
                                getUtils().createBlockCuboid(mountain);
                                p.sendMessage("§aThe Glowstone Mountain §6" + args[1].toLowerCase() + " §awas successfully created !");

                            } else {
                                p.sendMessage("§cERROR: this mountain already exist !");
                            }
                        } else {
                            p.sendMessage("§cERROR: please set the glowstone postion !");
                        }
                    }
                }
            }
            }
        return super.onCommand(sender, command, label, args);

    }
}
