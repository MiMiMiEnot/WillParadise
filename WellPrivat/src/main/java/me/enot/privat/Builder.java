package me.enot.privat;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import me.enot.configurations.Settings;
import me.enot.configurations.lang.Langs;
import me.enot.configurations.lang.Language;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Builder {

    public static boolean canCreateRG(Block block, Player player){
            int maxprivats = maxPrivats(player);
            int privats = getPlayerPrivats(player.getName()).size();
            if(privats < maxprivats){
                return true;
            } else {
                player.sendMessage(Language.get(Langs.rgs__max_privats)
                        .replace("{X}", Integer.toString(privats))
                        .replace("{Y}", Integer.toString(maxprivats))
                );
            }
        return false;
    }

    private static List<WellRegion> rglist = new ArrayList<>();
    public static void removeRGFromList(WellRegion wr){
        rglist.remove(wr);
    }
    public static void loadRGS(){
        int i = 0;
        File rgdir = Settings.rgdir;
        File[] rgdirfiles = rgdir.listFiles();
        if(rgdirfiles != null){
            for(File t : rgdirfiles){
                Config c = ConfigFactory.parseFile(t);
                Integer id = Integer.parseInt(t.getName().replace(".conf", ""));
                String privatname = c.getString("privat-name");
                Corner corner1 = new Corner(
                        c.getInt("corners.first.X"),
                        c.getInt("corners.first.Y"),
                        c.getInt("corners.first.Z")
                        );
                Corner corner2 = new Corner(
                        c.getInt("corners.second.X"),
                        c.getInt("corners.second.Y"),
                        c.getInt("corners.second.Z")
                );
                Corner privatblock = new Corner(
                  c.getInt("privat-block.X"),
                  c.getInt("privat-block.Y"),
                  c.getInt("privat-block.Z")
                );
                String rgowner = c.getString("owner-name");
                List<String> plsmembs = c.getStringList("privat-members");
                List<PrivatMember> privatMembers = new ArrayList<>();
                for (String s : plsmembs){
                 String[] strings = s.split(":");
                 String playername = strings[0];
                 boolean isOwner = strings[1].equalsIgnoreCase("1");
                 boolean canBlockBreak = strings[2].equalsIgnoreCase("1");
                 boolean canChestOpen = strings[3].equalsIgnoreCase("1");
                 boolean canBlockPlace = strings[4].equalsIgnoreCase("1");
                 boolean canUseCharka = strings[5].equalsIgnoreCase("1");;
                 boolean canUseAnvil = strings[6].equalsIgnoreCase("1");;
                 boolean canUseDoors = strings[7].equalsIgnoreCase("1");;
                 boolean canUsePech = strings[8].equalsIgnoreCase("1");;
                 boolean canUseHopers = strings[9].equalsIgnoreCase("1");;
                 boolean canUseWorkBench = strings[10].equalsIgnoreCase("1");;
                 boolean canUseBed = strings[11].equalsIgnoreCase("1");;
                 boolean canUsePlity = strings[12].equalsIgnoreCase("1");;
                 privatMembers.add(new PrivatMember(playername, isOwner, canBlockBreak, canChestOpen, canBlockPlace,
                         canUseCharka, canUseAnvil, canUseDoors, canUsePech, canUseHopers,
                         canUseWorkBench, canUseBed, canUsePlity));
                }
                rglist.add(new WellRegion(id, privatname, corner1, corner2, rgowner, privatMembers, privatblock));
                i++;
            }
            Bukkit.getConsoleSender().sendMessage(Language.get(Langs.main__load_x_rgs).replace("{X}", Integer.toString(i)));
        } else {
            Bukkit.getConsoleSender().sendMessage(Language.get(Langs.main__rgs_empty));
        }
    }

    public static void loadRG(File t) {
        Config c = ConfigFactory.parseFile(t);
        Integer id = Integer.parseInt(t.getName().replace(".conf", ""));
        String privatname = c.getString("privat-name");
        Corner corner1 = new Corner(
                c.getInt("corners.first.X"),
                c.getInt("corners.first.Y"),
                c.getInt("corners.first.Z")
        );
        Corner corner2 = new Corner(
                c.getInt("corners.second.X"),
                c.getInt("corners.second.Y"),
                c.getInt("corners.second.Z")
        );
        Corner privatblock = new Corner(
                c.getInt("privat-block.X"),
                c.getInt("privat-block.Y"),
                c.getInt("privat-block.Z")
        );
        String rgowner = c.getString("owner-name");
        List<String> plsmembs = c.getStringList("privat-members");
        List<PrivatMember> privatMembers = new ArrayList<>();
        for (String s : plsmembs) {
            String[] strings = s.split(":");
            String playername = strings[0];
            boolean isOwner = strings[1].equalsIgnoreCase("1");
            boolean canBlockBreak = strings[2].equalsIgnoreCase("1");
            boolean canChestOpen = strings[3].equalsIgnoreCase("1");
            boolean canBlockPlace = strings[4].equalsIgnoreCase("1");
            boolean canUseCharka = strings[5].equalsIgnoreCase("1");
            boolean canUseAnvil = strings[6].equalsIgnoreCase("1");
            boolean canUseDoors = strings[7].equalsIgnoreCase("1");
            boolean canUsePech = strings[8].equalsIgnoreCase("1");
            boolean canUseHopers = strings[9].equalsIgnoreCase("1");
            boolean canUseWorkBench = strings[10].equalsIgnoreCase("1");
            boolean canUseBed = strings[11].equalsIgnoreCase("1");
            boolean canUsePlity = strings[12].equalsIgnoreCase("1");
            privatMembers.add(new PrivatMember(playername, isOwner, canBlockBreak, canChestOpen, canBlockPlace,
                    canUseCharka, canUseAnvil, canUseDoors, canUsePech, canUseHopers,
                    canUseWorkBench, canUseBed, canUsePlity));
        }
        rglist.add(new WellRegion(id, privatname, corner1, corner2, rgowner, privatMembers, privatblock));
    }

    public static PrivatMember loadPM(String str){
        PrivatMember pm = null;
            String[] strings = str.split(":");
            String playername = strings[0];
            boolean isOwner = strings[1].equalsIgnoreCase("1");
            boolean canBlockBreak = strings[2].equalsIgnoreCase("1");
            boolean canChestOpen = strings[3].equalsIgnoreCase("1");
            boolean canBlockPlace = strings[4].equalsIgnoreCase("1");
            boolean canUseCharka = strings[5].equalsIgnoreCase("1");
            boolean canUseAnvil = strings[6].equalsIgnoreCase("1");
            boolean canUseDoors = strings[7].equalsIgnoreCase("1");
            boolean canUsePech = strings[8].equalsIgnoreCase("1");
            boolean canUseHopers = strings[9].equalsIgnoreCase("1");
            boolean canUseWorkBench = strings[10].equalsIgnoreCase("1");
            boolean canUseBed = strings[11].equalsIgnoreCase("1");
            boolean canUsePlity = strings[12].equalsIgnoreCase("1");
            pm = new PrivatMember(playername, isOwner, canBlockBreak, canChestOpen, canBlockPlace,
                    canUseCharka, canUseAnvil, canUseDoors, canUsePech, canUseHopers,
                    canUseWorkBench, canUseBed, canUsePlity);
        return pm;
    }

    public static String loadPM(PrivatMember pm){
        String isOwner = (pm.isOwner()) ? "1" : "0";
        String canBlockBreak = (pm.canBlockBreak()) ? "1" : "0";
        String canChestOpen = (pm.canChestOpen()) ? "1" : "0";
        String canBlockPlace = (pm.canBlockPlace()) ? "1" : "0";
        String canUseCharka = (pm.canUseCharka()) ? "1" : "0";
        String canUseAnvil = (pm.canUseAnvil()) ? "1" : "0";
        String canUseDoors = (pm.canUseDoors()) ? "1" : "0";
        String canUsePech = (pm.canUsePech()) ? "1" : "0";
        String canUseHopers = (pm.canUseHopers()) ? "1" : "0";
        String canUseWorkBench = (pm.canUseWorkBench()) ? "1" : "0";
        String canUseBed = (pm.canUseBed()) ? "1" : "0";
        String canUsePlity = (pm.canUsePlity()) ? "1" : "0";

        StringBuilder result = new StringBuilder();

        result.append(pm.getPlayername());
        result.append(":").append(isOwner);
        result.append(":").append(canBlockBreak);
        result.append(":").append(canChestOpen);
        result.append(":").append(canBlockPlace);
        result.append(":").append(canUseCharka);
        result.append(":").append(canUseAnvil);
        result.append(":").append(canUseDoors);
        result.append(":").append(canUsePech);
        result.append(":").append(canUseHopers);
        result.append(":").append(canUseWorkBench);
        result.append(":").append(canUseBed);
        result.append(":").append(canUsePlity);

        //Bukkit.getConsoleSender().sendMessage(result.toString());
        return result.toString();
    }


    public static void removeRG(WellRegion wr){
        rglist.remove(wr);
    }


    private static Integer maxPrivats(Player p){
        int count = 0;
        for(PermissionAttachmentInfo perms : p.getEffectivePermissions()){
            String permst = perms.getPermission();
            if(permst.startsWith(Settings.getSettings().getString("privat-limit-perms-start"))){
                String[] permsplited = perms.getPermission().split("\\.");
                int mins = permsplited.length - 1;
                count = Integer.parseInt(permsplited[permsplited.length - 1]);
            }
        }
        return count;
    }

    public static List<WellRegion> getPlayerPrivats(String p){
        List<WellRegion> wrs = new ArrayList<>();
        for(WellRegion region : getRglist()){
            if(region.getRgowner().equalsIgnoreCase(p)){
                wrs.add(region);
            }
        }
        return wrs;
    }

    public static List<WellRegion> getPlayerPrivatsWhereMember(String p){
        List<WellRegion> wrs = new ArrayList<>();
        for(WellRegion region : getRglist()){
            for(PrivatMember pm : region.getPlayerMembers()){
                if(pm.getPlayername().equalsIgnoreCase(p)){
                    wrs.add(region);
                }
            }
        }
        return wrs;
    }

    public static List<WellRegion> getRglist() {
        return rglist;
    }

    public static WellRegion getByID(String id){
        for(WellRegion wr : getRglist()){
            String s = Integer.toString(wr.getId());
            if(s.equalsIgnoreCase(id)){
                return wr;
            }
        }
        return null;
    }

    public static boolean isPrivateBlock(Block block){
        for(WellRegion wr : getRglist()){
            int blockx = block.getX();
            int blocky = block.getY();
            int blockz = block.getZ();
            int wrx = wr.getPrivatblock().getX();
            int wry = wr.getPrivatblock().getY();
            int wrz = wr.getPrivatblock().getZ();
            if(blockx == wrx && blocky == wry && blockz == wrz){
                return true;
            }
        }
        return false;
    }
}
