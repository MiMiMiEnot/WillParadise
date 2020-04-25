package me.enot.willairdrop.logic.utils;

import me.enot.willairdrop.configs.Settings;
import me.enot.willairdrop.configs.language.Langs;
import me.enot.willairdrop.configs.language.Language;
import me.enot.willairdrop.logic.AirDrop;
import me.enot.willairdrop.serializer.Loot;
import me.enot.willairdrop.serializer.Serialize;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.List;

public class Calculations {

    private static int randomNumber(int minimum, int maximum){
        double random = Math.random();
        Bukkit.getConsoleSender().sendMessage("random " + random);
        maximum -= minimum;
        int rand = (int) (random * ++maximum)+ minimum;
        Bukkit.getConsoleSender().sendMessage("random2 " + rand);
        return rand;
    }

    private static int randomX(){
        return randomNumber(Math.min(Settings.getMaxX(), Settings.getMinX()), Math.max(Settings.getMaxX(), Settings.getMinX()));
    }
    private static int randomZ(){
        return randomNumber(Math.min(Settings.getMaxZ(), Settings.getMinZ()), Math.max(Settings.getMaxZ(), Settings.getMinZ()));
    }

    public static Corner generateRandomCorner(){
        int x = randomX();
        int z = randomZ();

        Block b = null;
        Location loc = null;
        for(int i = 256; i >= 0; i--){
            Location templocation = new Location(Bukkit.getWorld(Settings.getSpawnWorld()), x, i, z);
            b = templocation.getBlock();
            if(b != null && b.getType() != Material.AIR){
                break;
            }
        }

        int y = b.getLocation().getBlockY() + 1;
        return new Corner(x, y, z);
    }

    public static Location generateLocation(int x, int z){
        World world = Bukkit.getWorld(Settings.getSpawnWorld());

        Block b = null;

        for(int i = 256; i >= 0; i--){
            Location templocation = new Location(Bukkit.getWorld(Settings.getSpawnWorld()), x, i, z);
            b = templocation.getBlock();
            if(b != null && b.getType() != Material.AIR){
                break;
            }
        }

        int y = b.getY() + 1;

        return new Location(world, x, y, z);
    }
    public static Location generateLootSpawnLocation(){
        World world = Bukkit.getWorld(Settings.getSpawnWorld());
        Corner c;

        do {
            c = generateRandomCorner();
            Bukkit.getConsoleSender().sendMessage("Новый корнер сформирован " + c.getX() + " " + c.getY() + " " + c.getZ());
        } while (!AirDropLogick.placeIsValid(c));

        int x = c.getX();
        int y = c.getY();
        int z = c.getZ();

        Location loc = new Location(world, x, y, z);
        //loc.getChunk().load(true);
        return loc;
    }
    public static Location generateLocation(int x, int y, int z){
        World world = Bukkit.getWorld(Settings.getSpawnWorld());

        return new Location(world, x, y, z);
    }

    public static int generateValidY(int x, int z){
        World world = Bukkit.getWorld(Settings.getSpawnWorld());
        /*int y = 256;
        Location location = new Location(world, x, y, z);
        do {
            Bukkit.getConsoleSender().sendMessage("Y до " + location.getBlockY());
            location.setY(y--);
            Bukkit.getConsoleSender().sendMessage("Y после " + location.getBlockY());
        } while (location.getBlock().getType() == Material.AIR);
*/
        return getValidY(world, x, z);
    }

    public static int getValidY(World w, int x, int z){
        for(int i = 256; i >= 0; i--){
            Location loc = new Location(w, x, i, z);
            if(loc.getBlock() != null && !Settings.getBlockedMaterials().contains(loc.getBlock().getType())) {
                return i + 1;
            }
        }
        return -1;
    }

    public static Integer toSeconds(String time){
        if(time.contains(" ")) {
            String[] splited = time.split(" ");
            int i = 0;
            for (String s : splited) {
                Integer temp = toSecondsByOne(s);
                if (temp != null) {
                    i += temp;
                } else {
                    return null;
                }
            }
            if (i == 0) {
                return null;
            } else {
                return i;
            }
        } else {
            return toSecondsByOne(time);
        }
    }

    public static Integer toSecondsByOne(String time){
        String[] splited = time.split("");
        if(splited.length > 1) {
            StringBuilder num = new StringBuilder();
            for(int i = 0; i < (splited.length - 1); i++){
                num.append(splited[i]);
            }
            String type = splited[splited.length-1];
            try {
                int i = Integer.parseInt(num.toString());
                switch (type) {
                    case "h":
                        i *= 3600;
                        break;
                    case "m":
                        i *= 60;
                        break;
                    default:
                        break;
                }
                return i;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static String seccondsToString(int seconds){

        String result = "";

        StringBuilder hours = new StringBuilder();
        for(String s : Language.getMessage(Langs.crate__hours)) { hours.append(s);}
        StringBuilder mins = new StringBuilder();
        for(String s : Language.getMessage(Langs.crate__minutes)) { mins.append(s);}
        StringBuilder secs = new StringBuilder();
        for(String s : Language.getMessage(Langs.crate__seconds)) { secs.append(s);}

        int hourses = 0;
        int minutes = 0;
        if(seconds >= 3600){
            hourses = seconds/3600;
            seconds -= (hourses * 3600);
        }
        if(seconds >= 60){
            minutes = seconds/60;
            seconds -= (minutes * 60);
        }

        if(hourses > 0) result += hourses + " " + hours.toString();
        if(minutes > 0) result += minutes + " " + mins.toString();
        if(seconds > 0) result += seconds + " " + secs.toString();


       return result;
    }

    private static Loot latest = null;

    public static Loot generateRandomLoot(){
        //Bukkit.getConsoleSender().sendMessage("1");
        List<Loot> lootList = Serialize.getLoots();

        Loot l;
        do {
            //Bukkit.getConsoleSender().sendMessage("2");
            int i = randomNumber(0, lootList.size() - 1);
            l = lootList.get(i);
            //ukkit.getConsoleSender().sendMessage(l.getLootName());
            if(latest != null) Bukkit.getConsoleSender().sendMessage(latest.getLootName());
        } while (latest != null && latest.getLootName().equalsIgnoreCase(l.getLootName()));
        //Bukkit.getConsoleSender().sendMessage("12312");
        if(latest == null) latest = l;

        return l;
    }


    public static AirDrop generateRandomAirDrop(){
        return new AirDrop(generateRandomLoot(), generateLootSpawnLocation());
    }


}
