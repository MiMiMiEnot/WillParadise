package me.enot.gui;

import me.enot.configurations.Settings;
import me.enot.configurations.lang.Language;
import me.enot.gui.listener.InventoryEvent;
import me.enot.privat.Builder;
import me.enot.privat.WellRegion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

import static me.enot.configurations.Settings.getSettings;

public class PrivateList {

    public static String title = Language.toString(Settings.getSettings().getString("gui.privat-list.title"));

    private static int slots = Settings.getSettings().getInt("gui.privat-list.slots");

    private static Inventory inventory;

    private static HashMap<ItemStack, WellRegion> itemslist = new HashMap<>();

    private static ItemStack close;

    public static void open(Player player){
        inventory = Bukkit.createInventory(null, slots, title);
        List<WellRegion> list = Builder.getPlayerPrivats(player.getName());
        List<WellRegion> memberlist = Builder.getPlayerPrivatsWhereMember(player.getName());
        if(list.size() > 0 || memberlist.size() > 0){
            int i = 0;
            for(WellRegion wr : list){
                ItemStack itemStack = loadRgBlock(wr);
                inventory.setItem(i, itemStack);
                i++;
            }
            for(WellRegion wr : memberlist){
                ItemStack itemStack = loadRgBlockMember(wr);
                inventory.setItem(i, itemStack);
            }
        } else {
            List<Integer> slots = Settings.getSettings().getIntList("gui.privat-list.private-block-if-havent-privats-slot");
            for(int i : slots){
                inventory.setItem(i, loadBarrier());
            }
        }
        ItemStack empty = new ItemStack(Material.getMaterial(getSettings().getString("gui.privat-list.empty-item")), 1, (short)getSettings().getInt("gui.privat-list.empty-data"));
        List<String> emptylore = Language.toStringList(getSettings().getStringList("gui.privat-list.empty-lore"));
        String emptyname = Language.toString(getSettings().getString("gui.privat-list.empty-name"));
        ItemMeta emptymeta = empty.getItemMeta();
        emptymeta.setLore(emptylore);
        emptymeta.setDisplayName(emptyname);
        empty.setItemMeta(emptymeta);

        List<Integer> emptyslots = getSettings().getIntList("gui.privat-list.empty-slots");
        for(Integer i : emptyslots){
            inventory.setItem(i, empty);
        }

        close = new ItemStack(Material.getMaterial(getSettings().getString("gui.privat-list.close-item")), 1, (short) getSettings().getInt("gui.privat-player-list.close-data"));
        String closename = Language.toString(getSettings().getString("gui.privat-list.close-name"));
        List<String> closelore = Language.toStringList(getSettings().getStringList("gui.privat-list.close-lore"));
        int closeslot = getSettings().getInt("gui.privat-list.close-slot");

        boolean closeactv = getSettings().getBoolean("gui.privat-list.close-actived");
        if(closeactv) {
            ItemMeta closemeta = close.getItemMeta();
            closemeta.setDisplayName(closename);
            closemeta.setLore(closelore);
            close.setItemMeta(closemeta);

            inventory.setItem(closeslot, close);
        }

        player.openInventory(inventory);
    }
    public static void open(String player, Player p2){
        inventory = Bukkit.createInventory(null, slots, title);
        List<WellRegion> list = Builder.getPlayerPrivats(player);
        List<WellRegion> memberlist = Builder.getPlayerPrivatsWhereMember(player);
        if(list.size() > 0 || memberlist.size() > 0){
            int i = 0;
            for(WellRegion wr : list){
                ItemStack itemStack = loadRgBlock(wr);
                inventory.setItem(i, itemStack);
                i++;
            }
            for(WellRegion wr : memberlist){
                ItemStack itemStack = loadRgBlockMember(wr);
                inventory.setItem(i, itemStack);
            }
        } else {
            List<Integer> slots = Settings.getSettings().getIntList("gui.privat-list.private-block-if-havent-privats-slot");
            for(int i : slots){
                inventory.setItem(i, loadBarrier());
            }
        }

        ItemStack empty = new ItemStack(Material.getMaterial(getSettings().getString("gui.privat-list.empty-item")), 1, (short)getSettings().getInt("gui.privat-list.empty-data"));
        List<String> emptylore = Language.toStringList(getSettings().getStringList("gui.privat-list.empty-lore"));
        String emptyname = Language.toString(getSettings().getString("gui.privat-list.empty-name"));
        ItemMeta emptymeta = empty.getItemMeta();
        emptymeta.setLore(emptylore);
        emptymeta.setDisplayName(emptyname);
        empty.setItemMeta(emptymeta);

        List<Integer> emptyslots = getSettings().getIntList("gui.privat-list.empty-slots");
        for(Integer i : emptyslots){
            inventory.setItem(i, empty);
        }

        close = new ItemStack(Material.getMaterial(getSettings().getString("gui.privat-list.close-item")), 1, (short) getSettings().getInt("gui.privat-player-list.close-data"));
        String closename = Language.toString(getSettings().getString("gui.privat-list.close-name"));
        List<String> closelore = Language.toStringList(getSettings().getStringList("gui.privat-list.close-lore"));
        int closeslot = getSettings().getInt("gui.privat-list.close-slot");

        boolean closeactv = getSettings().getBoolean("gui.privat-list.close-actived");
        if(closeactv) {
            ItemMeta closemeta = close.getItemMeta();
            closemeta.setDisplayName(closename);
            closemeta.setLore(closelore);
            close.setItemMeta(closemeta);

            inventory.setItem(closeslot, close);
        }

        p2.openInventory(inventory);
    }

    public static ItemStack getClose() {
        return close;
    }

    private static ItemStack loadRgBlock(WellRegion wr){
        Material material = Settings.getSettings().getEnum(Material.class, "gui.privat-list.privat-block");
        ItemStack stack = new ItemStack(material);

        String name = InventoryEvent.toStr(wr, Settings.getSettings().getString("gui.privat-list.private-block-name"));
        List<String> lore = InventoryEvent.toStrList(wr, Settings.getSettings().getStringList("gui.privat-list.private-block-lore-if-owner"));

        ItemMeta meta = stack.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        itemslist.put(stack, wr);
        return stack;
    }
    private static ItemStack loadRgBlockMember(WellRegion wr){
        Material material = Settings.getSettings().getEnum(Material.class, "gui.privat-list.privat-block");
        ItemStack stack = new ItemStack(material);

        String name = InventoryEvent.toStr(wr, Settings.getSettings().getString("gui.privat-list.private-block-name"));
        List<String> lore = InventoryEvent.toStrList(wr, Settings.getSettings().getStringList("gui.privat-list.privat-block-lore-if-member"));

        ItemMeta meta = stack.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        itemslist.put(stack, wr);
        return stack;
    }

    private static ItemStack loadBarrier(){
        return Settings.getHavent();
    }

    public static String getTitle() {
        return title;
    }

    public static HashMap<ItemStack, WellRegion> getItemslist() {
        return itemslist;
    }
}
