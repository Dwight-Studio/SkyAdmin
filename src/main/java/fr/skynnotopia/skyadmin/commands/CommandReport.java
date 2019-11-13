package fr.skynnotopia.skyadmin.commands;

import fr.skynnotopia.skyadmin.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import static fr.skynnotopia.skyadmin.Main.messagePrefix;

public class CommandReport implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender csender, Command command, String label, String[] args) {
        // Seul les joueurs peuvent executer cette commande
        if (!(csender instanceof Player)) {
            return false;
        // Seul les joueurs qui se sont connecté peuvent être report
        } else if (!(Bukkit.getServer().getOnlinePlayers().contains(Bukkit.getServer().getPlayer(args[0])) || Bukkit.getServer().getOnlinePlayers().contains(Bukkit.getServer().getPlayer(args[0])))) {
            csender.sendMessage(messagePrefix + "Ce joueur n'existe pas.");
        } else {
            // On est sûr que c'est un joueur --> On remplace CommandSender par Player
            Player sender = (Player) csender;
            Player player = Bukkit.getServer().getPlayer(args[0]);
            // On convertit la liste en String à partir de l'argument 1
            StringBuilder message = new StringBuilder();
            for (int i = 1; i != args.length; i++) {
                message.append(args[i]).append(" ");
            }
            String lastReportId = "";
            try {
                lastReportId = Integer.toString(Config.getInt("lastReportId")+1);
                Config.set("lastReportId", Config.getInt("lastReportId")+1);
                Config.set("reports." + lastReportId + ".sender", sender.getUniqueId().toString());
                Config.set("reports." + lastReportId + ".player", player.getUniqueId().toString());
                Config.set("reports." + lastReportId + ".date", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
                Config.set("reports." + lastReportId + ".reason", message.toString());
                Config.set("reports." + lastReportId + ".assigned", false);
                Config.set("reports." + lastReportId + ".assignedTo", "");
                Config.set("reports." + lastReportId + ".closed", false);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.DARK_GREEN + "[SkyAdmin] " + ChatColor.RED + "Une erreur est survenue.");
                Bukkit.getLogger().log(Level.SEVERE,e.toString());
            }

            sender.sendMessage(messagePrefix + " Signalement n°" + lastReportId+1);
            sender.sendMessage("Joueur signalé : " + ChatColor.AQUA + player.getDisplayName());
            sender.sendMessage("Raison : " + ChatColor.AQUA + message.toString());
            sender.sendMessage("État du signalement : " + ChatColor.AQUA + "Non assigné");
            sender.sendMessage(messagePrefix + " Signalement n°" + lastReportId+1);
        }
        return false;
    }
}
