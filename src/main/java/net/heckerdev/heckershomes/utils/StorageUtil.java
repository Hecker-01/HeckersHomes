package net.heckerdev.heckershomes.utils;

import net.heckerdev.heckershomes.HeckersHomes;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;

public class StorageUtil {
    public static FileConfiguration getMainConfig() {
        File playerDataFile = new File(new File(Bukkit.getPluginsFolder(), "HeckersHomes"), "config.yml");
        return YamlConfiguration.loadConfiguration(playerDataFile);
    }

    private static File getMainConfigFile(Player player) {
        return new File(new File(Bukkit.getPluginsFolder(), "HeckersHomes"), "config.yml");
    }

    private static FileConfiguration getPlayerDataConfig(Player player) {
        File playerDataFile = new File(new File(Bukkit.getPluginsFolder()+"/HeckersHomes", "player-data"), player.getUniqueId() + ".yml");
        return YamlConfiguration.loadConfiguration(playerDataFile);
    }

    private static File getPlayerDataFile(Player player) {
        return new File(new File(Bukkit.getPluginsFolder()+"/HeckersHomes", "player-data"), player.getUniqueId() + ".yml");
    }

    public static int getHomeCount(Player player) {
        if (getPlayerDataConfig(player).get("other-homes") != null) {
            return (getPlayerDataConfig(player).getConfigurationSection("other-homes").getKeys(false).size() + 1);
        } else return 1;
    }

    public static int getMaxHomes(Player player) {
        Permission perms = HeckersHomes.getPermissions();
        int extraHomes = getPlayerDataConfig(player).getInt("extra-homes", 0);
        int defaultHomes = getMainConfig().getInt("default-max-homes", 1);
        int groupHomes = 0;

        if (getMainConfig().getBoolean("vault-groups.enabled", false)) {
            String[] groups = perms.getPlayerGroups(player);
            for (String group : groups) {
                int groupHomesTemp = getMainConfig().getInt("vault-groups." + group + ".extra-homes", 0);
                if (groupHomesTemp > groupHomes) {
                    groupHomes = groupHomesTemp;
                }
            }
        }

        return defaultHomes + extraHomes + groupHomes;
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

    public static boolean giveHomes(Player player, int amount) {
        try {
            FileConfiguration playerConf = getPlayerDataConfig(player);
            int homesNow = playerConf.getInt("extra-homes", 0);
            playerConf.set("extra-homes", homesNow + amount);
            playerConf.save(getPlayerDataFile(player));
        } catch (IOException ignored) {
            return false;
        }

        return true;
    }

    public static Object[] getHomes(Player player) {
        if (getPlayerDataConfig(player).get("other-homes") != null) {
            return getPlayerDataConfig(player).getConfigurationSection("other-homes").getKeys(false).toArray();
        } else return null;
    }
}
