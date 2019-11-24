package fr.skynnotopia.skyadmin.commands;

import com.sun.imageio.plugins.wbmp.WBMPImageReader;
import fr.skynnotopia.skyadmin.Report;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.skynnotopia.skyadmin.Main.messagePrefix;
import static fr.skynnotopia.skyadmin.Report.getAssignedReports;
import static fr.skynnotopia.skyadmin.Report.getUnassignedReports;

public class CommandReports implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender csender, Command command, String label, String[] args) {

        // Seul les joueurs peuvent executer cette commande
        if (!(csender instanceof Player)) {
            return false;
        }

        if (args.length == 1) {
            return false;
        }

        if (args.length > 3) {
            return false;
        }

        // On est sûr que c'est un joueur --> On remplace CommandSender par Player
        Player sender = (Player) csender;

        if (args.length == 0) {
            sender.sendMessage(messagePrefix + "-- Signalements --");

            // Pour les non-assignés
            TextComponent reportsString1 = new TextComponent("Non-assignés :");
            if (getUnassignedReports().size() == 0) {
                reportsString1.addExtra(" ");
                TextComponent reportText = new TextComponent("Aucun signalement.");
                reportsString1.addExtra(reportText);
            } else {
                for (Report report : getUnassignedReports()) {
                    reportsString1.addExtra(" ");
                    TextComponent reportText = new TextComponent("[" + report.getId() + "]");
                    reportText.setColor(ChatColor.AQUA);
                    reportText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/reports " + report.getId() + " "));

                    TextComponent joueurSignaleText = new TextComponent("Joueur Signalé : ");
                    TextComponent joueurSignale = new TextComponent(report.getPlayer().getDisplayName());
                    joueurSignale.setColor(ChatColor.AQUA);
                    TextComponent joueurQuiSignaleText = new TextComponent("\nPar : ");
                    TextComponent joueurQuiSignale = new TextComponent(report.getSender().getDisplayName());
                    joueurQuiSignale.setColor(ChatColor.AQUA);

                    TextComponent reportHoverText[] = {joueurSignaleText, joueurSignale, joueurQuiSignaleText, joueurQuiSignale};

                    reportText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, reportHoverText));
                    reportsString1.addExtra(reportText);
                }
            }
            sender.spigot().sendMessage(reportsString1);

            // Pour les assignés
            TextComponent reportsString2 = new TextComponent("Assignés (tous) :");
            if (getAssignedReports().size() == 0) {
                reportsString2.addExtra(" ");
                TextComponent reportText = new TextComponent("Aucun signalement.");
                reportsString2.addExtra(reportText);
            } else {
                for (Report report : getAssignedReports()) {
                    reportsString2.addExtra(" ");
                    TextComponent reportText = new TextComponent("[" + report.getId() + "]");
                    reportText.setColor(ChatColor.AQUA);
                    reportText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/reports " + report.getId() + " "));

                    TextComponent ligne1 = new TextComponent("Assigné à : ");
                    TextComponent joueurAssigne = new TextComponent(report.getAssignment().getDisplayName());
                    joueurAssigne.setColor(ChatColor.AQUA);
                    ligne1.addExtra(joueurAssigne);

                    TextComponent reportHoverText[] = {ligne1};

                    reportText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, reportHoverText));
                    reportsString2.addExtra(reportText);
                }
            }
            sender.spigot().sendMessage(reportsString2);

            // Pour les assignés au joueur
            TextComponent reportsString3 = new TextComponent("Assignés (à vous) :");
            if (getAssignedReports(sender).size() == 0) {
                reportsString3.addExtra(" ");
                TextComponent reportText = new TextComponent("Aucun signalement.");
                reportsString3.addExtra(reportText);
            } else {
                for (Report report : getAssignedReports(sender)) {
                    reportsString3.addExtra(" ");
                    TextComponent reportText = new TextComponent("[" + report.getId() + "]");
                    reportText.setColor(ChatColor.AQUA);
                    reportText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/reports " + report.getId() + " "));

                    TextComponent joueurSignaleText = new TextComponent("Joueur Signalé : ");
                    TextComponent joueurSignale = new TextComponent(report.getPlayer().getDisplayName());
                    joueurSignale.setColor(ChatColor.AQUA);
                    TextComponent joueurQuiSignaleText = new TextComponent("\nPar : ");
                    TextComponent joueurQuiSignale = new TextComponent(report.getSender().getDisplayName());
                    joueurQuiSignale.setColor(ChatColor.AQUA);

                    TextComponent reportHoverText[] = {joueurSignaleText, joueurSignale, joueurQuiSignaleText, joueurQuiSignale};

                    reportText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, reportHoverText));
                    reportsString3.addExtra(reportText);
                }
            }
            sender.spigot().sendMessage(reportsString3);

            sender.sendMessage(messagePrefix + "-- Signalements --");
            return true;
        } else if (args[1].equalsIgnoreCase("cloturer")) {
            int parsedId;
            try {
                parsedId = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                return false;
            }
            Report report;
            try {
                report = new Report(parsedId);
                if (report.isClosed()) {
                    sender.sendMessage(messagePrefix + org.bukkit.ChatColor.RED + "Ce signalement a déjà été cloturé.");
                    return true;
                }
                report.close(sender);
                sender.sendMessage(messagePrefix + "Le signalement n°" + org.bukkit.ChatColor.AQUA + report.getId() + org.bukkit.ChatColor.RESET + " a bien été cloturé.");
                report.getSender().sendMessage(messagePrefix + "Votre signalement (n°" + org.bukkit.ChatColor.AQUA + report.getId() + org.bukkit.ChatColor.RESET + ") à l'encontre de " + org.bukkit.ChatColor.AQUA + report.getPlayer().getDisplayName() + org.bukkit.ChatColor.RESET + " a été cloturé par " + org.bukkit.ChatColor.AQUA + sender.getDisplayName() + org.bukkit.ChatColor.RESET + ".");
            } catch (NullPointerException e) {
                sender.sendMessage(messagePrefix + org.bukkit.ChatColor.RED + "ID invalide.");
                return true;
            }
            return true;
        } else if (args[1].equalsIgnoreCase("assigner")) {
            int parsedId;
            try {
                parsedId = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                return false;
            }
            try {
                Report report = new Report(parsedId);
                if (report.isClosed()) {
                    sender.sendMessage(messagePrefix + org.bukkit.ChatColor.RED + "Ce signalement a été cloturé.");
                    return true;
                }
                report.setAssignement(sender);
                sender.sendMessage(messagePrefix + "-- Signalement n°" + report.getId() + " --");
                sender.sendMessage("Émetteur : " + org.bukkit.ChatColor.AQUA + report.getSender().getDisplayName());
                sender.sendMessage("Joueur signalé : " + org.bukkit.ChatColor.AQUA + report.getPlayer().getDisplayName());
                sender.sendMessage("Date : " + org.bukkit.ChatColor.AQUA + report.getDate());
                sender.sendMessage("Raison : " + org.bukkit.ChatColor.AQUA + report.getReason());
                sender.sendMessage("État du signalement : " + org.bukkit.ChatColor.AQUA + "Assigné");
                sender.sendMessage("À : " + org.bukkit.ChatColor.AQUA + report.getAssignment().getDisplayName());
                sender.sendMessage("Date : " + org.bukkit.ChatColor.AQUA + report.getAssignmentDate());
                sender.sendMessage(messagePrefix + "-- Signalement n°" + report.getId() + " --");
                sender.playSound(sender.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                report.getSender().sendMessage(messagePrefix + "Votre signalement (n°" + org.bukkit.ChatColor.AQUA + report.getId() + org.bukkit.ChatColor.RESET + ") à l'encontre de " + org.bukkit.ChatColor.AQUA + report.getPlayer().getDisplayName() + org.bukkit.ChatColor.RESET + " est en cours de traitement par " + org.bukkit.ChatColor.AQUA + sender.getDisplayName() + org.bukkit.ChatColor.RESET + ".");
                report.getSender().playSound(sender.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                return true;
            } catch (NullPointerException e) {
                sender.sendMessage(messagePrefix + org.bukkit.ChatColor.RED + "ID invalide.");
                e.printStackTrace();
                return true;
            }
        } else {
            return false;
        }
    }

    public static void notifyReport() {
        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            if (pl.hasPermission("skyplugins.admin")) {
                if (getUnassignedReports().size() == 1) {
                    pl.sendMessage(messagePrefix + org.bukkit.ChatColor.AQUA + getUnassignedReports().size() + org.bukkit.ChatColor.RESET + " signalement est en attente de traitement.");
                    pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                } else if (getUnassignedReports().size() > 1) {
                    pl.sendMessage(messagePrefix + org.bukkit.ChatColor.AQUA + getUnassignedReports().size() + org.bukkit.ChatColor.RESET + " signalements sont en attente de traitement.");
                    pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                }
            }
        }
    }
}
