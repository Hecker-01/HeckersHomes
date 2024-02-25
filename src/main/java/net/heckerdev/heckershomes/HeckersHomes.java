package net.heckerdev.heckershomes;

import co.aikar.commands.PaperCommandManager;
import net.heckerdev.heckershomes.commands.*;
import net.heckerdev.heckershomes.listeners.PlayerJoinListener;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class HeckersHomes extends JavaPlugin {

    private static Permission perms = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        setupPermissions();
        setupListeners();
        setupCommands();
        Bukkit.getLogger().info("[HeckersHomes] has loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("[HeckersHomes] has been disabled!");
    }

    private void setupListeners() {
        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    private void setupCommands() {
        // Register commands
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new HomeCommand());
        manager.registerCommand(new ListhomesCommand());
        manager.registerCommand(new SethomeCommand());
        manager.registerCommand(new DelhomeCommand());
    }

    private boolean setupPermissions() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().warning(" - Disabled because Vault is not installed!");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        perms = rsp.getProvider();
        return perms != null;
    }

    public static Permission getPermissions() {
        return perms;
    }
    public static HeckersHomes getInstance() {
        return getPlugin(HeckersHomes.class);
    }
}
