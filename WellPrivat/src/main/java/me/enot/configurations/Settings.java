package me.enot.configurations;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueFactory;
import me.enot.WellPrivate;
import me.enot.configurations.lang.Language;
import me.enot.gui.listener.InventoryEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Settings {
    private static Plugin plugin = WellPrivate.getPlugin();
    private static Config settingscnf;
    private static File file = new File(plugin.getDataFolder(), "settings.conf");
    private static Settings settings = new Settings();
    public static File rgdir = new File(plugin.getDataFolder(), "regions/");
    public static File bedspawn = new File(plugin.getDataFolder(), "bedspawns/");

    private static void create(){
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        if(!rgdir.exists()){
            rgdir.mkdir();
        }
        if(!bedspawn.exists()){
            bedspawn.mkdir();
        }
        try {
            if(!file.exists()) {
                InputStream is = settings.getClass().getResourceAsStream("/settings.conf");
                if (is != null) {
                    Files.copy(is, file.toPath());
                }
            }
            settingscnf = ConfigFactory.parseFile(file).resolve();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Config getSettings() {
        return settingscnf;
    }

    private static HashMap<Material, Integer> rgsblocks = new HashMap<>();

    private static List<Material> chests = new ArrayList<>();
    private static List<Material> anvil = new ArrayList<>();
    private static List<Material> enchs = new ArrayList<>();
    private static List<Material> doorsandgates = new ArrayList<>();
    private static List<Material> pech = new ArrayList<>();
    private static List<Material> hopper = new ArrayList<>();
    private static List<Material> workbench = new ArrayList<>();
    private static List<Material> bed = new ArrayList<>();
    private static List<Material> blockedtoplace = new ArrayList<>();

    private static Material material;
    private static ItemStack havent;

    private static Material material1;
    private static ItemStack havent1;

    private static List<String> blockedsymbols = new ArrayList<>();
    private static String color;
    private static List<String> allowedworlds = new ArrayList<>();

    private static List<String> remember = new ArrayList<>();

    private static List<EntityType> blockedentitys = new ArrayList<>();
    public static void reload(){
        if(file == null || settingscnf == null){
            create();
        }
        settingscnf = ConfigFactory.parseFile(file);
        List<String> s = getSettings().getStringList("rg-blocks-and-radius");
        for(String st : s){
            Material material = Material.getMaterial(st.split(":")[0]);
            Integer radius = Integer.parseInt(st.split(":")[1]);
            rgsblocks.put(material, radius);
        }
        material = Settings.getSettings().getEnum(Material.class, "gui.privat-list.privat-block-if-havent-privats");
        havent = new ItemStack(material);
        material1 = Settings.getSettings().getEnum(Material.class, "gui.privat-player-list.privat-doesnt-have-players-material");
        havent1 = new ItemStack(material1);
        chests = getSettings().getEnumList(Material.class, "blocked-blocks.chests");
        anvil = getSettings().getEnumList(Material.class, "blocked-blocks.anvil");
        enchs = getSettings().getEnumList(Material.class, "blocked-blocks.enchattable");
        doorsandgates = getSettings().getEnumList(Material.class, "blocked-blocks.doors-and-gates");
        pech = getSettings().getEnumList(Material.class, "blocked-blocks.pech");
        hopper = getSettings().getEnumList(Material.class, "blocked-blocks.hopper");
        workbench = getSettings().getEnumList(Material.class, "blocked-blocks.workbench");
        bed = getSettings().getEnumList(Material.class, "blocked-blocks.bed");
        blockedtoplace = getSettings().getEnumList(Material.class, "block-to-place");

        String title = Language.toString(getSettings().getString("gui.privat-list.private-block-if-havent-privats-name "));
        List<String> lore = Language.toStringList(getSettings().getStringList("gui.privat-list.private-block-if-havent-privats-lore"));
        ItemMeta meta = havent.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(lore);
        havent.setItemMeta(meta);

        String displayname = Language.toString(getSettings().getString("gui.privat-player-list.privat-doesnt-have-players-name"));
        List<String> lore1 = Language.toStringList(getSettings().getStringList("gui.privat-player-list.privat-doesnt-have-players-lore"));
        ItemMeta meta1 = havent1.getItemMeta();
        meta1.setDisplayName(displayname);
        meta1.setLore(lore1);
        havent1.setItemMeta(meta1);

        allowedworlds = getSettings().getStringList("allowed-worlds");
        blockedsymbols = getSettings().getStringList("privat-name-disallowed-ellemets");
        color = getSettings().getString("privat-name-standart-color");
        remember = getSettings().getStringList("remember");

        blockedentitys = getSettings().getEnumList(EntityType.class, "blocked-entitys");
    }

    public static String getColor() {
        return color;
    }

    public static List<String> getBlockedsymbols() {
        return blockedsymbols;
    }

    public static HashMap<Material, Integer> getRgsblocks() {
        return rgsblocks;
    }

    public static List<Material> getChests() {
        return chests;
    }

    public static List<Material> getAnvil() {
        return anvil;
    }

    public static List<Material> getBed() {
        return bed;
    }

    public static List<Material> getDoorsandgates() {
        return doorsandgates;
    }

    public static List<Material> getEnchs() {
        return enchs;
    }

    public static List<Material> getHopper() {
        return hopper;
    }

    public static List<Material> getPech() {
        return pech;
    }

    public static List<Material> getWorkbench() {
        return workbench;
    }

    public static ItemStack getHavent() {
        return havent;
    }

    public static ItemStack getHaventPlayer() {
        return havent1;
    }

    public static List<Material> getBlockedtoplace() {
        return blockedtoplace;
    }

    public static List<String> getRemember() {
        return remember;
    }

    public static List<EntityType> getBlockedEntitys() {
        return blockedentitys;
    }

    public static void save(){
        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(file.toPath(), settingscnf.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void addID(){
        settingscnf = settingscnf.withValue("id", ConfigValueFactory.fromAnyRef(Settings.getSettings().getInt("id") + 1));
        save();
    }

    public static void rememberAdd(String playername){
        remember.add(playername);
        settingscnf = getSettings().withValue("remember", ConfigValueFactory.fromAnyRef(remember));
        save();
    }
    public static void rememberRemove(String playername){
        remember.remove(playername);
        settingscnf = getSettings().withValue("remember", ConfigValueFactory.fromAnyRef(remember));
        save();
    }

    public static List<String> getAllowedWorlds() {
        return allowedworlds;
    }
}
