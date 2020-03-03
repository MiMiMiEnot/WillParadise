package me.enot.events;

import me.enot.configurations.Settings;
import me.enot.configurations.lang.Langs;
import me.enot.configurations.lang.Language;
import me.enot.gui.PrivatePlayerList;
import me.enot.gui.listener.InventoryEvent;
import me.enot.privat.Builder;
import me.enot.privat.PrivatMember;
import me.enot.privat.WellRegion;
import me.enot.privat.logic.PrivateSetting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BlockPlace implements Listener {

    private static HashMap<Player, Block> tap = new HashMap<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        Material blockmaterial = block.getType();
        if (Settings.getRgsblocks().containsKey(blockmaterial)) {
            String worldname = p.getWorld().getName();
            if (Settings.getAllowedWorlds().contains(worldname)) {
                if (!tap.containsKey(p)) {
                    if (Builder.canCreateRG(block, p)) {
                        if (!PrivateSetting.crossedRG(block)) {
                            tap.put(p, block);
                            p.sendTitle(Language.get(Langs.rgs__privat_create_title), Language.get(Langs.rgs__privat_create_subtitle), 1, 60, 1);
                        } else {
                            p.sendMessage(Language.get(Langs.rgs__block_at_privat));
                            e.setCancelled(true);
                        }
                    } else {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }
            } else {
                p.sendMessage(Language.get(Langs.rgs__not_rg_world));
            }
        } else {
            WellRegion wr = PrivateSetting.inPrivat(block.getLocation());
            if (wr != null) {
                boolean inprivat = false;
                if (!wr.getRgowner().equalsIgnoreCase(p.getName())) {
                    for (PrivatMember pm : wr.getPlayerMembers()) {
                        if (pm.getPlayername().equalsIgnoreCase(p.getName())) {
                            if (pm.canBlockPlace()) {
                                inprivat = true;
                            }
                        }
                    }
                    if (!inprivat) {
                        e.setCancelled(true);
                        p.sendMessage(Language.get(Langs.rgs__player_not_in_privat).replace("{NAME}", Language.toString(wr.getPrivatname())));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if(tap.containsKey(e.getPlayer())){
            String chat = e.getMessage();
            Player p = e.getPlayer();
            int max = Settings.getSettings().getInt("privat-name-max-chars");
            int min = Settings.getSettings().getInt("privat-name-min-chars");

            if(chat.length() <= max){
                if(chat.length() >= min) {
                    if (isValid(chat)) {
                        Block b = tap.get(p);
                        tap.remove(p);
                        PrivateSetting.createRG(b, p, Settings.getColor() + chat);
                        p.sendMessage(Language.get(Langs.rgs__privat_created).replace("{NAME}", Language.toString(chat)));
                        e.setCancelled(true);
                    } else {
                        p.sendMessage(Language.get(Langs.rgs__privat_name_contains_bed_chats));
                    }
                } else {
                    p.sendMessage(Language.get(Langs.rgs__chat_to_small)
                            .replace("{X}", Integer.toString(chat.length()))
                            .replace("{Y}", Integer.toString(min)));
                    e.setCancelled(true);
                }
            } else {
                p.sendMessage(Language.get(Langs.rgs__chat_to_big)
                        .replace("{X}", Integer.toString(chat.length()))
                        .replace("{Y}", Integer.toString(max)));
                e.setCancelled(true);
            }
        } else if(InventoryEvent.addlist.containsKey(e.getPlayer())){
            String chat = e.getMessage();
            String allowed = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789_";
            List<String> list = Arrays.asList(allowed.split(""));

            for(String s : chat.split("")){
                if(!list.contains(s)){
                    e.getPlayer().sendMessage(Language.get(Langs.rgs__privat_player_add_uncorrect));
                    e.setCancelled(true);
                    return;
                }
            }
            Player p = e.getPlayer();
            WellRegion wr = PrivatePlayerList.adds.get(p);
            for(PrivatMember pm : wr.getPlayerMembers()){
                if(pm.getPlayername().equalsIgnoreCase(chat)){
                    p.sendMessage(Language.get(Langs.rgs__privat_player_use_alredy_in_privat).replace("{PLAYER}", chat));
                    e.setCancelled(true);
                    return;
                }
            }
            PrivateSetting.pmadd(wr, chat);
            InventoryEvent.addlist.remove(p);
            p.sendMessage(Language.get(Langs.rgs__privat_player_add).replace("{PLAYER}", chat)
                    .replace("{PRIVAT-NAME}", Language.toString(wr.getPrivatname())));
            e.setCancelled(true);
        }
    }

    private static boolean isValid(String s){
        for(String str : Settings.getBlockedsymbols()){
            if(s.contains(str)){
                return false;
            }
        }
        return true;
    }

    public static HashMap<Player, Block> getTap() {
        return tap;
    }
}
