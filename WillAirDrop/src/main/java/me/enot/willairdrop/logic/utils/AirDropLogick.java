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
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

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
        drop.getLocation().setY(Calculations.generateValidY(drop.getLocation().getBlockX(), drop.getLocation().getBlockZ()));
        Location location = drop.getLocation().clone();
        location.setX(drop.getLocation().getBlockX() + 0.5);
        location.setY(drop.getLocation().getBlockY() + 3);
        location.setZ(drop.getLocation().getBlockZ() + 0.5);
        Bukkit.getConsoleSender().sendMessage("dropY: " + drop.getLocation().getY() + " hologramY: " + location.getY());
        Hologram hol = HologramsAPI.createHologram(WillAirDrop.getPlugin(), location);
        hol.appendItemLine(new ItemStack(Settings.getAnimationMaterial()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Hologram animation до начала анимации " + hol.getY());

        new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = new Location(hol.getLocation().getWorld(), hol.getX(), hol.getY() - Settings.getAnimationMoveY(), hol.getZ());
                if (hol.getLocation().getY() > drop.getLocation().getY()) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "while " + hol.getLocation().getY()+ " > " + drop.getLocation().getY());
                    hol.teleport(loc);
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "while " + hol.getLocation().getY()+ " < " + drop.getLocation().getY());
                    Bukkit.getConsoleSender().sendMessage("Удаление голограммы ");
                    spawnFireworks(hol.getLocation());
                    hol.delete();
                    cancel();
                    Bukkit.getConsoleSender().sendMessage("Установка сундука ");
                    Material material = Settings.getLootMaterial();
                    Block block = drop.getLocation().getBlock();
                    block.setType(material);
                    Location location = drop.getLocation().clone();
                    double x = location.getBlockX() + Settings.getHologramX();
                    double y = location.getBlockY() + Settings.getHologramY();
                    double z = location.getBlockZ() + Settings.getHologramZ();
                    loc.setX(x);
                    loc.setY(y);
                    loc.setZ(z);
                    drop.setHologram(HologramsAPI.createHologram(WillAirDrop.getPlugin(), loc));
                    for (String s : Language.getMessage(Langs.hologram__airdrop)) {
                        drop.getHologram().appendTextLine(s);
                    }
                    dropList.put(block, drop);
                }
            }
        }.runTaskTimer(WillAirDrop.getPlugin(), 0, Settings.getAnimationSchedulerTicks());
        Inventory inv = drop.getLoot().getInventory();
        inventoryMap.put(drop, clone(inv));
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
    public static void spawnFireworks(Location loc){
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();;
        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).build());

        fw.setFireworkMeta(fwm);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(WillAirDrop.getPlugin(), fw::detonate, (2));
    }
}