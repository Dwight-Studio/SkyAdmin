package fr.skynnotopia.skyadmin.commands;

import fr.skynnotopia.skyadmin.Config;
import fr.skynnotopia.skyadmin.Report;
import fr.skynnotopia.skyadmin.exceptions.ReportNotInitiatedException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static fr.skynnotopia.skyadmin.Main.logPrefix;
import static fr.skynnotopia.skyadmin.Main.messagePrefix;
import static fr.skynnotopia.skyadmin.Report.*;

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
        Player sender = (Player) csender;
        // Seul les joueurs qui se sont connecté peuvent être report
        if (!(Bukkit.getServer().getOnlinePlayers().contains(Bukkit.getServer().getPlayer(args[0])) || Bukkit.getServer().getOnlinePlayers().contains(Bukkit.getServer().getPlayer(args[0])))) {
            csender.sendMessage(messagePrefix + "Ce joueur n'existe pas.");
            // Vérifier que le joueur n'est pas utiliser trop de fois la commande
        } else if (getPlayerActiveSentReportsCount(sender) > 3) {
            sender.sendMessage(messagePrefix + ChatColor.RED + "Vous avez dépasser la limite de signalements actifs.");
        } else {
            // On est sûr que c'est un joueur --> On remplace CommandSender par Player
            Player player = Bukkit.getServer().getPlayer(args[0]);
            // On convertit la liste en String à partir de l'argument 1
            StringBuilder message = new StringBuilder();
            for (int i = 1; i != args.length; i++) {
                message.append(args[i]).append(" ");
            }

            Report report = new Report(0);
            report.newReport(sender, player, message.toString());

            try {
                sender.sendMessage(messagePrefix + "-- Signalement n°" + report.getId() + " --");
                sender.sendMessage("Joueur signalé : " + ChatColor.AQUA + player.getDisplayName());
                sender.sendMessage("Raison : " + ChatColor.AQUA + message.toString());
                sender.sendMessage("État du signalement : " + ChatColor.AQUA + "Non assigné");
                sender.sendMessage(messagePrefix + "-- Signalement n°" + report.getId() + " --");
                sender.playSound(sender.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                return true;
            } catch (ReportNotInitiatedException e) {
                Bukkit.getLogger().log(Level.SEVERE,logPrefix + e.toString());
            }
        }
        return false;
    }
}
