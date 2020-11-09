package fr.dwightstudio.skyadmin.commands;

import fr.dwightstudio.skyadmin.Warn;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

import static fr.dwightstudio.skyadmin.Main.messagePrefix;

public class CommandWarn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender csender, Command command, String label, String[] args) {
        if (args.length < 2) {
            return false;
        }
        // Seul les joueurs peuvent executer cette commande
        if (!(csender instanceof Player)) {
            return false;
        }
        // On est sûr que c'est un joueur --> On remplace CommandSender par Player
        Player sender = (Player) csender;
        // Seul les joueurs qui se sont connecté peuvent être report
        if (Bukkit.getServer().getPlayer(args[0]) == null) {
            csender.sendMessage(messagePrefix + ChatColor.RED + "Ce joueur n'existe pas.");
            return true;
        }

        Player player = Bukkit.getServer().getPlayer(args[0]);
        // On convertit la liste en String à partir de l'argument 1
        StringBuilder message = new StringBuilder();
        for (int i = 1; i != args.length; i++) {
            message.append(args[i]).append(" ");
        }

        Warn warn = new Warn(sender, player, message.toString());
        Bukkit.getServer().broadcastMessage(messagePrefix + ChatColor.RED + "-- Avertissement n°" + warn.getId() + " --");
        Bukkit.getServer().broadcastMessage(ChatColor.RED + "Joueur averti : " + ChatColor.DARK_PURPLE + player.getDisplayName());
        Bukkit.getServer().broadcastMessage(ChatColor.RED + "Raison : " + ChatColor.DARK_PURPLE + message.toString());

        switch(Warn.getPlayerWarns(player).size()) {
            default:
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "Sanction : " + ChatColor.DARK_PURPLE + "Aucune.");
                break;
            case 3:
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "Sanction : " + ChatColor.DARK_PURPLE + "(Palier 1) Banni(e) pendant 30 heures.");
                Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getName(),"Application de la sanction de palier 1 : Banni(e) pendant 30 minutes.", new Date(System.currentTimeMillis()+30*60*1000),sender.getName());
                player.kickPlayer("Application de la sanction de palier 1 : Banni(e) pendant 30 minutes.");
                break;
            case 6:
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "Sanction : " + ChatColor.DARK_PURPLE + "(Palier 2) Banni(e) pendant 2 heures.");
                Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getName(),"Application de la sanction de palier 2 : Banni(e) pendant 2 heures.", new Date(System.currentTimeMillis()+2*60*60*1000),sender.getName());
                player.kickPlayer("Application de la sanction de palier 2 : Banni(e) pendant 2 heures.");
                break;
            case 9:
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "Sanction : " + ChatColor.DARK_PURPLE + "(Palier 3) Banni(e) pendant 24 heures.");
                Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getName(),"Application de la sanction de palier 3 : Banni(e) pendant 24 heures.", new Date(System.currentTimeMillis()+24*60*60*1000),sender.getName());
                player.kickPlayer("Application de la sanction de palier 3 : Banni(e) pendant 24 heures.");
                break;
            case 12:
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "Sanction : " + ChatColor.DARK_PURPLE + "(Palier 4) Banni(e) pendant 7 jours.");
                Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getName(),"Application de la sanction de palier 4 : Banni(e) pendant 7 jours.", new Date(System.currentTimeMillis()+7*24*60*60*1000),sender.getName());
                player.kickPlayer("Application de la sanction de palier 4 : Banni(e) pendant 7 jours.");
                break;
            case 15:
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "Sanction : " + ChatColor.DARK_PURPLE + "(Palier 5) Banni(e) pendant 30 jours.");
                Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(player.getName(),"Application de la sanction de palier 5 : Banni(e) pendant 30 jours.", new Date(System.currentTimeMillis()+30*24*60*60*1000),sender.getName());
                player.kickPlayer("Application de la sanction de palier 5 : Banni(e) pendant 30 jours.");
                break;
        }

        Bukkit.getServer().broadcastMessage(messagePrefix + ChatColor.RED + "-- Avertissement n°" + warn.getId() + " --");
        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            pl.playSound(pl.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
        }
        return true;
    }
}