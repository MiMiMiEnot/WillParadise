package me.enot.gui;

import me.enot.configurations.Settings;
import me.enot.configurations.lang.Language;
import me.enot.privat.PMRG;
import me.enot.privat.PrivatMember;
import me.enot.privat.WellRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.enot.configurations.Settings.getSettings;

public class PrivateMemberSetting {

    public static String title = Language.toString(Settings.getSettings().getString("gui.privat-member.title"));;

    private static int slots = Settings.getSettings().getInt("gui.privat-member.slots");

    private static Inventory inventory;

    private static HashMap<ItemStack, Integer> itemsslots = new HashMap<>();
    private static HashMap<ItemStack, PMRG> itemspmrg = new HashMap<>();


    private static ItemStack delte;
    private static ItemStack nazad;
    private static int nazadslot;
    private static int delteslot;
    private static ItemStack close;
    public static void open(Player p, PMRG pmrg){
        inventory = Bukkit.createInventory(null, slots, title);
        for(Map.Entry<ItemStack, Integer> entry : loaditems(pmrg).entrySet()){
            itemsslots.put(entry.getKey(), entry.getValue());
        }
        delte = new ItemStack(Settings.getSettings().getEnum(Material.class, "gui.privat-member.delete-material"), 1, (short) Settings.getSettings().getInt("gui.privat-member.delete-data"));
        String title = toStr(pmrg.getWellRegion(), Settings.getSettings().getString("gui.privat-member.delete-name"), pmrg.getPrivatMember().getPlayername());
        List<String> lore = toStrList(pmrg.getWellRegion(), Settings.getSettings().getStringList("gui.privat-member.delete-lore"), pmrg.getPrivatMember().getPlayername());
        delteslot = Settings.getSettings().getInt("gui.privat-member.delete-slot");
        ItemMeta meta = delte.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(title);
        delte.setItemMeta(meta);
        inventory.setItem(delteslot, delte);
        itemspmrg.put(delte, pmrg);

        nazad = new ItemStack(Settings.getSettings().getEnum(Material.class, "gui.privat-member.nazad-material"));
        String nazadname = Language.toString(Settings.getSettings().getString("gui.privat-member.nazad-name"));
        List<String> nazadlore = Language.toStringList(Settings.getSettings().getStringList("gui.privat-member.nazad-lore"));
        nazadslot = Settings.getSettings().getInt("gui.privat-member.nazad-slot");

        ItemMeta nazadmeta = nazad.getItemMeta();
        nazadmeta.setDisplayName(nazadname);
        nazadmeta.setLore(nazadlore);
        nazad.setItemMeta(nazadmeta);
        inventory.setItem(nazadslot, nazad);

        itemspmrg.put(nazad, pmrg);


        ItemStack empty = new ItemStack(Material.getMaterial(getSettings().getString("gui.privat-member.empty-item")), 1, (short)getSettings().getInt("gui.privat-member.empty-data"));
        List<String> emptylore = Language.toStringList(getSettings().getStringList("gui.privat-member.empty-lore"));
        String emptyname = Language.toString(getSettings().getString("gui.privat-member.empty-name"));
        ItemMeta emptymeta = empty.getItemMeta();
        emptymeta.setLore(emptylore);
        emptymeta.setDisplayName(emptyname);
        empty.setItemMeta(emptymeta);

        List<Integer> emptyslots = getSettings().getIntList("gui.privat-member.empty-slots");
        for(Integer i : emptyslots){
            inventory.setItem(i, empty);
        }

        close = new ItemStack(Material.getMaterial(getSettings().getString("gui.privat-member.close-item")), 1, (short) getSettings().getInt("gui.privat-player-list.close-data"));
        String closename = Language.toString(getSettings().getString("gui.privat-member.close-name"));
        List<String> closelore = Language.toStringList(getSettings().getStringList("gui.privat-member.close-lore"));
        int closeslot = getSettings().getInt("gui.privat-member.close-slot");

        boolean closeactv = getSettings().getBoolean("gui.privat-member.close-actived");
        if(closeactv) {
            ItemMeta closemeta = close.getItemMeta();
            closemeta.setDisplayName(closename);
            closemeta.setLore(closelore);
            close.setItemMeta(closemeta);

            inventory.setItem(closeslot, close);
        }

        p.openInventory(inventory);
    }

    public static ItemStack getClose() {
        return close;
    }

    public static int getNazad() {
        return nazadslot;
    }

    private static ItemStack blockbreak;
    private static ItemStack blockplace;
    private static ItemStack chestopen;
    private static ItemStack usecharka;
    private static ItemStack useanvil;
    private static ItemStack usedoors;
    private static ItemStack usepech;
    private static ItemStack useworkbench;
    private static ItemStack usebed;
    private static ItemStack useplity;
    private static ItemStack usehoppers;

    public static Material mmojna;
    public static Material mnemojna;
    private static ItemStack mojna;
    private static ItemStack nemojna;

    private static int canblockbreakslot = Settings.getSettings().getInt("gui.privat-member.canblockbreak-slot");;
    private static int canchestopenslot = Settings.getSettings().getInt("gui.privat-member.canchestopen-slot");;
    private static int canblockplaceslot = Settings.getSettings().getInt("gui.privat-member.canblockplace-slot");;
    private static int canusecharkaslot = Settings.getSettings().getInt("gui.privat-member.canusecharka-slot");;
    private static int canuseanvilslot = Settings.getSettings().getInt("gui.privat-member.canuseanvil-slot");;
    private static int canusedoorsandgatesslot = Settings.getSettings().getInt("gui.privat-member.canusedoorsandgates-slot");;
    private static int canusepechslot = Settings.getSettings().getInt("gui.privat-member.canusepech-slot");;
    private static int canuseworkbenchslot = Settings.getSettings().getInt("gui.privat-member.canuseworkbench-slot");;
    private static int canusebedslot = Settings.getSettings().getInt("gui.privat-member.canusebed-slot");;
    private static int canuseplityslot = Settings.getSettings().getInt("gui.privat-member.canuseplity-slot");;
    private static int canusehoppersslot = Settings.getSettings().getInt("gui.privat-member.canusehoppers-slot");;

    private static HashMap<ItemStack, Integer> loaditems(PMRG pmrg){
     PrivatMember pm = pmrg.getPrivatMember();
     HashMap<ItemStack, Integer> list = new HashMap<>();

     String canblockbreak = toString(Settings.getSettings().getString("gui.privat-member.canblockbreak"));

     String canchestopen = toString(Settings.getSettings().getString("gui.privat-member.canchestopen"));

     String canblockplace = toString(Settings.getSettings().getString("gui.privat-member.canblockplace"));

     String canusecharka = toString(Settings.getSettings().getString("gui.privat-member.canusecharka"));

     String canuseanvil = toString(Settings.getSettings().getString("gui.privat-member.canuseanvil"));

     String canusedoorsandgates = toString(Settings.getSettings().getString("gui.privat-member.canusedoorsandgates"));

     String canusepech = toString(Settings.getSettings().getString("gui.privat-member.canusepech"));

     String canuseworkbench = toString(Settings.getSettings().getString("gui.privat-member.canuseworkbench"));

     String canusebed = toString(Settings.getSettings().getString("gui.privat-member.canusebed"));

     String canuseplity = toString(Settings.getSettings().getString("gui.privat-member.canuseplity"));

     String canusehoppers = toString(Settings.getSettings().getString("gui.privat-member.canusehoppers"));

     List<String> lorea = toStringList(Settings.getSettings().getStringList("gui.privat-member.lore-active"));
     List<String> lored = toStringList(Settings.getSettings().getStringList("gui.privat-member.lore-disactive"));

     String materialmojna = Settings.getSettings().getString("gui.privat-member.mojna-material");
     String materialnemojna = Settings.getSettings().getString("gui.privat-member.ne-mojna-material");

     if(ConfirmGUI.hasColor(materialmojna)){
         mmojna = Material.getMaterial(materialmojna.split(":")[0]);
         int data = Integer.parseInt(materialmojna.split(":")[1]);
         mojna = new ItemStack(mmojna, 1, (short)data);
     } else {
         mmojna = Material.getMaterial(materialmojna);
         mojna = new ItemStack(mmojna);
     }
     if(ConfirmGUI.hasColor(materialnemojna)){
         mnemojna = Material.getMaterial(materialnemojna.split(":")[0]);
         int data = Integer.parseInt(materialnemojna.split(":")[1]);
         nemojna = new ItemStack(mnemojna, 1, (short)data);
     } else {
         mnemojna = Material.getMaterial(materialnemojna);
         nemojna = new ItemStack(mnemojna);
     }

     if(pm.canUsePlity()) {
         useplity = new ItemStack(mojna);
         ItemMeta meta = useplity.getItemMeta();
         meta.setDisplayName(canuseplity);
         meta.setLore(lorea);
         useplity.setItemMeta(meta);
     } else {
         useplity = new ItemStack(nemojna);
         ItemMeta meta = useplity.getItemMeta();
         meta.setDisplayName(canuseplity);
         meta.setLore(lored);
         useplity.setItemMeta(meta);
     }
     if(pm.canUseBed()) {
         usebed = new ItemStack(mojna);
         ItemMeta meta = usebed.getItemMeta();
         meta.setDisplayName(canusebed);
         meta.setLore(lorea);
         usebed.setItemMeta(meta);
     } else {
         usebed = new ItemStack(nemojna);
         ItemMeta meta = usebed.getItemMeta();
         meta.setDisplayName(canusebed);
         meta.setLore(lored);
         usebed.setItemMeta(meta);
     }

     if(pm.canUseWorkBench()) {
         useworkbench = new ItemStack(mojna);
         ItemMeta meta = useworkbench.getItemMeta();
         meta.setDisplayName(canuseworkbench);
         meta.setLore(lorea);
         useworkbench.setItemMeta(meta);
     } else {
         useworkbench = new ItemStack(nemojna);
         ItemMeta meta = useworkbench.getItemMeta();
         meta.setDisplayName(canuseworkbench);
         meta.setLore(lored);
         useworkbench.setItemMeta(meta);
     }

     if(pm.canUseDoors()){
         usedoors = new ItemStack(mojna);
         ItemMeta meta = usedoors.getItemMeta();
         meta.setDisplayName(canusedoorsandgates);
         meta.setLore(lorea);
         usedoors.setItemMeta(meta);
     } else {
         usedoors = new ItemStack(nemojna);
         ItemMeta meta = usedoors.getItemMeta();
         meta.setDisplayName(canusedoorsandgates);
         meta.setLore(lored);
         usedoors.setItemMeta(meta);
     }

     if(pm.canUseAnvil()){
         useanvil = new ItemStack(mojna);
         ItemMeta meta = useanvil.getItemMeta();
         meta.setDisplayName(canuseanvil);
         meta.setLore(lorea);
         useanvil.setItemMeta(meta);
     } else {
         useanvil = new ItemStack(nemojna);
         ItemMeta meta = useanvil.getItemMeta();
         meta.setDisplayName(canuseanvil);
         meta.setLore(lored);
         useanvil.setItemMeta(meta);
     }

     if(pm.canChestOpen()){
         chestopen = new ItemStack(mojna);
         ItemMeta meta = chestopen.getItemMeta();
         meta.setDisplayName(canchestopen);
         meta.setLore(lorea);
         chestopen.setItemMeta(meta);
     } else {
         chestopen = new ItemStack(nemojna);
         ItemMeta meta = chestopen.getItemMeta();
         meta.setDisplayName(canchestopen);
         meta.setLore(lored);
         chestopen.setItemMeta(meta);
     }

     if(pm.canBlockBreak()){
         blockbreak = new ItemStack(mojna);
         ItemMeta meta = blockbreak.getItemMeta();
         meta.setDisplayName(canblockbreak);
         meta.setLore(lorea);
         blockbreak.setItemMeta(meta);
     } else {
         blockbreak = new ItemStack(nemojna);
         ItemMeta meta = blockbreak.getItemMeta();
         meta.setDisplayName(canblockbreak);
         meta.setLore(lored);
         blockbreak.setItemMeta(meta);
     }

     if(pm.canUseHopers()){
         usehoppers = new ItemStack(mojna);
         ItemMeta meta = usehoppers.getItemMeta();
         meta.setDisplayName(canusehoppers);
         meta.setLore(lorea);
         usehoppers.setItemMeta(meta);
     } else {
         usehoppers = new ItemStack(nemojna);
         ItemMeta meta = usehoppers.getItemMeta();
         meta.setDisplayName(canusehoppers);
         meta.setLore(lored);
         usehoppers.setItemMeta(meta);
     }

     if(pm.canBlockPlace()){
         blockplace = new ItemStack(mojna);
         ItemMeta meta = blockplace.getItemMeta();
         meta.setDisplayName(canblockplace);
         meta.setLore(lorea);
         blockplace.setItemMeta(meta);
     } else {
         blockplace = new ItemStack(nemojna);
         ItemMeta meta = blockplace.getItemMeta();
         meta.setDisplayName(canblockplace);
         meta.setLore(lored);
         blockplace.setItemMeta(meta);
     }

     if(pm.canUsePech()){
         usepech = new ItemStack(mojna);
         ItemMeta meta = usepech.getItemMeta();
         meta.setDisplayName(canusepech);
         meta.setLore(lorea);
         usepech.setItemMeta(meta);
     } else {
         usepech = new ItemStack(nemojna);
         ItemMeta meta = usepech.getItemMeta();
         meta.setDisplayName(canusepech);
         meta.setLore(lored);
         usepech.setItemMeta(meta);
     }

     if(pm.canUseCharka()){
         usecharka = new ItemStack(mojna);
         ItemMeta meta = usecharka.getItemMeta();
         meta.setDisplayName(canusecharka);
         meta.setLore(lorea);
         usecharka.setItemMeta(meta);
     } else {
         usecharka = new ItemStack(nemojna);
         ItemMeta meta = usecharka.getItemMeta();
         meta.setDisplayName(canusecharka);
         meta.setLore(lored);
         usecharka.setItemMeta(meta);
     }

     inventory.setItem(canuseanvilslot, useanvil);
     inventory.setItem(canusecharkaslot, usecharka);
     inventory.setItem(canusedoorsandgatesslot, usedoors);
     inventory.setItem(canusehoppersslot, usehoppers);
     inventory.setItem(canusepechslot, usepech);
     inventory.setItem(canuseplityslot, useplity);
     inventory.setItem(canuseworkbenchslot, useworkbench);
     inventory.setItem(canblockbreakslot, blockbreak);
     inventory.setItem(canblockplaceslot, blockplace);
     inventory.setItem(canchestopenslot, chestopen);
     inventory.setItem(canusebedslot, usebed);


     itemspmrg.put(useanvil, pmrg);
     itemspmrg.put(usecharka, pmrg);
     itemspmrg.put(usedoors, pmrg);
     itemspmrg.put(usehoppers, pmrg);
     itemspmrg.put(usepech, pmrg);
     itemspmrg.put(useplity, pmrg);
     itemspmrg.put(useworkbench, pmrg);
     itemspmrg.put(blockbreak, pmrg);
     itemspmrg.put(blockplace, pmrg);
     itemspmrg.put(chestopen, pmrg);
     itemspmrg.put(usebed, pmrg);

     return list;
    }

    public static ItemStack getMojna() {
        return mojna;
    }

    public static ItemStack getNemojna() {
        return nemojna;
    }

    private static String toString(String s){
        return Language.toString(s);
    }

    private static List<String> toStringList(List<String> list){
        for(int i = 0; i < list.size(); i++){
            list.set(i, toString(list.get(i)));
        }
        return list;
    }

    public static HashMap<ItemStack, Integer> getItemsslots() {
        return itemsslots;
    }

    public static String toStr(WellRegion wr, String s, String playername){
        s = s.replace("{PRIVAT-NAME}", Language.toString(wr.getPrivatname()))
                .replace("{PRIVAT-ID}", Integer.toString(wr.getId()))
                .replace("{PRIVAT-OWNER}", wr.getRgowner())
                .replace("{PRIVAT-BLOCK-X}", Integer.toString(wr.getPrivatblock().getX()))
                .replace("{PRIVAT-BLOCK-Y}", Integer.toString(wr.getPrivatblock().getY()))
                .replace("{PRIVAT-BLOCK-Z}", Integer.toString(wr.getPrivatblock().getZ()))
                .replace("{PLAYER-NAME}", playername);
        s = ChatColor.translateAlternateColorCodes('&', s);
        return s;
    }

    public static List<String> toStrList(WellRegion wr, List<String> list, String playername){
        for(int i = 0; i < list.size(); i++){
            list.set(i, toStr(wr, list.get(i), playername));
        }

        return list;
    }

    public static String getTitle() {
        return title;
    }

    public static HashMap<ItemStack, PMRG> getItemspmrg() {
        return itemspmrg;
    }

    public static int getBlockbreak() {
        return canblockbreakslot;
    }

    public static int getBlockplace() {
        return canblockplaceslot;
    }

    public static int getChestopen() {
        return canchestopenslot;
    }

    public static int getUseanvil() {
        return canuseanvilslot;
    }

    public static int getUsebed() {
        return canusebedslot;
    }

    public static int getUsecharka() {
        return canusecharkaslot;
    }

    public static int getDelte() {
        return delteslot;
    }

    public static int getUsedoors() {
        return canusedoorsandgatesslot;
    }

    public static int getUsehoppers() {
        return canusehoppersslot;
    }

    public static int getUsepech() {
        return canusepechslot;
    }

    public static int getUseplity() {
        return canuseplityslot;
    }

    public static int getUseworkbench() {
        return canuseworkbenchslot;
    }
}
