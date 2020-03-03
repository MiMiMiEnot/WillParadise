package me.enot.gui;

import me.enot.configurations.Settings;
import me.enot.configurations.lang.Language;
import me.enot.gui.listener.InventoryEvent;
import me.enot.privat.PMRG;
import me.enot.privat.PrivatMember;
import me.enot.privat.WellRegion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.enot.configurations.Settings.getSettings;

public class PrivatePlayerList {
    public static String title = Language.toString(getSettings().getString("gui.privat-player-list.title"));;

    private static int slots = getSettings().getInt("gui.privat-player-list.slots");

    private static Inventory inventory;

    private static HashMap<ItemStack, PMRG> privatMemberWellRegionHashMap = new HashMap<>();

    private static ItemStack add;

    private static ItemStack nazad;

    public static HashMap<Player, WellRegion> adds = new HashMap<>();

    private static ItemStack close;

    public static void open(WellRegion wr, Player player){
        title = replace(title, "{PRIVAT-NAME}", wr.getPrivatname());
        inventory = Bukkit.createInventory(null, slots, title);
        List<ItemStack> list = load(wr);
        add = new ItemStack(getSettings().getEnum(Material.class, "gui.privat-player-list.add-material"));
        String name = Language.toString(getSettings().getString("gui.privat-player-list.add-name"));
        List<String> lore = Language.toStringList(getSettings().getStringList("gui.privat-player-list.add-lore"));
        int slott = getSettings().getInt("gui.privat-player-list.add-slot");

        nazad = new ItemStack(getSettings().getEnum(Material.class, "gui.privat-player-list.nazad-material"));
        String nazadname = Language.toString(getSettings().getString("gui.privat-player-list.nazad-name"));
        List<String> nazadlore = Language.toStringList(getSettings().getStringList("gui.privat-player-list.nazad-lore"));
        int nazadslot = getSettings().getInt("gui.privat-player-list.nazad-slot");

        ItemMeta nazadmeta = nazad.getItemMeta();
        nazadmeta.setDisplayName(nazadname);
        nazadmeta.setLore(nazadlore);
        nazad.setItemMeta(nazadmeta);
        inventory.setItem(nazadslot, nazad);

        ItemMeta meta = add.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        add.setItemMeta(meta);

        adds.put(player, wr);

        inventory.setItem(slott, add);
        if(list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                inventory.setItem(i, list.get(i));
            }
        } else {
            List<Integer> slots = getSettings().getIntList("gui.privat-player-list.privat-doesnt-have-players-slots");
            ItemStack barr = Settings.getHaventPlayer();
            ItemMeta metar = barr.getItemMeta();
            metar.setLore(replaceList(metar.getLore(), "{PRIVAT-NAME}", Language.toString(wr.getPrivatname())));
            barr.setItemMeta(metar);
            for(int i : slots){
                inventory.setItem(i, barr);
            }
        }


        ItemStack empty = new ItemStack(Material.getMaterial(getSettings().getString("gui.privat-player-list.empty-item")), 1, (short)getSettings().getInt("gui.privat-player-list.empty-data"));
        List<String> emptylore = Language.toStringList(getSettings().getStringList("gui.privat-player-list.empty-lore"));
        String emptyname = Language.toString(getSettings().getString("gui.privat-player-list.empty-name"));
        ItemMeta emptymeta = empty.getItemMeta();
        emptymeta.setLore(emptylore);
        emptymeta.setDisplayName(emptyname);
        empty.setItemMeta(emptymeta);

        List<Integer> emptyslots = getSettings().getIntList("gui.privat-player-list.empty-slots");
        for(Integer i : emptyslots){
            inventory.setItem(i, empty);
        }


        close = new ItemStack(Material.getMaterial(getSettings().getString("gui.privat-player-list.close-item")), 1, (short) getSettings().getInt("gui.privat-player-list.close-data"));
        String closename = Language.toString(getSettings().getString("gui.privat-player-list.close-name"));
        List<String> closelore = Language.toStringList(getSettings().getStringList("gui.privat-player-list.close-lore"));
        int closeslot = getSettings().getInt("gui.privat-player-list.close-slot");

        boolean closeactv = getSettings().getBoolean("gui.privat-player-list.close-actived");
        if(closeactv) {
            ItemMeta closemeta = close.getItemMeta();
            closemeta.setDisplayName(closename);
            closemeta.setLore(closelore);
            close.setItemMeta(closemeta);

            inventory.setItem(closeslot, close);
        }
        player.openInventory(inventory);
    }


    public static ItemStack getClose() {
        return close;
    }

    private static List<ItemStack> load(WellRegion wr){
        Material material = getSettings().getEnum(Material.class, "gui.privat-player-list.player-material");

        List<ItemStack> stacks = new ArrayList<>();

        String mojet = Language.toString(getSettings().getString("gui.privat-player-list.mojet"));
        String nemojet = Language.toString(getSettings().getString("gui.privat-player-list.ne-mojet"));

        String canblockbreak = "{PLAYER-CANBLOCKBREAK}";
        String canchestopen = "{PLAYER-CANCHESTOPEN}";
        String canblockplace = "{PLAYER-CANBLOCKPLACE}";
        String canusecharka = "{PLAYER-CANUSECHARKA}";
        String canuseanvil = "{PLAYER-CANUSEANVIL}";
        String canusedoorsandgates = "{PLAYER-CANUSEDOORSANDGATES}";
        String canusepech = "{PLAYER-CANUSEPECH}";
        String canusehoppers = "{PLAYER-CANUSEHOPERS}";
        String canuseworkbench = "{PLAYER-CANUSEWORKBENCH}";
        String canusebed = "{PLAYER-CANUSEBED}";
        String canuseplity = "{PLAYER-CANUSEPLITY}";

        for(PrivatMember pm : wr.getPlayerMembers()){

            String name = replace(InventoryEvent.toStr(wr, getSettings().getString("gui.privat-player-list.player-displayname")), "{PLAYER-NAME}", pm.getPlayername());
            List<String> lore = replaceList(InventoryEvent.toStrList(wr, getSettings().getStringList("gui.privat-player-list.player-lore")), "{PLAYER-NAME}", pm.getPlayername());
            if (pm.canUseHopers()) {
                lore = replaceList(lore, canusehoppers, mojet);
            } else {
                lore = replaceList(lore, canusehoppers, nemojet);
            }
            if (pm.canBlockBreak()) {
                lore = replaceList(lore, canblockbreak, mojet);
            } else {
                lore = replaceList(lore, canblockbreak, nemojet);
            }
            if (pm.canChestOpen()) {
                lore = replaceList(lore, canchestopen, mojet);
            } else {
                lore = replaceList(lore, canchestopen, nemojet);
            }
            if (pm.canBlockPlace()) {
                lore = replaceList(lore, canblockplace, mojet);
            } else {
                lore = replaceList(lore, canblockplace, nemojet);
            }
            if (pm.canUseCharka()) {
                lore = replaceList(lore, canusecharka, mojet);
            } else {
                lore = replaceList(lore, canusecharka, nemojet);
            }
            if (pm.canUseAnvil()) {
                lore = replaceList(lore, canuseanvil, mojet);
            } else {
                lore = replaceList(lore, canuseanvil, nemojet);
            }
            if (pm.canUseDoors()) {
                lore = replaceList(lore, canusedoorsandgates, mojet);
            } else {
                lore = replaceList(lore, canusedoorsandgates, nemojet);
            }
            if (pm.canUsePech()) {
                lore = replaceList(lore, canusepech, mojet);
            } else {
                lore = replaceList(lore, canusepech, nemojet);
            }
            if (pm.canUseWorkBench()) {
                lore = replaceList(lore, canuseworkbench, mojet);
            } else {
                lore = replaceList(lore, canuseworkbench, nemojet);
            }
            if (pm.canUseBed()) {
                lore = replaceList(lore, canusebed, mojet);
            } else {
                lore = replaceList(lore, canusebed, nemojet);
            }
            if (pm.canUsePlity()) {
                lore = replaceList(lore, canuseplity, mojet);
            } else {
                lore = replaceList(lore, canuseplity, nemojet);
            }

            ItemStack stack = new ItemStack(material);
            ItemMeta meta = stack.getItemMeta();
            meta.setLore(lore);
            meta.setDisplayName(name);
            stack.setItemMeta(meta);
            stacks.add(stack);
            privatMemberWellRegionHashMap.put(stack, new PMRG(pm, wr));
        }
        return stacks;
    }

    private static String replace(String what, String s, String to){
        return what.replace(s, to);
    }
    private static List<String> replaceList(List<String> list, String s, String to){
        for(int i = 0; i < list.size(); i++){
            list.set(i, list.get(i).replace(s, to));
        }
        return list;
    }

    public static ItemStack getAdd() {
        return add;
    }

    public static String getTitle() {
        return title;
    }

    public static ItemStack getNazad() {
        return nazad;
    }

    public static HashMap<ItemStack, PMRG> getPrivatMemberWellRegionHashMap() {
        return privatMemberWellRegionHashMap;
    }
}
