package net.heckerdev.heckershomes.utils;

import co.aikar.commands.annotation.Private;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;

public class StorageUtil {
    private static FileConfiguration getPlayerDataConfig(Player player) {
        File playerDataFile = new File(new File(Bukkit.getPluginsFolder()+"/HeckersHomes", "player-data"), player.getUniqueId() + ".yml");
        return YamlConfiguration.loadConfiguration(playerDataFile);
    }

    private static File getPlayerDataFile(Player player) {
        return new File(new File(Bukkit.getPluginsFolder()+"/HeckersHomes", "player-data"), player.getUniqueId() + ".yml");

    }

    public static boolean doesHomeExist(Player player, String home) {
        return getPlayerDataConfig(player).get("other-homes." + home.toLowerCase()) != null;
    }

    public static boolean doesMainHomeExist(Player player) {
        return getPlayerDataConfig(player).get("main-home.") != null;
    }

    public static boolean setNewHome(Player player, String home, String world, double x, double y, double z, double yaw, double pitch) {
        if (!doesHomeExist(player, home)) {
            try {
                FileConfiguration playerConf = getPlayerDataConfig(player);
                String homeLoc = "other-homes." + home.toLowerCase();
                playerConf.set(homeLoc + ".world", world);
                playerConf.set(homeLoc + ".x", x);
                playerConf.set(homeLoc + ".y", y);
                playerConf.set(homeLoc + ".z", z);
                playerConf.set(homeLoc + ".yaw", yaw);
                playerConf.set(homeLoc + ".pitch", pitch);
                playerConf.save(getPlayerDataFile(player));
            } catch (IOException ignored) {}

            return true;
        } else {
            return false;
        }
    }

    public static void setMainHome(Player player, String world, double x, double y, double z, double yaw, double pitch) {
        try {
            FileConfiguration playerConf = getPlayerDataConfig(player);
            String homeLoc = "main-home.";
            playerConf.set(homeLoc + ".world", world);
            playerConf.set(homeLoc + ".x", x);
            playerConf.set(homeLoc + ".y", y);
            playerConf.set(homeLoc + ".z", z);
            playerConf.set(homeLoc + ".yaw", yaw);
            playerConf.set(homeLoc + ".pitch", pitch);
            playerConf.save(getPlayerDataFile(player));
        } catch (IOException ignored) {}
    }

    public static Location getHome(Player player, String home) {
        FileConfiguration playerConf = getPlayerDataConfig(player);
        String homeLoc = "other-homes." + home.toLowerCase();
        String world = playerConf.getString(homeLoc + ".world");
        double x = playerConf.getDouble(homeLoc + ".x");
        double y = playerConf.getDouble(homeLoc + ".y");
        double z = playerConf.getDouble(homeLoc + ".z");
        double yaw = playerConf.getDouble(homeLoc + ".yaw");
        double pitch = playerConf.getDouble(homeLoc + ".pitch");

        return new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch);
    }

    public static Location getMainHome(Player player) {
        FileConfiguration playerConf = getPlayerDataConfig(player);
        String homeLoc = "main-home.";
        String world = playerConf.getString(homeLoc + ".world");
        double x = playerConf.getDouble(homeLoc + ".x");
        double y = playerConf.getDouble(homeLoc + ".y");
        double z = playerConf.getDouble(homeLoc + ".z");
        double yaw = playerConf.getDouble(homeLoc + ".yaw");
        double pitch = playerConf.getDouble(homeLoc + ".pitch");

        return new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch);
    }

    public static boolean delHome(Player player, String home) {
        if (doesHomeExist(player, home)) {
            try {
                FileConfiguration playerConf = getPlayerDataConfig(player);
                playerConf.set("other-homes." + home.toLowerCase(), null);
                playerConf.save(getPlayerDataFile(player));
            } catch (IOException ignored) {}

            return true;
        } else {
            return false;
        }
    }
}
