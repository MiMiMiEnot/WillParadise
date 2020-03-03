package me.enot.commands;

import me.enot.configurations.Settings;
import me.enot.configurations.lang.Langs;
import me.enot.configurations.lang.Language;
import me.enot.gui.PrivateList;
import me.enot.gui.PrivatePlayerList;
import me.enot.privat.Builder;
import me.enot.privat.WellRegion;
import me.enot.privat.logic.PrivateSetting;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RGcmd implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("rg")){
            if(s instanceof Player) {
                Player p = (Player) s;
                if (args.length >= 1 && args.length <= 2) {
                    if (args[0].equalsIgnoreCase("info")) {
                        if (p.hasPermission(Settings.getSettings().getString("rg-info-perms"))) {
                            if (args.length > 1) {
                                String id = args[1];
                                WellRegion wr = Builder.getByID(id);
                                if(wr == null){
                                    p.sendMessage(Language.get(Langs.rgs__net_rg_with_id).replace("{ID}", id));
                                } else {
                                    PrivatePlayerList.open(wr, p);
                                }
                            } else {
                                WellRegion wr = PrivateSetting.inPrivat(p.getLocation());
                                if (wr != null) {
                                    PrivatePlayerList.open(wr, p);
                                } else {
                                    p.sendMessage(Language.get(Langs.rgs__not_in_rg));
                                }
                            }
                        } else {
                            PrivateList.open(p);
                        }
                    } else if(args[0].equalsIgnoreCase("list")) {
                        if (p.hasPermission(Settings.getSettings().getString("rg-list-perms"))) {
                            if (args.length > 1) {
                                String playername = args[1];
                                PrivateList.open(playername, p);
                            } else {
                                p.sendMessage(Language.get(Langs.rgs__rg_command_use));
                            }
                        } else {
                            PrivateList.open(p);
                        }
                    } else if(args[0].equalsIgnoreCase("reload")) {
                        if(p.hasPermission(Settings.getSettings().getString("reload-perm"))) {
                            s.sendMessage(Language.get(Langs.main__reload));
                            Settings.reload();
                            Language.reload();
                        } else {
                            PrivateList.open(p);
                        }
                    } else {
                        PrivateList.open(p);
                    }
                } else {
                    PrivateList.open(p);
                }
            }
        }
        return false;
    }
}
