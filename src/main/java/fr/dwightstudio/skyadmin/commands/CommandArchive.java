package fr.dwightstudio.skyadmin.commands;

import fr.dwightstudio.skyadmin.Report;
import fr.dwightstudio.skyadmin.Ticket;
import fr.dwightstudio.skyadmin.Warn;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.dwightstudio.skyadmin.Main.messagePrefix;
import static fr.dwightstudio.skyadmin.Ticket.*;
import static fr.dwightstudio.skyadmin.Warn.*;

public class CommandArchive implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender csender, Command command, String label, String[] args) {

        // Seul les joueurs peuvent executer cette commande
        if (!(csender instanceof Player)) {
            return false;
        }

        // On est sûr que c'est un joueur --> On remplace CommandSender par Player
        Player sender = (Player) csender;

        if (args.length < 2) {
            return false;
        }

        if (args.length > 2) {
            return false;
        }

        if (args[0].equalsIgnoreCase("warns")) {
            int parsedId;
            try {
                parsedId = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return false;
            }
            try {
                Warn warn = new Warn(parsedId);
                sender.sendMessage(messagePrefix + org.bukkit.ChatColor.RED + "-- Avertissement n°" + warn.getId() + " --");
                sender.sendMessage(org.bukkit.ChatColor.RED + "Émetteur : " + org.bukkit.ChatColor.AQUA + warn.getSender().getDisplayName());
                sender.sendMessage(org.bukkit.ChatColor.RED + "Joueur averti : " + org.bukkit.ChatColor.DARK_PURPLE + warn.getPlayer().getDisplayName());
                sender.sendMessage(org.bukkit.ChatColor.RED + "Raison : " + org.bukkit.ChatColor.DARK_PURPLE + warn.getReason());

                switch (warn.getCurrentWarnCount()) {
                    default:
                        sender.sendMessage(org.bukkit.ChatColor.RED + "Sanction : " + org.bukkit.ChatColor.DARK_PURPLE + "Aucune.");
                        break;
                    case 3:
                        sender.sendMessage(org.bukkit.ChatColor.RED + "Sanction : " + org.bukkit.ChatColor.DARK_PURPLE + "(Palier 1) Banni(e) pendant 30 minutes.");
                        break;
                    case 6:
                        sender.sendMessage(org.bukkit.ChatColor.RED + "Sanction : " + org.bukkit.ChatColor.DARK_PURPLE + "(Palier 2) Banni(e) pendant 2 heures.");
                        break;
                    case 9:
                        sender.sendMessage(org.bukkit.ChatColor.RED + "Sanction : " + org.bukkit.ChatColor.DARK_PURPLE + "(Palier 3) Banni(e) pendant 24 heures.");
                        break;
                    case 12:
                        sender.sendMessage(org.bukkit.ChatColor.RED + "Sanction : " + org.bukkit.ChatColor.DARK_PURPLE + "(Palier 4) Banni(e) pendant 7 jours.");
                        break;
                    case 15:
                        sender.sendMessage(org.bukkit.ChatColor.RED + "Sanction : " + org.bukkit.ChatColor.DARK_PURPLE + "(Palier 5) Banni(e) pendant 30 jours.");
                        break;
                }

                sender.sendMessage(messagePrefix + ChatColor.RED + "-- Avertissement n°" + warn.getId() + " --");
                sender.playSound(sender.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                return true;

            } catch (NullPointerException e) {
                sender.sendMessage(messagePrefix + org.bukkit.ChatColor.RED + "ID invalide.");
                e.printStackTrace();
                return true;
            }
        } else if (args[0].equalsIgnoreCase("reports")) {
            int parsedId;
            try {
                parsedId = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return false;
            }
            try {
                Report report = new Report(parsedId);
                sender.sendMessage(messagePrefix + "-- Signalement n°" + report.getId() + " --");
                sender.sendMessage("Émetteur : " + org.bukkit.ChatColor.AQUA + report.getSender().getDisplayName());
                sender.sendMessage("Joueur signalé : " + org.bukkit.ChatColor.AQUA + report.getPlayer().getDisplayName());
                sender.sendMessage("Date : " + org.bukkit.ChatColor.AQUA + report.getDate());
                sender.sendMessage("Raison : " + org.bukkit.ChatColor.AQUA + report.getReason());
                if (!report.isClosed() && !report.isAssigned()) {
                    sender.sendMessage("État du signalement : " + org.bukkit.ChatColor.AQUA + "Non assigné");
                } else if (!report.isClosed()) {
                    sender.sendMessage("État du signalement : " + org.bukkit.ChatColor.AQUA + "Assigné");
                    sender.sendMessage("À : " + org.bukkit.ChatColor.AQUA + report.getAssignment().getDisplayName());
                    sender.sendMessage("Date : " + org.bukkit.ChatColor.AQUA + report.getAssignmentDate());
                } else {
                    sender.sendMessage("État du signalement : " + org.bukkit.ChatColor.AQUA + "Cloturé");
                    sender.sendMessage("Assigné à : " + report.getAssignment().getDisplayName());
                    sender.sendMessage("Date : " + org.bukkit.ChatColor.AQUA + report.getAssignmentDate());
                    sender.sendMessage("Cloturé par : " + org.bukkit.ChatColor.AQUA + report.getClosingPlayer().getDisplayName());
                    sender.sendMessage("Date : " + org.bukkit.ChatColor.AQUA + report.getClosingDate());
                }
                sender.sendMessage(messagePrefix + "-- Signalement n°" + report.getId() + " --");
                sender.playSound(sender.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                return true;
            } catch (NullPointerException e) {
                sender.sendMessage(messagePrefix + org.bukkit.ChatColor.RED + "ID invalide.");
                e.printStackTrace();
                return true;
            }
        } else if (args[0].equalsIgnoreCase("tickets")) {
            int parsedId;
            try {
                parsedId = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return false;
            }
            try {
                Ticket ticket = new Ticket(parsedId);
                sender.sendMessage(messagePrefix + "-- Billet de support n°" + ticket.getId() + " --");
                sender.sendMessage("Émetteur : " + org.bukkit.ChatColor.AQUA + ticket.getSender().getDisplayName());
                sender.sendMessage("Date : " + org.bukkit.ChatColor.AQUA + ticket.getDate());
                sender.sendMessage("Raison : " + org.bukkit.ChatColor.AQUA + ticket.getReason());
                if (!ticket.isClosed() && !ticket.isAssigned()) {
                    sender.sendMessage("État du billet : " + org.bukkit.ChatColor.AQUA + "Non assigné");
                } else if (!ticket.isClosed()) {
                    sender.sendMessage("État du billet : " + org.bukkit.ChatColor.AQUA + "Assigné");
                    sender.sendMessage("À : " + org.bukkit.ChatColor.AQUA + ticket.getAssignment().getDisplayName());
                    sender.sendMessage("Date : " + org.bukkit.ChatColor.AQUA + ticket.getAssignmentDate());
                } else {
                    sender.sendMessage("État du billet : " + org.bukkit.ChatColor.AQUA + "Cloturé");
                    sender.sendMessage("Assigné à : " + ticket.getAssignment().getDisplayName());
                    sender.sendMessage("Date : " + org.bukkit.ChatColor.AQUA + ticket.getAssignmentDate());
                    sender.sendMessage("Cloturé par : " + org.bukkit.ChatColor.AQUA + ticket.getClosingPlayer().getDisplayName());
                    sender.sendMessage("Date : " + org.bukkit.ChatColor.AQUA + ticket.getClosingDate());
                }
                sender.sendMessage(messagePrefix + "-- Billet de support n°" + ticket.getId() + " --");
                sender.playSound(sender.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                return true;
            } catch (NullPointerException e) {
                sender.sendMessage(messagePrefix + org.bukkit.ChatColor.RED + "ID invalide.");
                e.printStackTrace();
                return true;
            }
        } else if (args[0].equalsIgnoreCase("joueur")) {
            Player player = Bukkit.getPlayer(args[1]);
            // Verifi que le joueur existe
            if (player == null) {
                sender.sendMessage(messagePrefix + ChatColor.RED + "Ce joueur n'existe pas.");
                return true;
            }

            sender.sendMessage(messagePrefix + "-- " + player.getDisplayName() + " --");

            // Reports émis
            TextComponent reportsString1 = new TextComponent("Signalements émis :");
            if (Report.getPlayerSentReports(player).size() == 0) {
                reportsString1.addExtra(" ");
                TextComponent reportText = new TextComponent("Aucun signalement.");
                reportsString1.addExtra(reportText);
            } else {
                for (Report report : Report.getPlayerSentReports(player)) {
                    reportsString1.addExtra(" ");
                    TextComponent reportText = new TextComponent("[" + report.getId() + "]");
                    reportText.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                    reportText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/archive reports " + report.getId()));

                    TextComponent joueurSignaleText = new TextComponent("Joueur Signalé : ");
                    TextComponent joueurSignale = new TextComponent(report.getPlayer().getDisplayName());
                    joueurSignale.setColor(net.md_5.bungee.api.ChatColor.AQUA);

                    TextComponent reportHoverText[] = {joueurSignaleText, joueurSignale};

                    reportText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, reportHoverText));
                    reportsString1.addExtra(reportText);
                }
            }
            sender.spigot().sendMessage(reportsString1);

            // Reports reçus
            TextComponent reportsString2 = new TextComponent("Signalements reçus :");
            if (Report.getPlayerReports(player).size() == 0) {
                reportsString2.addExtra(" ");
                TextComponent reportText = new TextComponent("Aucun signalement.");
                reportsString2.addExtra(reportText);
            } else {
                for (Report report : Report.getPlayerReports(player)) {
                    reportsString2.addExtra(" ");
                    TextComponent reportText = new TextComponent("[" + report.getId() + "]");
                    reportText.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                    reportText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/archive reports " + report.getId()));

                    TextComponent joueurQuiSignaleText = new TextComponent("Par : ");
                    TextComponent joueurQuiSignale = new TextComponent(report.getSender().getDisplayName());
                    joueurQuiSignale.setColor(net.md_5.bungee.api.ChatColor.AQUA);

                    TextComponent reportHoverText[] = {joueurQuiSignaleText, joueurQuiSignale};

                    reportText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, reportHoverText));
                    reportsString2.addExtra(reportText);
                }
            }
            sender.spigot().sendMessage(reportsString2);

            // Reports assignés
            if (player.hasPermission("skyadmin.reports")) {
                TextComponent reportsString3 = new TextComponent("Signalements assignés :");
                if (Report.getAssignedReports(player).size() == 0) {
                    reportsString3.addExtra(" ");
                    TextComponent reportText = new TextComponent("Aucun signalement.");
                    reportsString3.addExtra(reportText);
                } else {
                    for (Report report : Report.getAssignedReports(player)) {
                        reportsString3.addExtra(" ");
                        TextComponent reportText = new TextComponent("[" + report.getId() + "]");
                        reportText.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                        reportText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/archive reports " + report.getId()));

                        TextComponent joueurSignaleText = new TextComponent("Joueur Signalé : ");
                        TextComponent joueurSignale = new TextComponent(report.getPlayer().getDisplayName());
                        joueurSignale.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                        TextComponent joueurQuiSignaleText = new TextComponent("\nPar : ");
                        TextComponent joueurQuiSignale = new TextComponent(report.getSender().getDisplayName());
                        joueurQuiSignale.setColor(net.md_5.bungee.api.ChatColor.AQUA);

                        TextComponent reportHoverText[] = {joueurSignaleText, joueurSignale, joueurQuiSignaleText, joueurQuiSignale};

                        reportText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, reportHoverText));
                        reportsString3.addExtra(reportText);
                    }
                }
                sender.spigot().sendMessage(reportsString3);
            }

            // Reports Cloturés
            if (player.hasPermission("skyadmin.reports")) {
                TextComponent reportsString4 = new TextComponent("Signalements cloturés :");
                if (Report.getClosedReports(player).size() == 0) {
                    reportsString4.addExtra(" ");
                    TextComponent reportText = new TextComponent("Aucun signalement.");
                    reportsString4.addExtra(reportText);
                } else {
                    for (Report report : Report.getClosedReports(player)) {
                        reportsString4.addExtra(" ");
                        TextComponent reportText = new TextComponent("[" + report.getId() + "]");
                        reportText.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                        reportText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/archive reports " + report.getId()));

                        TextComponent joueurSignaleText = new TextComponent("Joueur Signalé : ");
                        TextComponent joueurSignale = new TextComponent(report.getPlayer().getDisplayName());
                        joueurSignale.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                        TextComponent joueurQuiSignaleText = new TextComponent("\nPar : ");
                        TextComponent joueurQuiSignale = new TextComponent(report.getSender().getDisplayName());
                        joueurQuiSignale.setColor(net.md_5.bungee.api.ChatColor.AQUA);

                        TextComponent reportHoverText[] = {joueurSignaleText, joueurSignale, joueurQuiSignaleText, joueurQuiSignale};

                        reportText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, reportHoverText));
                        reportsString4.addExtra(reportText);
                    }
                }
                sender.spigot().sendMessage(reportsString4);
            }

            // Tickets émis
            TextComponent ticketsString1 = new TextComponent("Billets de supports émis :");
            if (getPlayerTickets(player).size() == 0) {
                ticketsString1.addExtra(" ");
                TextComponent ticketText = new TextComponent("Aucun billet.");
                ticketsString1.addExtra(ticketText);
            } else {
                for (Ticket ticket : getPlayerTickets(player)) {
                    ticketsString1.addExtra(" ");
                    TextComponent ticketText = new TextComponent("[" + ticket.getId() + "]");
                    ticketText.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                    ticketText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/archive tickets " + ticket.getId()));
                    ticketsString1.addExtra(ticketText);
                }
            }
            sender.spigot().sendMessage(ticketsString1);

            // Tickets assignés
            if (player.hasPermission("skyadmin.tickets")) {
                TextComponent reportsString3 = new TextComponent("Billets assignés :");
                if (getAssignedTickets(player).size() == 0) {
                    reportsString3.addExtra(" ");
                    TextComponent reportText = new TextComponent("Aucun billet.");
                    reportsString3.addExtra(reportText);
                } else {
                    for (Ticket ticket : getAssignedTickets(player)) {
                        reportsString3.addExtra(" ");
                        TextComponent reportText = new TextComponent("[" + ticket.getId() + "]");
                        reportText.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                        reportText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/archive tickets " + ticket.getId()));

                        TextComponent joueurQuiSignaleText = new TextComponent("Par : ");
                        TextComponent joueurQuiSignale = new TextComponent(ticket.getSender().getDisplayName());
                        joueurQuiSignale.setColor(net.md_5.bungee.api.ChatColor.AQUA);

                        TextComponent reportHoverText[] = {joueurQuiSignaleText, joueurQuiSignale};

                        reportText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, reportHoverText));
                        reportsString3.addExtra(reportText);
                    }
                }
                sender.spigot().sendMessage(reportsString3);
            }

            // Tickets Cloturés
            if (player.hasPermission("skyadmin.tickets")) {
                TextComponent reportsString4 = new TextComponent("Billets cloturés :");
                if (getClosedTickets(player).size() == 0) {
                    reportsString4.addExtra(" ");
                    TextComponent reportText = new TextComponent("Aucun billet.");
                    reportsString4.addExtra(reportText);
                } else {
                    for (Ticket ticket : getClosedTickets(player)) {
                        reportsString4.addExtra(" ");
                        TextComponent reportText = new TextComponent("[" + ticket.getId() + "]");
                        reportText.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                        reportText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/archive tickets " + ticket.getId()));

                        TextComponent joueurQuiSignaleText = new TextComponent("Par : ");
                        TextComponent joueurQuiSignale = new TextComponent(ticket.getSender().getDisplayName());
                        joueurQuiSignale.setColor(net.md_5.bungee.api.ChatColor.AQUA);

                        TextComponent reportHoverText[] = {joueurQuiSignaleText, joueurQuiSignale};

                        reportText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, reportHoverText));
                        reportsString4.addExtra(reportText);
                    }
                }
                sender.spigot().sendMessage(reportsString4);
            }

            // Warns reçus
            TextComponent warnsString1 = new TextComponent("Avertissments reçus :");
            if (getPlayerWarns(player).size() == 0) {
                warnsString1.addExtra(" ");
                TextComponent ticketText = new TextComponent("Aucun avertissment.");
                warnsString1.addExtra(ticketText);
            } else {
                for (Warn warn : getPlayerWarns(player)) {
                    warnsString1.addExtra(" ");
                    TextComponent ticketText = new TextComponent("[" + warn.getId() + "]");
                    ticketText.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                    ticketText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/archive warns " + warn.getId()));
                    warnsString1.addExtra(ticketText);
                }
            }
            sender.spigot().sendMessage(warnsString1);

            // Warns émis
            TextComponent warnsString2 = new TextComponent("Avertissments émis :");
            if (getPlayerSentWarns(player).size() == 0) {
                warnsString2.addExtra(" ");
                TextComponent ticketText = new TextComponent("Aucun avertissment.");
                warnsString2.addExtra(ticketText);
            } else {
                for (Warn warn : getPlayerSentWarns(player)) {
                    warnsString2.addExtra(" ");
                    TextComponent ticketText = new TextComponent("[" + warn.getId() + "]");
                    ticketText.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                    ticketText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/archive warns " + warn.getId()));
                    warnsString2.addExtra(ticketText);
                }
            }
            sender.spigot().sendMessage(warnsString2);

            sender.sendMessage(messagePrefix + "-- " + player.getDisplayName() + " --");
            return true;
        } else {
            return false;
        }
    }
}
