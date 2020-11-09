package fr.dwightstudio.skyadmin.commands;

import fr.dwightstudio.skyadmin.Ticket;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.dwightstudio.skyadmin.Main.messagePrefix;
import static fr.dwightstudio.skyadmin.Ticket.getPlayerActiveTickets;
import static fr.dwightstudio.skyadmin.commands.CommandTickets.notifyTicket;

public class CommandTicket implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender csender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        // Seul les joueurs peuvent executer cette commande
        if (!(csender instanceof Player)) {
            return false;
        }
        // On est sûr que c'est un joueur --> On remplace CommandSender par Player
        Player sender = (Player) csender;

        // Vérifier que le joueur n'est pas utiliser trop de fois la commande
        if (getPlayerActiveTickets(sender).size() >= 3) {
            sender.sendMessage(messagePrefix + ChatColor.RED + "Vous avez atteint la quantité maximale de billets de support actifs.");
            return true;
        }

        // On convertit la liste en String à partir de l'argument 1
        StringBuilder message = new StringBuilder();
        for (int i = 0; i != args.length; i++) {
            message.append(args[i]).append(" ");
        }

        Ticket ticket = new Ticket(sender, message.toString());
        sender.sendMessage(messagePrefix + "-- Billet de support n°" + ticket.getId() + " --");
        sender.sendMessage("Émetteur : " + org.bukkit.ChatColor.AQUA + ticket.getSender().getDisplayName());
        sender.sendMessage("Raison : " + ChatColor.AQUA + message.toString());
        sender.sendMessage("État du billet : " + ChatColor.AQUA + "Non assigné");
        sender.sendMessage(messagePrefix + "-- Billet de support n°" + ticket.getId() + " --");
        sender.playSound(sender.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
        notifyTicket();
        return true;
    }
}
