package me.enot.events;

import me.enot.configurations.Settings;
import me.enot.configurations.lang.Langs;
import me.enot.configurations.lang.Language;
import me.enot.gui.PrivatePlayerList;
import me.enot.privat.Builder;
import me.enot.privat.PrivatMember;
import me.enot.privat.WellRegion;
import me.enot.privat.logic.PrivateSetting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EventsInPrivat implements Listener {


    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent e) {
        if (e.getEntityType().equals(EntityType.WITHER) || e.getEntityType().equals(EntityType.WITHER_SKULL)) {
            Block block = e.getBlock();
            if (Settings.getRgsblocks().containsKey(block.getType())) {
                //Bukkit.getConsoleSender().sendMessage("Triggerred " + block.toString() + " " + e.getEntity() + " " + e.getEntityType() + "  " + e.getTo().toString());
                if(Builder.isPrivateBlock(block)) {
                    e.setCancelled(true);
                }
            }
        }
    }


   /*@EventHandler
   public void onBlockExplode(BlockExplodeEvent e){
        //Bukkit.getConsoleSender().sendMessage("BlockExplodeEvent called\n\n");
        HashMap<Material, Integer> privatblocks = Settings.getRgsblocks();
        List<Material> blockedblocks = new ArrayList<>();
        for (Map.Entry<Material, Integer> entry : privatblocks.entrySet()) {
            blockedblocks.add(entry.getKey());
        }
        if (!e.blockList().isEmpty()) {
            for (int i = 0; i < e.blockList().size(); i++) {
                if (blockedblocks.contains(e.blockList().get(i).getType()) && Builder.isPrivateBlock(e.blockList().get(i))) {
                    e.blockList().remove(i);
                } else if (e.blockList().get(i).getState() instanceof CreatureSpawner) {
                    Block b = e.blockList().get(i);
                    if (PrivateSetting.inPrivat(b.getLocation()) != null) {
                        e.blockList().remove(i);
                    }
                }
            }
        }
    }*/

    @EventHandler
    public void onWitherAndTNT(EntityExplodeEvent e) {
        //Bukkit.getConsoleSender().sendMessage("EntityExplode " + e.blockList().toString() + " \n" + e.getEntity() + " " + e.getEntityType());
        HashMap<Material, Integer> privatblocks = Settings.getRgsblocks();
        if (!e.blockList().isEmpty()) {
            for (int i = 0; i < e.blockList().size(); i++) {
                if (privatblocks.containsKey(e.blockList().get(i).getType()) && Builder.isPrivateBlock(e.blockList().get(i))) {
                    e.blockList().remove(i);
                } else if (e.blockList().get(i).getState() instanceof CreatureSpawner) {
                    Block b = e.blockList().get(i);
                    if (PrivateSetting.inPrivat(b.getLocation()) != null) {
                        e.blockList().remove(i);
                    }
                }
            }
        }
        //Bukkit.getConsoleSender().sendMessage("EntityExplode2 " + e.blockList().toString() + " \n" + e.getEntity() + " " + e.getEntityType());
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            Block block = e.getClickedBlock();
            boolean isChest = isChest(block);
            boolean isAnvil = isAnvil(block);
            boolean isBed = isBed(block);
            boolean isDoorOrGate = isDoorOrGate(block);
            boolean isEnchattable = isEnchattable(block);
            boolean isFurnace = isFurnace(block);
            boolean isHopper = isHopper(block);
            boolean isWorkBench = isWorkBench(block);
            boolean isBlockedToPlace = isBlockedToPlace(p.getInventory().getItemInMainHand());

            boolean isPrivatBlock = Settings.getRgsblocks().containsKey(block.getType());

            WellRegion wr = PrivateSetting.inPrivat(block.getLocation());
            if (wr != null) {
                if (!wr.getRgowner().equalsIgnoreCase(p.getName())) {
                    if (!p.hasPermission(Settings.getSettings().getString("permissions.admin"))) {
                        int i = 0;
                        if (isBlockedToPlace) {
                            if (PrivateSetting.isMember(p.getName(), wr) == null) {
                                e.setCancelled(true);
                                p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                            }
                        }
                        List<PrivatMember> pms = wr.getPlayerMembers();
                        if (!pms.isEmpty()) {
                            for (PrivatMember pm : pms) {
                                if (pm.getPlayername().equalsIgnoreCase(p.getName())) {
                                    i++;
                                    if (isChest) {
                                        if (!pm.canChestOpen()) {
                                            e.setCancelled(true);
                                            p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                                        }
                                    } else if (isAnvil) {
                                        if (!pm.canUseAnvil()) {
                                            e.setCancelled(true);
                                            p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                                        }
                                    } else if (isBed) {
                                        if (!pm.canUseBed()) {
                                            e.setCancelled(true);
                                            p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                                        }
                                    } else if (isWorkBench) {
                                        if (!pm.canUseWorkBench()) {
                                            e.setCancelled(true);
                                            p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                                        }
                                    } else if (isDoorOrGate) {
                                        if (!pm.canUseDoors()) {
                                            e.setCancelled(true);
                                            p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                                        }
                                    } else if (isEnchattable) {
                                        if (!pm.canUseCharka()) {
                                            e.setCancelled(true);
                                            p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                                        }
                                    } else if (isFurnace) {
                                        if (!pm.canUsePech()) {
                                            e.setCancelled(true);
                                            p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                                        }
                                    } else {
                                        if (!pm.canUseHopers()) {
                                            e.setCancelled(true);
                                            p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                                        }
                                    }
                                }
                            }
                            if (i == 0) {
                                e.setCancelled(true);
                            }
                        } else {
                            e.setCancelled(true);
                            p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                        }
                    } else if(isPrivatBlock && Builder.isPrivateBlock(block)) {
                            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                PrivatePlayerList.open(wr, p);
                                e.setCancelled(true);
                            }
                        }
                } else if(isPrivatBlock && Builder.isPrivateBlock(block)) {
                    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        PrivatePlayerList.open(wr, p);
                        e.setCancelled(true);
                    }
                }
            }

        } else if (e.getAction() == (Action.PHYSICAL)) {
            Player p = e.getPlayer();
            WellRegion wr = PrivateSetting.inPrivat(p.getLocation());
            if (wr != null) {
                if (!p.hasPermission(Settings.getSettings().getString("permissions.admin"))) {
                    if (!wr.getRgowner().equalsIgnoreCase(p.getName())) {
                        int i = 0;
                        for (PrivatMember pm : wr.getPlayerMembers()) {
                            if (pm.getPlayername().equalsIgnoreCase(p.getName())) {
                                i++;
                                if (!pm.canUsePlity()) {
                                    p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                                    e.setCancelled(true);
                                    return;
                                }
                            }
                        }
                        if (i == 0) {
                            p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onEntity(VehicleDamageEvent e){
        if(e.getAttacker() instanceof Player){
            WellRegion wr = PrivateSetting.inPrivat(e.getVehicle().getLocation());
            if(wr != null){
                Player att = (Player) e.getAttacker();
                if(!wr.getRgowner().equalsIgnoreCase(att.getDisplayName())) {
                    PrivatMember pm = PrivateSetting.isMember(att.getDisplayName(), wr);
                    if (pm == null) {
                        e.setCancelled(true);
                    }
                }
            }

            /*EntityType type = e.getEntityType();
            List<EntityType> entitis = Settings.getBlockedEntitys();
            if(entitis.contains(type)){
                Bukkit.getConsoleSender().sendMessage("Обнаружен моб " + type.name() + " блокирование урона");
                e.setCancelled(true);
            }
             */
        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            EntityType type = e.getEntityType();
            List<EntityType> entitis = Settings.getBlockedEntitys();
            if(entitis.contains(type)){
                WellRegion wr = PrivateSetting.inPrivat(e.getEntity().getLocation());
                if(wr != null) {
                    Player att = (Player) e.getDamager();
                    if (!wr.getRgowner().equalsIgnoreCase(att.getDisplayName())) {
                        PrivatMember pm = PrivateSetting.isMember(att.getDisplayName(), wr);
                        if (pm == null) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }


    private static boolean isChest(Block block){
        return Settings.getChests().contains(block.getType());
    }

    private static boolean isAnvil(Block block){
        return Settings.getAnvil().contains(block.getType());
    }

    private static boolean isEnchattable(Block block){
        return Settings.getEnchs().contains(block.getType());
    }

    private static boolean isDoorOrGate(Block block){
        return Settings.getDoorsandgates().contains(block.getType());
    }

    private static boolean isFurnace(Block block){
        return Settings.getPech().contains(block.getType());
    }

    private static boolean isHopper(Block block){
        return Settings.getHopper().contains(block.getType());
    }

    private static boolean isWorkBench(Block block){
        return Settings.getWorkbench().contains(block.getType());
    }

    private static boolean isBed(Block block){
        return Settings.getBed().contains(block.getType());
    }
    private static boolean isBlockedToPlace(ItemStack stack){
        return Settings.getBlockedtoplace().contains(stack.getType());
    }


}
