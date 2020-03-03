package me.enot.gui.listener;

import me.enot.configurations.lang.Langs;
import me.enot.configurations.lang.Language;
import me.enot.events.BlockBreak;
import me.enot.gui.ConfirmGUI;
import me.enot.gui.PrivateList;
import me.enot.gui.PrivateMemberSetting;
import me.enot.gui.PrivatePlayerList;
import me.enot.privat.PMRG;
import me.enot.privat.PrivatMember;
import me.enot.privat.WellRegion;
import me.enot.privat.logic.PrivateSetting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InventoryEvent implements Listener {

    private static String confirm_title = ConfirmGUI.title;
    private static String privat_list_title = PrivateList.title;
    private static String privat_member_setting = PrivateMemberSetting.title;
    private static String privat_player_list = PrivatePlayerList.title;

    public static HashMap<Player, WellRegion> addlist = new HashMap<>();
    private static HashMap<Player, PMRG> deltelist = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equalsIgnoreCase(confirm_title)) {
            Player p = (Player) e.getWhoClicked();
            if(!getDeltelist().containsKey(p)) {
                HashMap<Player, WellRegion> list = BlockBreak.getConfirm();
                HashMap<Player, ItemStack> blockist = BlockBreak.getConfblock();

                WellRegion wr = list.get(p);
                ItemStack stack = blockist.get(p);
                if (e.getCurrentItem().isSimilar(ConfirmGUI.yes)) {
                    if (PrivateSetting.removeRG(wr)) {
                        list.remove(p);
                        blockist.remove(p);
                        p.sendMessage(Language.get(Langs.rgs__rg_deleted_succesfully)
                                .replace("{NAME}", Language.toString(wr.getPrivatname()))
                                .replace("{ID}", Integer.toString(wr.getId())));
                        p.getInventory().addItem(stack);
                        p.updateInventory();
                        Location location = new Location(p.getWorld(), wr.getPrivatblock().getX(), wr.getPrivatblock().getY(), wr.getPrivatblock().getZ());
                        location.getBlock().setType(Material.AIR);
                    } else {
                        list.remove(p);
                        blockist.remove(p);
                        p.sendMessage(Language.get(Langs.rgs__rg_deleting_error));
                    }
                    p.closeInventory();
                } else if (e.getCurrentItem().isSimilar(ConfirmGUI.no)) {
                    list.remove(p);
                    blockist.remove(p);
                    p.sendMessage(Language.get(Langs.rgs__rg_deleting_no).replace("{NAME}", Language.toString(wr.getPrivatname())));
                    p.closeInventory();
                }
            } else {
                if (e.getCurrentItem().isSimilar(ConfirmGUI.yes)) {
                    PMRG pmrg = getDeltelist().get(p);
                    WellRegion wr = pmrg.getWellRegion();
                    PrivatMember pm = pmrg.getPrivatMember();
                    PrivateSetting.removePM(wr, pm);
                    //Bukkit.getConsoleSender().sendMessage("DELETING\n" + wr.getPrivatname() + "\n" + pm.getPlayername());
                    getDeltelist().remove(p);
                    p.closeInventory();
                    p.sendMessage(Language.get(Langs.main__player_deleted_from_rg)
                            .replace("{PLAYER}", pm.getPlayername())
                            .replace("{PRIVAT-NAME}", Language.toString(wr.getPrivatname()))
                            .replace("{PRIVAT-ID}", Integer.toString(wr.getId()))
                    );
                } else if(e.getCurrentItem().isSimilar(ConfirmGUI.no)) {
                    PMRG pmrg = getDeltelist().get(p);
                    getDeltelist().remove(p);
                    p.sendMessage(Language.get(Langs.rgs__rg_member_no).replace("{NAME}", Language.toString(pmrg.getPrivatMember().getPlayername())));
                    p.closeInventory();
                }
            }

            e.setCancelled(true);
        } else if(e.getInventory().getTitle().equalsIgnoreCase(privat_list_title)){
            Player p = (Player) e.getWhoClicked();
            if(e.getCurrentItem().isSimilar(PrivateList.getClose())){
                p.closeInventory();
            } else {
                HashMap<ItemStack, WellRegion> rgs = PrivateList.getItemslist();
                for (Map.Entry<ItemStack, WellRegion> entry : rgs.entrySet()) {
                    if (e.getCurrentItem().isSimilar(entry.getKey())) {
                        p.closeInventory();
                        PrivatePlayerList.open(entry.getValue(), p);
                    }
                }
            }
            e.setCancelled(true);
        } else if(e.getInventory().getTitle().equalsIgnoreCase(privat_player_list)) {
            Player p = (Player) e.getWhoClicked();
            HashMap<ItemStack, PMRG> list = PrivatePlayerList.getPrivatMemberWellRegionHashMap();
            if(e.getCurrentItem().isSimilar(PrivatePlayerList.getAdd())){
                if(!addlist.containsKey(p)) {
                    addlist.put(p, PrivatePlayerList.adds.get(p));
                    p.closeInventory();
                    p.sendTitle(Language.get(Langs.rgs__privat_player_add_title), Language.get(Langs.rgs__privat_player_add_subtitle), 1, 60, 1);
                } else {
                    p.closeInventory();
                    p.sendTitle(Language.get(Langs.rgs__privat_player_add_title), Language.get(Langs.rgs__privat_player_add_subtitle), 1, 60, 1);
                }
            } else if(e.getCurrentItem().isSimilar(PrivatePlayerList.getNazad())) {
                p.closeInventory();
                PrivateList.open(p);
            } else if(e.getCurrentItem().isSimilar(PrivatePlayerList.getClose())) {
                p.closeInventory();
            } else {
                for (Map.Entry<ItemStack, PMRG> entry : list.entrySet()) {
                    if (entry.getKey().isSimilar(e.getCurrentItem())) {
                        p.closeInventory();
                        PrivateMemberSetting.open(p, entry.getValue());
                    }
                }
            }
            e.setCancelled(true);
        } else if(e.getInventory().getTitle().equalsIgnoreCase(privat_member_setting)){
            Player p = (Player) e.getWhoClicked();
            //HashMap<ItemStack, Integer> list = PrivateMemberSetting.getItemsslots();
            HashMap<ItemStack, PMRG> ls = PrivateMemberSetting.getItemspmrg();
            ItemStack stack = e.getCurrentItem();

            if(!stack.isSimilar(PrivateMemberSetting.getClose())) {
                ItemStack mojna = PrivateMemberSetting.getMojna();
                //Bukkit.getConsoleSender().sendMessage("Menu detected");
                PMRG pmrg = ls.get(stack);
                if (e.getSlot() == PrivateMemberSetting.getBlockbreak()) {
                    if (stack.getData().equals(mojna.getData())) {
                        Bukkit.getConsoleSender().sendMessage("BlockBreak true");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        //Bukkit.getConsoleSender().sendMessage(String.valueOf(pm.canBlockBreak()));
                        pm.setCanBlockBreak(false);
                        //Bukkit.getConsoleSender().sendMessage(String.valueOf(pm.canBlockBreak()));
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    } else {
                        Bukkit.getConsoleSender().sendMessage("BlockBreak false");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanBlockBreak(true);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    }
                    p.closeInventory();
                    PrivateMemberSetting.open(p, pmrg);
                } else if (e.getSlot() == PrivateMemberSetting.getBlockplace()) {
                    if (stack.getData().equals(mojna.getData())) {
                        //Bukkit.getConsoleSender().sendMessage("BlockPlace true");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanBlockPlace(false);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    } else {
                        //Bukkit.getConsoleSender().sendMessage("BlockPlace false");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanBlockPlace(true);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    }
                    p.closeInventory();
                    PrivateMemberSetting.open(p, pmrg);
                } else if (e.getSlot() == PrivateMemberSetting.getChestopen()) {
                    if (stack.getData().equals(mojna.getData())) {
                        //Bukkit.getConsoleSender().sendMessage("ChestOpen true");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanChestOpen(false);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    } else {
                        //Bukkit.getConsoleSender().sendMessage("ChestOpen false");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanChestOpen(true);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    }
                    p.closeInventory();
                    PrivateMemberSetting.open(p, pmrg);
                } else if (e.getSlot() == PrivateMemberSetting.getUseanvil()) {
                    if (stack.getData().equals(mojna.getData())) {
                        //Bukkit.getConsoleSender().sendMessage("Anvil true");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUseAnvil(false);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    } else {
                        //Bukkit.getConsoleSender().sendMessage("Anvil false");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUseAnvil(true);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    }
                    p.closeInventory();
                    PrivateMemberSetting.open(p, pmrg);
                } else if (e.getSlot() == PrivateMemberSetting.getUsebed()) {
                    if (stack.getData().equals(mojna.getData())) {
                        //Bukkit.getConsoleSender().sendMessage("Bed true");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUseBed(false);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    } else {
                        //Bukkit.getConsoleSender().sendMessage("Bed false");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUseBed(true);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    }
                    p.closeInventory();
                    PrivateMemberSetting.open(p, pmrg);
                } else if (e.getSlot() == PrivateMemberSetting.getUsecharka()) {
                    if (stack.getData().equals(mojna.getData())) {
                        //Bukkit.getConsoleSender().sendMessage("Charka true");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUseCharka(false);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    } else {
                        //Bukkit.getConsoleSender().sendMessage("Charka false");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUseCharka(true);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    }
                    p.closeInventory();
                    PrivateMemberSetting.open(p, pmrg);
                } else if (e.getSlot() == PrivateMemberSetting.getUsedoors()) {
                    if (stack.getData().equals(mojna.getData())) {
                        //Bukkit.getConsoleSender().sendMessage("Doors true");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUseDoors(false);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    } else {
                        //Bukkit.getConsoleSender().sendMessage("Doors false");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUseDoors(true);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    }
                    p.closeInventory();
                    PrivateMemberSetting.open(p, pmrg);
                } else if (e.getSlot() == PrivateMemberSetting.getUsehoppers()) {
                    if (stack.getData().equals(mojna.getData())) {
                        //Bukkit.getConsoleSender().sendMessage("Hoppers true");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUseHopers(false);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    } else {
                        //Bukkit.getConsoleSender().sendMessage("Hoppers false");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUseHopers(true);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    }
                    p.closeInventory();
                    PrivateMemberSetting.open(p, pmrg);
                } else if (e.getSlot() == PrivateMemberSetting.getUsepech()) {
                    if (stack.getData().equals(mojna.getData())) {
                        //Bukkit.getConsoleSender().sendMessage("Pech  rue");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUsePech(false);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    } else {
                        //Bukkit.getConsoleSender().sendMessage("Pech false");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUsePech(true);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    }
                    p.closeInventory();
                    PrivateMemberSetting.open(p, pmrg);
                } else if (e.getSlot() == PrivateMemberSetting.getUseplity()) {
                    if (stack.getData().equals(mojna.getData())) {
                        //Bukkit.getConsoleSender().sendMessage("Plity true");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUsePlity(false);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    } else {
                        //Bukkit.getConsoleSender().sendMessage("Plity false");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUsePlity(true);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    }
                    p.closeInventory();
                    PrivateMemberSetting.open(p, pmrg);
                } else if (e.getSlot() == PrivateMemberSetting.getUseworkbench()) {
                    if (stack.getData().equals(mojna.getData())) {
                        //Bukkit.getConsoleSender().sendMessage("WorkBench true");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUseWorkBench(false);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    } else {
                        //Bukkit.getConsoleSender().sendMessage("WorkBench false");
                        PrivatMember pm = ls.get(stack).getPrivatMember();
                        pm.setCanUseWorkBench(true);
                        PrivateSetting.changePerm(ls.get(stack).getWellRegion(), pm);
                    }
                    p.closeInventory();
                    PrivateMemberSetting.open(p, pmrg);
                } else if (e.getSlot() == PrivateMemberSetting.getDelte()) {
                    WellRegion wr = ls.get(stack).getWellRegion();
                    PrivatMember pm = ls.get(stack).getPrivatMember();
                    /*
                    PrivateSetting.removePM(wr, pm);
                    //Bukkit.getConsoleSender().sendMessage("DELETING\n" + wr.getPrivatname() + "\n" + pm.getPlayername());
                    p.closeInventory();
                    p.sendMessage(Language.get(Langs.main__player_deleted_from_rg)
                            .replace("{PLAYER}", pm.getPlayername())
                            .replace("{PRIVAT-NAME}", Language.toString(wr.getPrivatname()))
                            .replace("{PRIVAT-ID}", Integer.toString(wr.getId()))
                    );
                     */
                    getDeltelist().put(p, pmrg);
                    p.closeInventory();
                    ConfirmGUI.open(p, pm);
                } else if (e.getSlot() == PrivateMemberSetting.getNazad()) {
                    WellRegion wr = ls.get(stack).getWellRegion();
                    p.closeInventory();
                    PrivatePlayerList.open(wr, p);
                }
            } else {
                p.closeInventory();
            }
            e.setCancelled(true);
        }
    }

    public static HashMap<Player, PMRG> getDeltelist() {
        return deltelist;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if(e.getInventory().getTitle().equalsIgnoreCase(confirm_title)){
            Player player = (Player)e.getPlayer();
            if(BlockBreak.getConfirm().containsKey(player)){
                ConfirmGUI.open(player, BlockBreak.getConfirm().get(player));
            } else {
                if (getDeltelist().containsKey(player)) {
                    ConfirmGUI.open(player, getDeltelist().get(player).getPrivatMember());
                }
            }
        }
    }

    public static String toStr(WellRegion wr, String s){
        s = s.replace("{PRIVAT-NAME}", Language.toString(wr.getPrivatname()))
                .replace("{PRIVAT-ID}", Integer.toString(wr.getId()))
                .replace("{PRIVAT-OWNER}", wr.getRgowner())
                .replace("{PRIVAT-BLOCK-X}", Integer.toString(wr.getPrivatblock().getX()))
                .replace("{PRIVAT-BLOCK-Y}", Integer.toString(wr.getPrivatblock().getY()))
                .replace("{PRIVAT-BLOCK-Z}", Integer.toString(wr.getPrivatblock().getZ()));
        s = ChatColor.translateAlternateColorCodes('&', s);
        return s;
    }

    public static List<String> toStrList(WellRegion wr, List<String> list){
        for(int i = 0; i < list.size(); i++){
            list.set(i, toStr(wr, list.get(i)));
        }

        return list;
    }
}
