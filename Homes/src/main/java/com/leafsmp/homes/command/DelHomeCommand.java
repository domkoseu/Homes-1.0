package com.leafsmp.homes.command;

import com.leafsmp.homes.HomeStorage;
import com.leafsmp.homes.HomesPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {

    private final HomesPlugin plugin;

    public DelHomeCommand(HomesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Tento prikaz muze pouzit jen hrac.");
            return true;
        }

        if (args.length == 0) {
            int count = plugin.storage().countHomes(player.getUniqueId());
            player.sendActionBar(Component.text("Uses: /delhome <number> ", NamedTextColor.RED)
                    .append(Component.text("(you have " + count + ")", NamedTextColor.GRAY)));
            return true;
        }

        int index;
        try {
            index = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendActionBar(Component.text("Home number must be between 1 and 10", NamedTextColor.RED));
            return true;
        }

        if (index < HomeStorage.MIN_INDEX || index > HomeStorage.MAX_INDEX) {
            player.sendActionBar(Component.text("Home number must be between 1 and 10", NamedTextColor.RED));
            return true;
        }

        if (!plugin.storage().isSet(player.getUniqueId(), index)) {
            player.sendActionBar(Component.text("Home ", NamedTextColor.RED)
                    .append(Component.text(index, NamedTextColor.YELLOW))
                    .append(Component.text(" is not set", NamedTextColor.RED)));
            return true;
        }

        plugin.storage().deleteHome(player.getUniqueId(), index);
        player.sendActionBar(Component.text("Home ", NamedTextColor.RED)
                .append(Component.text(index, NamedTextColor.YELLOW))
                .append(Component.text(" has been deleted", NamedTextColor.RED)));
        return true;
    }
}
