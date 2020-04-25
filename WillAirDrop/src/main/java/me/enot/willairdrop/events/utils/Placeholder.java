package me.enot.willairdrop.events.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.enot.willairdrop.WillAirDrop;
import me.enot.willairdrop.configs.language.Langs;
import me.enot.willairdrop.configs.language.Language;
import me.enot.willairdrop.configs.language.Replace;
import me.enot.willairdrop.events.DropEvents;
import me.enot.willairdrop.logic.AirDrop;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class Placeholder extends PlaceholderExpansion {

    private Plugin plugin;

    public Placeholder(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }
    @Override
    public String getIdentifier(){
        return "willairdrop";
    }

    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        if(player == null){
            return "";
        }

        if(identifier.equals("text")) {
            for (Map.Entry<AirDrop, Counter> entry : DropEvents.getCounterMap().entrySet()) {
                if (entry.getValue().getPlayer().getName().equalsIgnoreCase(player.getName())) {
                    Counter counter = entry.getValue();
                    StringBuilder sb = new StringBuilder();
                    if (counter != null) {
                        for(String s : Language.getMessage(Langs.hologram__air_drop_counter,
                                new Replace("{X}", Integer.toString(counter.getSeconds())))){
                            sb.append(s);
                        }
                    } else {
                        for(String s : Language.getMessage(Langs.hologram__airdrop)){
                            sb.append(s);
                        };
                    }
                    return sb.toString();
                }
            }
        }
        return null;
    }


}
