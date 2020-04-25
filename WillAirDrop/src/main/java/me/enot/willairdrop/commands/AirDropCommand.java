package me.enot.willairdrop.commands;

import me.enot.willairdrop.configs.Settings;
import me.enot.willairdrop.configs.language.Langs;
import me.enot.willairdrop.configs.language.Language;
import me.enot.willairdrop.configs.language.Replace;
import me.enot.willairdrop.logic.AirDrop;
import me.enot.willairdrop.logic.utils.AirDropLogick;
import me.enot.willairdrop.logic.utils.Calculations;
import me.enot.willairdrop.serializer.Loot;
import me.enot.willairdrop.serializer.Serialize;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AirDropCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("airdrop")){
            if(args.length > 0){
                if(args[0].equalsIgnoreCase("reload")){
                    if(s.hasPermission(Settings.getReloadPermission())){
                        Settings.reload();
                        Language.reload();
                        AirDropLogick.formatedTimeList();
                        Language.sendMessage(s, Langs.main__reload);
                    } else {
                        Language.sendMessage(s, Langs.main__hasnt_permissions);
                    }
                } else {
                    if(args[0].equalsIgnoreCase("start")){
                        if(s.hasPermission(Settings.getStartPermission())){
                            if(args.length >= 5){
                                String lootname = args[1];
                                String coordinateX = args[2];
                                String coordinateZ = args[3];
                                int time = 0;
                                for(int i = 4; i < args.length; i++) {
                                    Integer temptime = Calculations.toSeconds(args[i]);
                                    if (temptime != null) {
                                        time += temptime;
                                    } else {
                                        Language.sendMessage(s, Langs.crate__admin_time_not_write);
                                        return false;
                                    }
                                }
                                try {
                                    Integer x = Integer.parseInt(coordinateX);
                                    Integer z = Integer.parseInt(coordinateZ);
                                    Loot l = Serialize.findByName(lootname);
                                    if (l != null) {
                                        AirDrop drop = new AirDrop(l, Calculations.generateLocation(x, z));

                                        StringBuilder name = new StringBuilder();
                                        if(s instanceof Player){
                                            name.append(((Player)s).getName());
                                        } else {
                                            for (String ssss :Language.getMessage(Langs.crate__console_name)){
                                                name.append(ssss);
                                            }
                                        }
                                        drop.call(time, name.toString());
                                        Language.sendMessage(s, Langs.crate__admin_loot_start,
                                                new Replace("{TIME}", Calculations.seccondsToString(time)),
                                                new Replace("{NAME}", l.getLootName()),
                                                new Replace("{X}", Integer.toString(x)),
                                                new Replace("{Z}", Integer.toString(z)));
                                    } else {
                                        Language.sendMessage(s, Langs.crate__admin_loot_not_find,
                                                new Replace("{NAME}", lootname));
                                    }
                                } catch (NumberFormatException e) {
                                    Language.sendMessage(s, Langs.crate__admin_coords_not_write);
                                }

                            } else {
                                Language.sendMessage(s, Langs.crate__admin_start_usage);
                            }
                        } else {
                            Language.sendMessage(s, Langs.main__hasnt_permissions);
                        }
                    }
                }
            }
        }
        return false;
    }
}
