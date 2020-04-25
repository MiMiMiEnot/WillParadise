package me.enot.willairdrop.logic.utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import me.enot.willairdrop.WillAirDrop;
import me.enot.willairdrop.configs.Settings;
import me.enot.willairdrop.configs.language.Langs;
import me.enot.willairdrop.configs.language.Language;
import me.enot.willairdrop.configs.language.Replace;
import me.enot.willairdrop.logic.AirDrop;
import me.enot.willairdrop.serializer.Loot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AirDropLogick {

    public static boolean placeIsValid(Corner corner) {
        //Location loc = new Location(Bukkit.getWorld(Settings.getSpawnWorld()), corner.getX(), corner.getY(), corner.getZ());

        //int blocky = corner.getY();

        Location loc = new Location(Bukkit.getWorld(Settings.getSpawnWorld()), corner.getX(), (corner.getY() -1), corner.getZ());

        return !Settings.getBlockedMaterials().contains(loc.getBlock().getType());
        /*Block b = null;

        for (int i = blocky; i >= 0; i--) {
            Location templocation = new Location(Bukkit.getWorld(Settings.getSpawnWorld()), corner.getX(), i, corner.getZ());
            b = templocation.getBlock();
            if (b != null && b.getType() != Material.AIR) {
                break;
            }
        }

        if (b != null) {
            return !Settings.getBlockedMaterials().contains(b.getType());
        }
        */
    }

    private static HashMap<Block, AirDrop> dropList = new HashMap<>();

    public static void startTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(WillAirDrop.getPlugin(), () -> {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String s = formatter.format(date);
            if (list.contains(s)) {
                int onlinePlayers = Bukkit.getOnlinePlayers().size();
                int minPlayers = Settings.getMinPlayers();
                if (onlinePlayers >= minPlayers) {
                    AirDrop drop = Calculations.generateRandomAirDrop();
                    drop.call();
                } else {
                    Language.broadcastMessage(Langs.crate__min_players,
                            new Replace("{X}", Integer.toString(onlinePlayers)),
                            new Replace("{Y}", Integer.toString(minPlayers)));
                }
            }
        }, 0, 20);
    }

    public static HashMap<Block, AirDrop> getDropList() {
        return dropList;
    }

    private static List<String> list = new ArrayList<>();

    public static void formatedTimeList() {
        List<String> cnflist = Settings.getLootTime();

        for (String s : cnflist) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            String time = Settings.getAnnounceTimeString();
            String[] splited = time.split("");
            try {
                Date date = format.parse(s);

                StringBuilder t = new StringBuilder();
                for (int i = 0; i < (splited.length - 1); i++) {
                    t.append(splited[i]);
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                switch (splited[splited.length - 1]) {
                    case "h":
                        calendar.add(Calendar.HOUR, (Integer.parseInt(t.toString()) * -1));
                        break;
                    case "m":
                        calendar.add(Calendar.MINUTE, (Integer.parseInt(t.toString()) * -1));
                        break;
                }
                Date after = calendar.getTime();
                list.add(format.format(after));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getList() {
        return list;
    }

    private static HashMap<AirDrop, Inventory> inventoryMap = new HashMap<>();

    public static void doAnimation(AirDrop drop) {
        // TODO: 16.03.2020 Создание анимации
        Hologram hol = HologramsAPI.createHologram(WillAirDrop.getPlugin(), drop.getHologram().getLocation());
        ItemLine itemLine = hol.appendItemLine(new ItemStack(Settings.getAnimationMaterial()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Hologram animation до начала анимации " + hol.getY());
        while (hol.getY() > drop.getLocation().getY()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "while " + hol.getY() + " > " + drop.getLocation().getY());
            hol.teleport(hol.getLocation().getWorld(), hol.getX(), hol.getY() - Settings.getAnimationMoveY(), hol.getZ());
        }
        Bukkit.getConsoleSender().sendMessage("Удаление голограммы ");
        hol.delete();
        Bukkit.getConsoleSender().sendMessage("Установка сундука ");
        //Bukkit.getScheduler().runTaskLater(WillAirDrop.getPlugin(), () -> {
        Material material = Settings.getLootMaterial();
        drop.getLocation().setY(Calculations.generateValidY(drop.getLocation().getBlockX(), drop.getLocation().getBlockZ()));
        Block block = drop.getLocation().getBlock();
        block.setType(material);
        for (String s : Language.getMessage(Langs.hologram__airdrop)) {
            drop.getHologram().appendTextLine(s);
        }
        dropList.put(block, drop);
        Inventory inv = drop.getLoot().getInventory();
        inventoryMap.put(drop, clone(inv));
        //}, 5 * 20);
    }

    public static HashMap<AirDrop, Inventory> getInventoryMap() {
        return inventoryMap;
    }

    public static Inventory clone(Inventory inv){
        Inventory inventory = Bukkit.createInventory(null, inv.getSize(), inv.getTitle());
        for(int i = 0; i < inv.getSize(); i++){
            ItemStack stack = inv.getItem(i);
            if(stack != null && stack.getType() != Material.AIR){
                inventory.setItem(i, stack);
            }
        }
        return inventory;
    }
}

