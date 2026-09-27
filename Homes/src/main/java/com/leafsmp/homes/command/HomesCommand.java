package com.leafsmp.homes.command;

import com.leafsmp.homes.HomeStorage;
import com.leafsmp.homes.HomesPlugin;
import com.leafsmp.homes.TeleportTask;
import com.leafsmp.homes.gui.HomesMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomesCommand implements CommandExecutor {

    private final HomesPlugin plugin;

    public HomesCommand(HomesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Tento prikaz muze pouzit jen hrac.");
            return true;
        }

        if (args.length == 0) {
            HomesMenu.open(plugin, player);
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
            player.sendActionBar(Component.text("You don't have permission for home ", NamedTextColor.RED)
                    .append(Component.text(index, NamedTextColor.YELLOW)));
            return true;
        }

        if (!plugin.storage().isSet(player.getUniqueId(), index)) {
            player.sendActionBar(Component.text("Home ", NamedTextColor.RED)
                    .append(Component.text(index, NamedTextColor.YELLOW))
                    .append(Component.text(" is not set, use /sethome " + index, NamedTextColor.RED)));
            return true;
        }

        Location loc = plugin.storage().getHome(player.getUniqueId(), index);
        player.closeInventory();
        TeleportTask.start(plugin, player, loc, index);
        return true;
    }
}
