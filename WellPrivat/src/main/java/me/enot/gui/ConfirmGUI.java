package me.enot.gui;

import me.enot.configurations.Settings;
import me.enot.configurations.lang.Language;
import me.enot.privat.PrivatMember;
import me.enot.privat.WellRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ConfirmGUI {

    public static String title = Language.toString(Settings.getSettings().getString("gui.confirm.title"));

    private static int slots = Settings.getSettings().getInt("gui.confirm.slots");

    private static Inventory inventory = Bukkit.createInventory(null, slots, title);

    public static void open(Player p, WellRegion wr){
        load(wr);

        p.openInventory(inventory);
    }
    public static void open(Player p, PrivatMember pm){
        load(pm);

        p.openInventory(inventory);
    }

    public static ItemStack yes;
    public static ItemStack no;

    private static void load(WellRegion wr){
        int privateslot = Settings.getSettings().getInt("gui.confirm.private-slot");
        int yesslot = Settings.getSettings().getInt("gui.confirm.yes-slot");
        int noslot = Settings.getSettings().getInt("gui.confirm.no-slot");

        ItemStack privat = getPrivate(wr);
        yes = getYes(wr);
        no = getNo(wr);

        inventory.setItem(privateslot, privat);
        inventory.setItem(yesslot, yes);
        inventory.setItem(noslot, no);
    }
    private static void load(PrivatMember privatMember){
        int privatmemberslot = Settings.getSettings().getInt("gui.confirm.member-slot");
        int yesslot = Settings.getSettings().getInt("gui.confirm.yes-slot");
        int noslot = Settings.getSettings().getInt("gui.confirm.no-slot");

        ItemStack privat = getPrivate(privatMember);
        yes = getYes(privatMember);
        no = getNo(privatMember);

        inventory.setItem(privatmemberslot, privat);
        inventory.setItem(yesslot, yes);
        inventory.setItem(noslot, no);
    }

    public static boolean hasColor(String s){
        String[] ss = s.split(":");
        return ss.length > 1;
    }

    private static ItemStack getPrivate(WellRegion wr) {
        Material material = Settings.getSettings().getEnum(Material.class, "gui.confirm.privat-material");
        ItemStack stack = new ItemStack(material);

        String displayname = toStr(wr, Settings.getSettings().getString("gui.confirm.private-display-name"));
        List<String> lore = toStrList(wr, Settings.getSettings().getStringList("gui.confirm.private-lore"));

        ItemMeta im = stack.getItemMeta();

        im.setDisplayName(displayname);
        im.setLore(lore);

        stack.setItemMeta(im);

        return stack;
    }
    private static ItemStack getPrivate(PrivatMember pm) {
        Material material = Settings.getSettings().getEnum(Material.class, "gui.confirm.member-material");
        ItemStack stack = new ItemStack(material);

        String displayname = toStr(pm, Settings.getSettings().getString("gui.confirm.member-display-name"));
        List<String> lore = toStrList(pm, Settings.getSettings().getStringList("gui.confirm.member-lore"));

        ItemMeta im = stack.getItemMeta();

        im.setDisplayName(displayname);
        im.setLore(lore);

        stack.setItemMeta(im);

        return stack;
    }
    private static ItemStack getYes(WellRegion wr) {
        String s = Settings.getSettings().getString("gui.confirm.yes-material");

        Material material;
        ItemStack stack;

        if(hasColor(s)){
            material = Material.getMaterial(s.split(":")[0]);
            stack = new ItemStack(material, 1, (short) Integer.parseInt(s.split(":")[1]));
        } else {
            material = Material.getMaterial(s);
            stack = new ItemStack(material);
        }
        String displayname = toStr(wr, Settings.getSettings().getString("gui.confirm.yes-display-name"));
        List<String> lore = toStrList(wr, Settings.getSettings().getStringList("gui.confirm.yes-lore"));

        ItemMeta im = stack.getItemMeta();

        im.setDisplayName(displayname);
        im.setLore(lore);

        stack.setItemMeta(im);

        return stack;
    }

    private static ItemStack getNo(PrivatMember pm) {
        String s = Settings.getSettings().getString("gui.confirm.no-material");


        Material material;
        ItemStack stack;

        if(hasColor(s)){
            material = Material.getMaterial(s.split(":")[0]);
            stack = new ItemStack(material, 1, (short) Integer.parseInt(s.split(":")[1]));
        } else {
            material = Material.getMaterial(s);
            stack = new ItemStack(material);
        }
        String displayname = toStr(pm, Settings.getSettings().getString("gui.confirm.no-display-name"));
        List<String> lore = toStrList(pm, Settings.getSettings().getStringList("gui.confirm.no-lore"));

        ItemMeta im = stack.getItemMeta();

        im.setDisplayName(displayname);
        im.setLore(lore);

        stack.setItemMeta(im);

        return stack;
    }

    private static ItemStack getYes(PrivatMember pm) {
        String s = Settings.getSettings().getString("gui.confirm.yes-material");

        Material material;
        ItemStack stack;

        if(hasColor(s)){
            material = Material.getMaterial(s.split(":")[0]);
            stack = new ItemStack(material, 1, (short) Integer.parseInt(s.split(":")[1]));
        } else {
            material = Material.getMaterial(s);
            stack = new ItemStack(material);
        }
        String displayname = toStr(pm, Settings.getSettings().getString("gui.confirm.yes-display-name"));
        List<String> lore = toStrList(pm, Settings.getSettings().getStringList("gui.confirm.yes-lore"));

        ItemMeta im = stack.getItemMeta();

        im.setDisplayName(displayname);
        im.setLore(lore);

        stack.setItemMeta(im);

        return stack;
    }

    private static ItemStack getNo(WellRegion wr) {
        String s = Settings.getSettings().getString("gui.confirm.no-material");


        Material material;
        ItemStack stack;

        if(hasColor(s)){
            material = Material.getMaterial(s.split(":")[0]);
            stack = new ItemStack(material, 1, (short) Integer.parseInt(s.split(":")[1]));
        } else {
            material = Material.getMaterial(s);
            stack = new ItemStack(material);
        }
        String displayname = toStr(wr, Settings.getSettings().getString("gui.confirm.no-display-name"));
        List<String> lore = toStrList(wr, Settings.getSettings().getStringList("gui.confirm.no-lore"));

        ItemMeta im = stack.getItemMeta();

        im.setDisplayName(displayname);
        im.setLore(lore);

        stack.setItemMeta(im);

        return stack;
    }

    private static String toStr(WellRegion wr, String s){
        s = s.replace("{PRIVAT-NAME}", Language.toString(wr.getPrivatname()))
                .replace("{PRIVAT-ID}", Integer.toString(wr.getId()))
                .replace("{PRIVAT-OWNER}", wr.getRgowner())
                .replace("{PRIVAT-BLOCK-X}", Integer.toString(wr.getPrivatblock().getX()))
                .replace("{PRIVAT-BLOCK-Y}", Integer.toString(wr.getPrivatblock().getY()))
                .replace("{PRIVAT-BLOCK-Z}", Integer.toString(wr.getPrivatblock().getZ()));
        s = ChatColor.translateAlternateColorCodes('&', s);
        return s;
    }

    private static List<String> toStrList(WellRegion wr, List<String> list){
        for(int i = 0; i < list.size(); i++){
            list.set(i, toStr(wr, list.get(i)));
        }

        return list;
    }

    private static String toStr(PrivatMember pm, String s){
        s = s.replace("{NAME}", Language.toString(pm.getPlayername()));
        s = ChatColor.translateAlternateColorCodes('&', s);
        return s;
    }

    private static List<String> toStrList(PrivatMember pm, List<String> list){
        for(int i = 0; i < list.size(); i++){
            list.set(i, toStr(pm, list.get(i)));
        }

        return list;
    }

}
