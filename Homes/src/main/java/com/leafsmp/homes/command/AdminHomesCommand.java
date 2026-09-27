package com.leafsmp.homes.command;

import com.leafsmp.homes.HomesPlugin;
import com.leafsmp.homes.gui.AdminHomesMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class AdminHomesCommand implements CommandExecutor {

    private final HomesPlugin plugin;

    public AdminHomesCommand(HomesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Tento prikaz muze pouzit jen hrac.");
            return true;
        }

        if (args.length < 1) {
            player.sendActionBar(Component.text("Usage: /adminhomes <player>", NamedTextColor.RED));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        AdminHomesMenu.open(plugin, player, target);
        return true;
    }
}
