package me.enot.willairdrop.logic.utils;

import com.typesafe.config.ConfigValue;
import me.enot.willairdrop.configs.Settings;
import me.enot.willairdrop.logic.AirDrop;
import me.enot.willairdrop.serializer.Loot;
import me.enot.willairdrop.serializer.Serialize;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Saver {
    private static final String SAVEPATH = "save.";


    public static void save(){
        List<AirDrop> activeDrops = new ArrayList<>();
        AirDropLogick.getDropList().forEach((k, v) -> activeDrops.add(v));
        int i = 0;
        for(AirDrop drop : activeDrops){
            String lootName = drop.getLoot().getLootName();
            int x = drop.getLocation().getBlockX();
            int y = drop.getLocation().getBlockY();
            int z = drop.getLocation().getBlockZ();
            boolean opened = drop.isOpened();
            Settings.add(SAVEPATH + i + ".loot-name", lootName);
            Settings.add(SAVEPATH + i + ".location.X", Integer.toString(x));
            Settings.add(SAVEPATH + i + ".location.Y", Integer.toString(y));
            Settings.add(SAVEPATH + i + ".location.Z", Integer.toString(z));
            Settings.add(SAVEPATH + i + ".opened", opened);
            i++;
        }

    }
    public static void load(){
        for(Map.Entry<String, ConfigValue> entry : Settings.getSettings().entrySet()){
            if(entry.getKey().startsWith(SAVEPATH)) {
                String key = entry.getKey();
                //Bukkit.getConsoleSender().sendMessage(key);
                String num = key.split("\\.")[1];
                //Bukkit.getConsoleSender().sendMessage(num);
                if (key.startsWith(SAVEPATH + num + "\\.")) {
                    String lootName = Settings.getSettings().getString((SAVEPATH + num + ".loot-name"));
                    int x = Settings.getSettings().getInt(SAVEPATH + num + ".location.X");
                    int y = Settings.getSettings().getInt(SAVEPATH + num + ".location.Y");
                    int z = Settings.getSettings().getInt(SAVEPATH + num + ".location.Z");
                    boolean opened = Settings.getSettings().getBoolean(SAVEPATH + num + ".opened");

                    Loot l = Serialize.findByName(lootName);
                    Location location = Calculations.generateLocation(x, y, z);
                    AirDrop drop = new AirDrop(l, location, opened);
                    location.getBlock().setType(Settings.getLootMaterial());
                    Block b = location.getBlock();
                    AirDropLogick.getDropList().put(b, drop);
                    Inventory inv = drop.getLoot().getInventory();
                    AirDropLogick.getInventoryMap().put(drop, AirDropLogick.clone(inv));

                    Settings.remove(SAVEPATH + num);
                }
            }
        }
    }
}
