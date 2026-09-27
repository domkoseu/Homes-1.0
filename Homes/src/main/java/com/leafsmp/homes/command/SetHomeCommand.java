package com.leafsmp.homes.command;

import com.leafsmp.homes.HomeStorage;
import com.leafsmp.homes.HomesPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {

    private final HomesPlugin plugin;

    public SetHomeCommand(HomesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Tento prikaz muze pouzit jen hrac.");
            return true;
        }

        if (args.length < 1) {
            player.sendActionBar(Component.text("Usage: /sethome <1-10>", NamedTextColor.RED));
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

        if (index > HomeStorage.FREE_MAX_INDEX && !player.hasPermission("homes.homes." + index)) {
            player.sendActionBar(Component.text("You don't have permission to set home ", NamedTextColor.RED)
                    .append(Component.text(index, NamedTextColor.YELLOW)));
            return true;
        }

        plugin.storage().setHome(player.getUniqueId(), index, player.getLocation());
        player.sendActionBar(Component.text("Home ", NamedTextColor.GREEN)
                .append(Component.text(index, NamedTextColor.YELLOW))
                .append(Component.text(" has been set", NamedTextColor.GREEN)));
        return true;
    }
}
