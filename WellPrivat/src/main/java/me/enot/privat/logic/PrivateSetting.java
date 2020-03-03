package me.enot.privat.logic;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueFactory;
import me.enot.configurations.Settings;
import me.enot.privat.Builder;
import me.enot.privat.Corner;
import me.enot.privat.PrivatMember;
import me.enot.privat.WellRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class PrivateSetting {

    public static WellRegion inPrivat(Location loc){
        return inPrivat(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static WellRegion inPrivat(int lcx, int lcy, int lcz){
        for(WellRegion wr : Builder.getRglist()){
            int corner1x = wr.getCorner1().getX();
            int corner1y = wr.getCorner1().getY();
            int corner1z = wr.getCorner1().getZ();
            int corner2x = wr.getCorner2().getX();
            int corner2y = wr.getCorner2().getY();
            int corner2z = wr.getCorner2().getZ();

            int maxX = Math.max(corner1x, corner2x);
            int minX = Math.min(corner1x, corner2x);
            int maxY = Math.max(corner1y, corner2y);
            int minY = Math.min(corner1y, corner2y);
            int maxZ = Math.max(corner1z, corner2z);
            int minZ = Math.min(corner1z, corner2z);

            if(minX <= lcx &&  lcx <= maxX){
                if(minY <= lcy && lcy <= maxY){
                    if(minZ <= lcz && lcz <= maxZ){
                        return wr;
                    }
                }
            }
        }
        return null;
    }

    public static boolean crossedRG(Block block){
        Integer radius = Settings.getRgsblocks().get(block.getType());

        int blockx = block.getX();
        int blocky = block.getY();
        int blockz = block.getZ();

        int x1 = blockx + radius;
        int x2 = blockx - radius;

        int y1 = blocky + radius;
        int y2 = blocky - radius;

        int z1 = blockz + radius;
        int z2 = blockz - radius;

        if(inPrivat(x1, y1, z1) != null){
            return true;
        }
        if(inPrivat(x1, y2, z2) != null){
            return true;
        }
        if(inPrivat(x1, y2, z1) != null) {
            return true;
        }
        if(inPrivat(x1, y1, z2) != null){
            return true;
        }
        if(inPrivat(x2, y1, z1) != null){
            return true;
        }
        if(inPrivat(x2, y2, z2) != null){
            return true;
        }
        if(inPrivat(x2, y1, z2) != null){
            return true;
        }
        if(inPrivat(x2, y2, z1) != null){
            return true;
        }

        if(Settings.getSettings().getBoolean("test")) {
            Location l1 = new Location(block.getWorld(), x1, y1, z1);
            Location l2 = new Location(block.getWorld(), x1, y2, z2);
            Location l3 = new Location(block.getWorld(), x1, y2, z1);
            Location l4 = new Location(block.getWorld(), x1, y1, z2);
            Location l5 = new Location(block.getWorld(), x2, y1, z1);
            Location l6 = new Location(block.getWorld(), x2, y2, z2);
            Location l7 = new Location(block.getWorld(), x2, y1, z2);
            Location l8 = new Location(block.getWorld(), x2, y2, z1);

            l1.getBlock().setType(Material.WOOL);
            l2.getBlock().setType(Material.WOOL);
            l3.getBlock().setType(Material.WOOL);
            l4.getBlock().setType(Material.WOOL);
            l5.getBlock().setType(Material.WOOL);
            l6.getBlock().setType(Material.WOOL);
            l7.getBlock().setType(Material.WOOL);
            l8.getBlock().setType(Material.WOOL);
        }
        return false;
    }

    private static List<Corner> createCorners(Block block){
        Integer radius = Settings.getRgsblocks().get(block.getType());
        if(radius == null){
            return null;
        }
        int blockx = block.getX();
        int blocky = block.getY();
        int blockz = block.getZ();

        int npm1X = blockx - radius;
        int npm1Y = blocky - radius;
        int npm1Z = blockz - radius;
        int npm2X = blockx + radius;
        int npm2Y = blocky + radius;
        int npm2Z = blockz + radius;

        if(npm1Y < 0) {
            npm1Y = 0;
        }
        if(npm2Y < 0) {
            npm1Y = 0;
        }

        Corner corner1 = new Corner(npm1X, npm1Y, npm1Z);
        Corner corner2 = new Corner(npm2X, npm2Y, npm2Z);

        List<Corner> list = new ArrayList<>();
        list.add(corner1);
        list.add(corner2);
        return list;
    }


    public static void createRG(Block block, Player player, String privatname){
        List<Corner> list = createCorners(block);
        ConfigCreating.create(privatname, list.get(0), list.get(1), player.getName().toLowerCase(), block);
    }

    public static boolean removeRG(WellRegion wr){
        Builder.removeRG(wr);
        Location loc = new Location(Bukkit.getWorld("world"), wr.getPrivatblock().getX(), wr.getPrivatblock().getY(), wr.getPrivatblock().getZ());
        loc.getBlock().setType(Material.AIR);
        return ConfigDeleting.delete(wr.getId());
    }

    public static void removePM(WellRegion wr, PrivatMember pm){
        List<PrivatMember> list = wr.getPlayerMembers();
        int id = wr.getId();
        File cfile = new File(Settings.rgdir, id + ".conf");
        Config c = ConfigFactory.parseFile(cfile);
        List<String> ls = new ArrayList<>();

        int i = 0;
        while (i < list.size()) {
            PrivatMember member = list.get(i);
            StringBuilder result = new StringBuilder();
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
            ls.add(result.toString());
            i++;
        }
        list.remove(pm);
        wr.setPlayermembers(list);

        c = c.withValue("privat-members", ConfigValueFactory.fromAnyRef(ls));

        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(cfile.toPath(), c.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public static void changePerm(WellRegion wr, PrivatMember after){
        int id = wr.getId();
        File cfile = new File(Settings.rgdir, + id + ".conf");
        Config c = ConfigFactory.parseFile(cfile);

        List<PrivatMember> pmlist = wr.getPlayerMembers();
        //Bukkit.getConsoleSender().sendMessage(pmlist.toString());
        //Bukkit.getConsoleSender().sendMessage("Начинаю поиск " + wr.getPrivatname() + " игрок " + after.getPlayername());
        for(int i = 0; i < pmlist.size(); i++){
            if(after.getPlayername().equalsIgnoreCase(pmlist.get(i).getPlayername())){
                pmlist.set(i, after);
            }
        }
        //Bukkit.getConsoleSender().sendMessage(pmlist.toString());
        wr.setPlayermembers(pmlist);
        List<String> toconfiglist = new ArrayList<>();
        for(PrivatMember member : pmlist){
            String s = Builder.loadPM(member);
            toconfiglist.add(s);
        }

        c = c.withValue("privat-members", ConfigValueFactory.fromAnyRef(toconfiglist));
        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(cfile.toPath(), c.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void pmadd(WellRegion wr, String name){
        boolean isOwner = false;
        boolean canBlockBreak = Settings.getSettings().getBoolean("default-perms-for-users.canBlockBreak");
        boolean canChestOpen = Settings.getSettings().getBoolean("default-perms-for-users.canChestOpen");
        boolean canBlockPlace = Settings.getSettings().getBoolean("default-perms-for-users.canBlockPlace");
        boolean canUseCharka = Settings.getSettings().getBoolean("default-perms-for-users.canUseCharka");
        boolean canUseAnvil = Settings.getSettings().getBoolean("default-perms-for-users.canUseAnvil");
        boolean canUseDoors = Settings.getSettings().getBoolean("default-perms-for-users.canUseDoors");
        boolean canUsePech = Settings.getSettings().getBoolean("default-perms-for-users.canUsePech");
        boolean canUseHopers = Settings.getSettings().getBoolean("default-perms-for-users.canUseHopers");
        boolean canUseWorkBench = Settings.getSettings().getBoolean("default-perms-for-users.canUseWorkBench");
        boolean canUseBed = Settings.getSettings().getBoolean("default-perms-for-users.canUseBed");
        boolean canUsePlity = Settings.getSettings().getBoolean("default-perms-for-users.canUsePlity");

        int id = wr.getId();
        File cfile = new File(Settings.rgdir, + id + ".conf");
        Config c = ConfigFactory.parseFile(cfile);
        List<String> k = c.getStringList("privat-members");
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(":").append(isOwner);
        sb.append(":").append(canBlockBreak);
        sb.append(":").append(canChestOpen);
        sb.append(":").append(canBlockPlace);
        sb.append(":").append(canUseCharka);
        sb.append(":").append(canUseAnvil);
        sb.append(":").append(canUseDoors);
        sb.append(":").append(canUsePech);
        sb.append(":").append(canUseHopers);
        sb.append(":").append(canUseWorkBench);
        sb.append(":").append(canUseBed);
        sb.append(":").append(canUsePlity);
        List<PrivatMember> lists = wr.getPlayerMembers();
        String sbs = sb.toString().replaceAll("true", Integer.toString(1)).replaceAll("false", Integer.toString(0));
        //Bukkit.getConsoleSender().sendMessage(sbs);
        lists.add(Builder.loadPM(sbs));
        wr.setPlayermembers(lists);
        k.add(sbs);
        c = c.withValue("privat-members", ConfigValueFactory.fromAnyRef(k));
        ConfigRenderOptions cro = ConfigRenderOptions.defaults().setJson(false).setOriginComments(false);
        try {
            Files.write(cfile.toPath(), c.root().render(cro).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static PrivatMember isMember(String nick, WellRegion wr){
        for(PrivatMember pm : wr.getPlayerMembers()){
            if(pm.getPlayername().equalsIgnoreCase(nick)){
                return pm;
            }
        }
        return null;
    }

    public static PrivatMember getByNick(WellRegion wr, String nick){
        for(PrivatMember member : wr.getPlayerMembers()){
            if(member.getPlayername().equalsIgnoreCase(nick)){
                return member;
            }
        }
        return null;
    }

}
