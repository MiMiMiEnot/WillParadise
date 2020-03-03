package me.enot.events;

import me.enot.configurations.lang.Langs;
import me.enot.configurations.lang.Language;
import me.enot.privat.WellRegion;
import me.enot.privat.logic.PrivateSetting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class PlayerMove implements Listener {

    private static HashMap<Player, WellRegion> inPrivat = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        WellRegion wr = PrivateSetting.inPrivat(p.getLocation());
        if(inPrivat.containsKey(p)){
            if(wr == null){
                String privatname = inPrivat.get(p).getPrivatname();
                inPrivat.remove(p);
                p.sendMessage(Language.get(Langs.rgs__player_come_out_rg).replace("{NAME}", Language.toString(privatname)));
            }
        } else {
            if(wr != null){
                inPrivat.put(p, wr);
                String privatname = wr.getPrivatname();
                p.sendMessage(Language.get(Langs.rgs__player_come_in_rg).replace("{NAME}", Language.toString(privatname)));
            }
        }
    }

}
