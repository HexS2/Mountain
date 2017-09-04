package fr.inoxs.mountain;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
/*
    @Author : CTRL
 */
public class SelectionManager implements Listener {

    private static ConcurrentHashMap<UUID, Selection> selections = new ConcurrentHashMap<>();
    private GlowstoneMountain plugin;

    public SelectionManager(GlowstoneMountain plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() != null && event.getItem().getType().equals(Material.GOLD_AXE)) {
            if (!player.isOp())
                return;

            Selection selection = getSelection(player.getUniqueId());
            if (selection == null)
                selection = new Selection();

            Block block = event.getClickedBlock();
            if (block == null)
                return;

            Location loc = block.getLocation();

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                selection.setLocation2(loc);
                player.sendMessage(ChatColor.GREEN + "Position #2 " + block.getX() +"; "+ block.getY() + "; " + block.getZ());
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                selection.setLocation1(loc);
                player.sendMessage(ChatColor.GREEN + "Position #1 " + block.getX() +"; "+ block.getY() + "; " + block.getZ());
            }

            setSelection(player.getUniqueId(), selection);
            event.setCancelled(true);
        }
    }

    public Selection getSelection(UUID player) {
        return selections.get(player);
    }

    public void setSelection(UUID player, Selection area) {
        selections.put(player, area);
    }
}