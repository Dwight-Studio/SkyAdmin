package fr.dwightstudio.skyadmin.commands;

import fr.dwightstudio.skyadmin.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

import static fr.dwightstudio.skyadmin.Main.logPrefix;
import static fr.dwightstudio.skyadmin.Main.messagePrefix;

public class CommandSkyAdmin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        } else if (args[0].equalsIgnoreCase("test")) {
            sender.sendMessage(messagePrefix + "Le plugin est en fonctionnement !");
            return true;
        } else if (args[0].equalsIgnoreCase("reload")){
            sender.sendMessage(messagePrefix + "Rechargement en cours...");
            Bukkit.getLogger().log(Level.INFO,logPrefix + "Reloading all configs...");
            if (Config.reload()) {
                sender.sendMessage(messagePrefix + "Fait.");
                Bukkit.getLogger().log(Level.INFO,"Done.");
            } else {
                sender.sendMessage(messagePrefix + ChatColor.WHITE + "Une erreur est survenue.");
                Bukkit.getLogger().log(Level.SEVERE,"Error !");
            }
            return true;
        } else {
            return false;
        }
    }
}
