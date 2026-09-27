package com.leafsmp.homes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Persists per-player home locations, equivalent of the {home<index>::<uuid>}
 * variables in the original Skript. One YAML file, path:
 * homes.<uuid>.<index>.[world|x|y|z|yaw|pitch]
 */
public class HomeStorage {

    public static final int MIN_INDEX = 1;
    public static final int MAX_INDEX = 10;
    public static final int FREE_MAX_INDEX = 5; // 1-5 need no extra permission, 6-10 do

    private final File file;
    private final FileConfiguration config;

    public HomeStorage(HomesPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "homes.yml");
        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Nepodarilo se vytvorit homes.yml: " + e.getMessage());
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    private String path(UUID uuid, int index) {
        return "homes." + uuid + "." + index;
    }

    public boolean isSet(UUID uuid, int index) {
        return config.contains(path(uuid, index) + ".world");
    }

    public Location getHome(UUID uuid, int index) {
        String base = path(uuid, index);
        if (!config.contains(base + ".world")) return null;

        String worldName = config.getString(base + ".world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) return null;

        double x = config.getDouble(base + ".x");
        double y = config.getDouble(base + ".y");
        double z = config.getDouble(base + ".z");
        float yaw = (float) config.getDouble(base + ".yaw");
        float pitch = (float) config.getDouble(base + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    public void setHome(UUID uuid, int index, Location loc) {
        String base = path(uuid, index);
        config.set(base + ".world", loc.getWorld().getName());
        config.set(base + ".x", loc.getX());
        config.set(base + ".y", loc.getY());
        config.set(base + ".z", loc.getZ());
        config.set(base + ".yaw", (double) loc.getYaw());
        config.set(base + ".pitch", (double) loc.getPitch());
        save();
    }

    public void deleteHome(UUID uuid, int index) {
        config.set(path(uuid, index), null);
        save();
    }

    /** How many homes (1-10) this player currently has set. */
    public int countHomes(UUID uuid) {
        int count = 0;
        for (int i = MIN_INDEX; i <= MAX_INDEX; i++) {
            if (isSet(uuid, i)) count++;
        }
        return count;
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
