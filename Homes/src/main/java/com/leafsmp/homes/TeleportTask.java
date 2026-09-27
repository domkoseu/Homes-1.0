package com.leafsmp.homes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Equivalent of the original:
 *   loop 5 times:
 *       send action bar "Teleporting in Xs..."
 *       wait 1 second
 *       if player moved: cancel
 *   teleport
 *
 * Runs once per second for 5 seconds; cancels itself if the player moves more
 * than 1 block from where they started, exactly like the Skript version.
 */
public class TeleportTask extends BukkitRunnable {

    private final Player player;
    private final Location destination;
    private final Location startLocation;
    private final int homeIndex;
    private int remaining = 5;

    public TeleportTask(Player player, Location destination, int homeIndex) {
        this.player = player;
        this.destination = destination;
        this.startLocation = player.getLocation();
        this.homeIndex = homeIndex;
    }

    public static void start(Plugin plugin, Player player, Location destination, int homeIndex) {
        new TeleportTask(player, destination, homeIndex).runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            cancel();
            return;
        }

        if (hasMoved()) {
            player.sendActionBar(Component.text("You moved, teleport cancelled", NamedTextColor.RED));
            cancel();
            return;
        }

        if (remaining <= 0) {
            player.teleport(destination);
            player.sendActionBar(Component.text("Teleported to Home ", NamedTextColor.GREEN)
                    .append(Component.text(homeIndex, NamedTextColor.YELLOW)));
            cancel();
            return;
        }

        player.sendActionBar(Component.text("Teleporting in ", NamedTextColor.GRAY)
                .append(Component.text(remaining, NamedTextColor.YELLOW))
                .append(Component.text("s...", NamedTextColor.GRAY)));
        remaining--;
    }

    private boolean hasMoved() {
        Location current = player.getLocation();
        if (!current.getWorld().equals(startLocation.getWorld())) {
            return true;
        }
        return current.distance(startLocation) > 1;
    }
}
