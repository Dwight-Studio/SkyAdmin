package fr.skynnotopia.skyadmin.commands;

import fr.skynnotopia.skyadmin.Ticket;
import net.md_5.bungee.api.ChatColor;
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
import static fr.skynnotopia.skyadmin.Ticket.getAssignedTickets;
import static fr.skynnotopia.skyadmin.Ticket.getUnassignedTickets;

public class CommandTickets implements CommandExecutor {
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
            sender.sendMessage(messagePrefix + "-- Billets de support --");

            // Pour les non-assignés
            TextComponent ticketsString1 = new TextComponent("Non-assignés :");
            if (getUnassignedTickets().size() == 0) {
                ticketsString1.addExtra(" ");
                TextComponent ticketText = new TextComponent("Aucun billet.");
                ticketsString1.addExtra(ticketText);
            } else {
                for (Ticket ticket : getUnassignedTickets()) {
                    ticketsString1.addExtra(" ");
                    TextComponent ticketText = new TextComponent("[" + ticket.getId() + "]");
                    ticketText.setColor(ChatColor.AQUA);
                    ticketText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tickets " + ticket.getId() + " "));

                    TextComponent joueurQuiDemandeText = new TextComponent("Par : ");
                    TextComponent joueurQuiDemande = new TextComponent(ticket.getSender().getDisplayName());
                    joueurQuiDemande.setColor(ChatColor.AQUA);

                    TextComponent ticketHoverText[] = {joueurQuiDemandeText, joueurQuiDemande};

                    ticketText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ticketHoverText));
                    ticketsString1.addExtra(ticketText);
                }
            }
            sender.spigot().sendMessage(ticketsString1);

            // Pour les assignés
            TextComponent ticketsString2 = new TextComponent("Assignés (tous) :");
            if (getAssignedTickets().size() == 0) {
                ticketsString2.addExtra(" ");
                TextComponent ticketText = new TextComponent("Aucun billet.");
                ticketsString2.addExtra(ticketText);
            } else {
                for (Ticket ticket : getAssignedTickets()) {
                    ticketsString2.addExtra(" ");
                    TextComponent ticketText = new TextComponent("[" + ticket.getId() + "]");
                    ticketText.setColor(ChatColor.AQUA);
                    ticketText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tickets " + ticket.getId() + " "));

                    TextComponent ligne1 = new TextComponent("Assigné à : ");
                    TextComponent joueurAssigne = new TextComponent(ticket.getAssignment().getDisplayName());
                    joueurAssigne.setColor(ChatColor.AQUA);
                    ligne1.addExtra(joueurAssigne);

                    TextComponent ticketHoverText[] = {ligne1};

                    ticketText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ticketHoverText));
                    ticketsString2.addExtra(ticketText);
                }
            }
            sender.spigot().sendMessage(ticketsString2);

            // Pour les assignés au joueur
            TextComponent ticketsString3 = new TextComponent("Assignés (à vous) :");
            if (getAssignedTickets(sender).size() == 0) {
                ticketsString3.addExtra(" ");
                TextComponent ticketText = new TextComponent("Aucun billet.");
                ticketsString3.addExtra(ticketText);
            } else {
                for (Ticket ticket : getAssignedTickets(sender)) {
                    ticketsString3.addExtra(" ");
                    TextComponent ticketText = new TextComponent("[" + ticket.getId() + "]");
                    ticketText.setColor(ChatColor.AQUA);
                    ticketText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tickets " + ticket.getId() + " "));

                    TextComponent joueurQuiDemandeText = new TextComponent("Par : ");
                    TextComponent joueurQuiDemande = new TextComponent(ticket.getSender().getDisplayName());
                    joueurQuiDemande.setColor(ChatColor.AQUA);

                    TextComponent ticketHoverText[] = {joueurQuiDemandeText, joueurQuiDemande};

                    ticketText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ticketHoverText));
                    ticketsString3.addExtra(ticketText);
                }
            }
            sender.spigot().sendMessage(ticketsString3);

            sender.sendMessage(messagePrefix + "-- Billets de support --");
            return true;
        } else if (args[1].equalsIgnoreCase("cloturer")) {
            int parsedId;
            try {
                parsedId = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                return false;
            }
            Ticket ticket;
            try {
                ticket = new Ticket(parsedId);
                if (ticket.isClosed()) {
                    sender.sendMessage(messagePrefix + org.bukkit.ChatColor.RED + "Ce billet a déjà été cloturé.");
                    return true;
                }
                if (!ticket.isAssigned()) {
                    sender.sendMessage(messagePrefix + org.bukkit.ChatColor.RED + "Ce billet n'a pas été assigné.");
                    return true;
                }
                ticket.close(sender);
                sender.sendMessage(messagePrefix + "Le billet de support n°" + org.bukkit.ChatColor.AQUA + ticket.getId() + org.bukkit.ChatColor.RESET + " a bien été cloturé.");
                ticket.getSender().sendMessage(messagePrefix + "Votre billet de support (n°" + org.bukkit.ChatColor.AQUA + ticket.getId() + org.bukkit.ChatColor.RESET + ") a été cloturé par " + org.bukkit.ChatColor.AQUA + sender.getDisplayName() + org.bukkit.ChatColor.RESET + ".");
                for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    if (pl.hasPermission("skyplugins.tickets") && pl != sender) {
                        pl.sendMessage(messagePrefix + "Le billet de support n°" + org.bukkit.ChatColor.AQUA + ticket.getId() + org.bukkit.ChatColor.RESET + " a été cloturé par " + org.bukkit.ChatColor.AQUA + sender.getDisplayName() + org.bukkit.ChatColor.RESET + ".");
                    }
                }
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
                Ticket ticket = new Ticket(parsedId);
                if (ticket.isClosed()) {
                    sender.sendMessage(messagePrefix + org.bukkit.ChatColor.RED + "Ce billet a déjà été cloturé.");
                    return true;
                }
                ticket.setAssignement(sender);
                sender.sendMessage(messagePrefix + "-- Billet de support n°" + ticket.getId() + " --");
                sender.sendMessage("Émetteur : " + org.bukkit.ChatColor.AQUA + ticket.getSender().getDisplayName());
                sender.sendMessage("Date : " + org.bukkit.ChatColor.AQUA + ticket.getDate());
                sender.sendMessage("Raison : " + org.bukkit.ChatColor.AQUA + ticket.getReason());
                sender.sendMessage("État du billet : " + org.bukkit.ChatColor.AQUA + "Assigné");
                sender.sendMessage("À : " + ticket.getAssignment().getDisplayName());
                sender.sendMessage("Date : " + org.bukkit.ChatColor.AQUA + ticket.getAssignmentDate());
                sender.sendMessage(messagePrefix + "-- Billet de support n°" + ticket.getId() + " --");
                sender.playSound(sender.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                ticket.getSender().sendMessage(messagePrefix + "Votre billet de support (n°" + org.bukkit.ChatColor.AQUA + ticket.getId() + org.bukkit.ChatColor.RESET + ") est en cours de traitement par " + org.bukkit.ChatColor.AQUA + sender.getDisplayName() + org.bukkit.ChatColor.RESET + ".");
                ticket.getSender().playSound(sender.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    if (pl.hasPermission("skyplugins.tickets") && pl != sender) {
                        pl.sendMessage(messagePrefix + "Le billet de support n°" + org.bukkit.ChatColor.AQUA + ticket.getId() + org.bukkit.ChatColor.RESET + " est en cours de traitement par " + org.bukkit.ChatColor.AQUA + sender.getDisplayName() + org.bukkit.ChatColor.RESET + ".");
                    }
                }
            } catch (NullPointerException e) {
                sender.sendMessage(messagePrefix + org.bukkit.ChatColor.RED + "ID invalide.");
                e.printStackTrace();
                return true;
            }
            return true;
        } else {
            return false;
        }
    }

    public static void notifyTicket() {
        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            if (pl.hasPermission("skyplugins.tickets")) {
                if (getUnassignedTickets().size() == 1) {
                    pl.sendMessage(messagePrefix + org.bukkit.ChatColor.AQUA + getUnassignedTickets().size() + org.bukkit.ChatColor.RESET + " billet de support est en attente de traitement.");
                    pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                } else if (getUnassignedTickets().size() > 1) {
                    pl.sendMessage(messagePrefix + org.bukkit.ChatColor.AQUA + getUnassignedTickets().size() + org.bukkit.ChatColor.RESET + " billets de support sont en attente de traitement.");
                    pl.playSound(pl.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
                }
            }
        }
    }
}
