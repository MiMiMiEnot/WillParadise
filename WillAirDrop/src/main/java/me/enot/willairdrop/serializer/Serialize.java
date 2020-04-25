package me.enot.willairdrop.serializer;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import me.enot.willairdrop.configs.Settings;
import me.enot.willairdrop.configs.language.Langs;
import me.enot.willairdrop.configs.language.Language;
import me.enot.willairdrop.configs.language.Replace;
import me.enot.willairdrop.logic.utils.Calculations;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Serialize {

    private static ArrayList<Loot> loots = new ArrayList<>();
    public static void load(){
        File dir = Settings.lootdir;
        File[] files = dir.listFiles();
        if(files != null){
            long milis = System.currentTimeMillis();
            int i = 0;
            for(File f : files){
                loots.add(serialize(f));
                i++;
            }
            long nowmilis = System.currentTimeMillis();

            long time = nowmilis - milis;

            Language.sendMessage(Bukkit.getConsoleSender(), Langs.main__loots_loader,
                    new Replace("{X}", Integer.toString(i)),
                    new Replace("{TIME}", Long.toString(time)));
        }
    }

    public static ArrayList<Loot> getLoots() {
        return loots;
    }

    protected static Loot serialize(File f){
        if(f.getName().endsWith(".conf")){
            Config config = ConfigFactory.parseFile(f);
            Integer duration = Calculations.toSeconds(config.getString("to-open-cooldown"));
            if(duration == null){
                Language.sendMessage(Bukkit.getConsoleSender(), Langs.main__error__load_error_loot_invalid_time,
                        new Replace("{LOOTNAME}", f.getName().split("\\.")[0]));
                return null;
            }
            int slots = config.getInt("slots");
            String title = Language.toString(config.getString("title"));
            Inventory inventory = Bukkit.createInventory(null, slots, title);
            for(Map.Entry<String, ConfigValue> entry : config.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("loot.")) {
                    ConfigValue value = entry.getValue();
                    String[] keysplit = key.split("\\.");
                    //Bukkit.getConsoleSender().sendMessage(key);
                    String name = keysplit[1];
                    int slot = config.getInt("loot." + name + ".slot");
                    ItemStack stack = serializeByPath(config, name);

                    inventory.setItem(slot, stack);
                }
            }

            return new Loot(f.getName().split("\\.")[0], inventory, duration);
        }
        return null;
    }

    protected static Loot serialize(String filename){
        return serialize(new File(Settings.lootdir, filename + ".conf"));
    }

    private static ItemStack serializeByPath(Config c, String path) {

        String pathstart = "loot." + path + ".";
        String pathdata = pathstart + "stack-data.data";
        String pathamount = pathstart + "stack-data.amount";
        String pathlore = pathstart + "stack-meta.lore";
        String pathname = pathstart + "stack-meta.name";
        String pathenchs = pathstart + "stack-meta.enchantements";


        Material material = c.getEnum(Material.class, "loot." + path + ".material");
        int amount = 1;
        short data = 0;
        if(hasPath(c, pathamount)) amount = c.getInt(pathamount);
        if(hasPath(c, pathdata)) data = (short)c.getInt(pathdata);

        ItemStack stack = new ItemStack(material, amount, data);

        ItemMeta meta = stack.getItemMeta();

        if(hasPath(c, pathlore)){
            List<String> list = Language.toStringList(c.getStringList(pathlore));
            meta.setLore(list);
        }
        if(hasPath(c, pathname)){
            String displayname = Language.toString(c.getString(pathname));
            meta.setDisplayName(displayname);
        }
        if(hasPath(c, pathenchs)){
            HashMap<Enchantment, Integer> enchs = loadEnchs(c, pathenchs);
            for(Map.Entry<Enchantment, Integer> entry : enchs.entrySet()){
                Enchantment ench = entry.getKey();
                int level = entry.getValue();

                meta.addEnchant(ench, level, true);
            }
        }

        stack.setItemMeta(meta);
        return stack;
    }


    private static boolean hasPath(Config c, String path){
        return c.hasPath(path);
    }

    private static HashMap<Enchantment, Integer> loadEnchs(Config c, String path){
        HashMap<Enchantment, Integer> map = new HashMap<>();
        for(Map.Entry<String, ConfigValue> entry : c.entrySet()){
            String key = entry.getKey();
            ConfigValue value = entry.getValue();
            if(key.startsWith(path)){
                String[] splited = key.split("\\.");
                String ench = splited[4];
                Object levels = value.unwrapped();
                int level = 1;
                if(levels instanceof Integer){
                    level = ((Integer) levels).intValue();
                }
                map.put(Enchantment.getByName(ench), level);
            }
        }
        return map;
    }

    public static Loot findByName(String name){
        for(Loot l : loots){
            if(l.getLootName().equalsIgnoreCase(name)){
                return l;
            }
        }
        return null;
    }

}
