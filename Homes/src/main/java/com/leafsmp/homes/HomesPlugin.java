package com.leafsmp.homes;

import com.leafsmp.homes.command.AdminHomesCommand;
import com.leafsmp.homes.command.DelHomeCommand;
import com.leafsmp.homes.command.HomesCommand;
import com.leafsmp.homes.command.SetHomeCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class HomesPlugin extends JavaPlugin {

    private HomeStorage storage;

    @Override
    public void onEnable() {
        this.storage = new HomeStorage(this);

        getCommand("homes").setExecutor(new HomesCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("delhome").setExecutor(new DelHomeCommand(this));
        getCommand("adminhomes").setExecutor(new AdminHomesCommand(this));
    }

    public HomeStorage storage() {
        return storage;
    }
}
