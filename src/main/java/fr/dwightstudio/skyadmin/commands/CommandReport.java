package fr.dwightstudio.skyadmin.commands;

import fr.dwightstudio.skyadmin.Report;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.dwightstudio.skyadmin.Main.messagePrefix;

public class CommandReport implements CommandExecutor {
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
        // Vérifier que le joueur n'est pas utiliser trop de fois la commande
        if (Report.getPlayerActiveSentReports(sender).size() >= 3) {
            sender.sendMessage(messagePrefix + ChatColor.RED + "Vous avez atteint la quantité maximale de signalements actifs.");
            return true;
        }
        Player player = Bukkit.getServer().getPlayer(args[0]);
        // On convertit la liste en String à partir de l'argument 1
        StringBuilder message = new StringBuilder();
        for (int i = 1; i != args.length; i++) {
            message.append(args[i]).append(" ");
        }

        Report report = new Report(sender, player, message.toString());
        sender.sendMessage(messagePrefix + "-- Signalement n°" + report.getId() + " --");
        sender.sendMessage("Émetteur : " + org.bukkit.ChatColor.AQUA + sender.getDisplayName());
        sender.sendMessage("Joueur signalé : " + ChatColor.AQUA + player.getDisplayName());
        sender.sendMessage("Raison : " + ChatColor.AQUA + message.toString());
        sender.sendMessage("État du signalement : " + ChatColor.AQUA + "Non assigné");
        sender.sendMessage(messagePrefix + "-- Signalement n°" + report.getId() + " --");
        sender.playSound(sender.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
        CommandReports.notifyReport();
        return true;
    }
}
