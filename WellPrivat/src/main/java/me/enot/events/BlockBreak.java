package me.enot.events;

import me.enot.configurations.Settings;
import me.enot.configurations.lang.Langs;
import me.enot.configurations.lang.Language;
import me.enot.gui.ConfirmGUI;
import me.enot.privat.Builder;
import me.enot.privat.PrivatMember;
import me.enot.privat.WellRegion;
import me.enot.privat.logic.PrivateSetting;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BlockBreak implements Listener {


    private static HashMap<Player, WellRegion> confirm = new HashMap<>();
    private static HashMap<Player, ItemStack> confblock = new HashMap<>();
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(BlockPlace.getTap().containsKey(e.getPlayer())){
            e.setCancelled(true);
            return;
        }
        Player p = e.getPlayer();
        Block block = e.getBlock();
        ItemStack stack = new ItemStack(block.getType());
        WellRegion wr = PrivateSetting.inPrivat(block.getLocation());
        if(wr != null){
            if(Settings.getRgsblocks().containsKey(block.getType()) && Builder.isPrivateBlock(block)){
                if(wr.getRgowner().equalsIgnoreCase(p.getName())){
                    if(!confirm.containsKey(p)) {
                        if(!confblock.containsKey(p)) {
                            confirm.put(p, wr);
                            confblock.put(p, stack);
                            ConfirmGUI.open(p, wr);
                        } else {
                            ConfirmGUI.open(p, wr);
                        }
                    } else {
                      ConfirmGUI.open(p, wr);
                    }
                } else {
                    p.sendMessage(Language.get(Langs.rgs__player_not_owner_break_private_block).replace("{NAME}", wr.getRgowner()));
                }
                e.setCancelled(true);
            } else {
                boolean inprivat = false;
                if (!wr.getRgowner().equalsIgnoreCase(p.getName())) {
                    for (PrivatMember pm : wr.getPlayerMembers()) {
                        if (pm.getPlayername().equalsIgnoreCase(p.getName())) {
                            if (pm.canBlockBreak()) {
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

    public static HashMap<Player, WellRegion> getConfirm() {
        return confirm;
    }

    public static HashMap<Player, ItemStack> getConfblock() {
        return confblock;
    }

}
