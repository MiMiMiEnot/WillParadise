package me.enot.willairdrop.events;

import me.enot.willairdrop.configs.language.Langs;
import me.enot.willairdrop.configs.language.Language;
import me.enot.willairdrop.configs.language.Replace;
import me.enot.willairdrop.events.utils.Counter;
import me.enot.willairdrop.logic.AirDrop;
import me.enot.willairdrop.logic.utils.AirDropLogick;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DropEvents implements Listener {


    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Block b = e.getBlock();
        if(AirDropLogick.getDropList().containsKey(b)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e){
        e.blockList().removeIf(b -> AirDropLogick.getDropList().containsKey(b));
    }

    @EventHandler
    public void onBlockChanging(EntityChangeBlockEvent e){
        Block b = e.getBlock();
        if(AirDropLogick.getDropList().containsKey(b)){
            e.setCancelled(true);
        }
    }

    private static HashMap<AirDrop, Counter> counterMap = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block b = e.getClickedBlock();
            if(AirDropLogick.getDropList().containsKey(b)){
                e.setCancelled(true);
                AirDrop drop = AirDropLogick.getDropList().get(b);
                if(drop.isOpened()){
                    Inventory inventory = drop.getLoot().getInventory();
                    if(AirDropLogick.getInventoryMap().containsKey(drop)){
                        inventory = AirDropLogick.getInventoryMap().get(drop);
                    }
                    p.openInventory(inventory);
                } else if(getCounterMap().containsKey(drop)) {
                    Counter counter = getCounterMap().get(drop);
                    Language.sendMessage(p, Langs.crate__to_open_message,
                            new Replace("{X}", Integer.toString(counter.getSeconds())));
                } else {
                    Counter counter = new Counter(p, drop);
                    getCounterMap().put(drop, counter);
                    counter.start();
                }
            }
        }
    }

    public static HashMap<AirDrop, Counter> getCounterMap() {
        return counterMap;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getDamager() instanceof Player) {
                for (Map.Entry<AirDrop, Counter> entry : getCounterMap().entrySet()) {
                    if (entry.getValue().getPlayer().getName().equalsIgnoreCase(p.getName())) {
                        entry.getValue().stopWB();
                        getCounterMap().remove(entry.getKey());
                    }
                }
            }
            if(e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE){
                Projectile projectile = (Projectile) e.getDamager();
                if(projectile.getShooter() instanceof Player){
                    for (Map.Entry<AirDrop, Counter> entry : getCounterMap().entrySet()) {
                        if (entry.getValue().getPlayer().getName().equalsIgnoreCase(p.getName())) {
                            entry.getValue().stopWB();
                            getCounterMap().remove(entry.getKey());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        Inventory inv = e.getInventory();
        for(Map.Entry<AirDrop, Inventory> entry : AirDropLogick.getInventoryMap().entrySet()){
            if(entry.getValue().equals(inv)){
                if(isEmpty(inv)){
                    AirDropLogick.getInventoryMap().remove(entry.getKey());
                    entry.getKey().getLocation().getBlock().setType(Material.AIR);
                    entry.getKey().getHologram().delete();
                    AirDropLogick.getDropList().remove(entry.getKey().getLocation().getBlock());
                }
            }
        }
    }

    @EventHandler
    public void onQuick(PlayerQuitEvent e){
        for (Map.Entry<AirDrop, Counter> entry : getCounterMap().entrySet()) {
            if (entry.getValue().getPlayer().getName().equalsIgnoreCase(e.getPlayer().getName())) {
                entry.getValue().stopWB();
                getCounterMap().remove(entry.getKey());
            }
        }
    }

    private static boolean isEmpty(Inventory inv){
        for (ItemStack stack : inv.getContents()){
            if(stack != null) return false;
        }
        return true;
    }
}