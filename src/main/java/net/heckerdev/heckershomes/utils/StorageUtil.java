package net.heckerdev.heckershomes.utils;

import net.heckerdev.heckershomes.HeckersHomes;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.bukkit.Bukkit.getServer;

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

    public static CompletableFuture<Integer> getMaxHomes(Player player) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        Bukkit.getServer().getScheduler().runTaskAsynchronously(HeckersHomes.getInstance(), () -> {
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

            int totalHomes = defaultHomes + extraHomes + groupHomes;
            future.complete(totalHomes);
        });

        return future;
    }

    public static boolean doesHomeExist(Player player, String home) {
        return getPlayerDataConfig(player).get("other-homes." + home.toLowerCase()) != null;
    }

    public static boolean doesMainHomeExist(Player player) {
        return getPlayerDataConfig(player).get("main-home.") != null;
    }

    public static CompletableFuture<Boolean> setNewHome(Player player, String home, String world, double x, double y, double z, double yaw, double pitch) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Bukkit.getServer().getScheduler().runTaskAsynchronously(HeckersHomes.getInstance(), () -> {
            try {
                if (!doesHomeExist(player, home)) {
                    FileConfiguration playerConf = getPlayerDataConfig(player);
                    String homeLoc = "other-homes." + home.toLowerCase();
                    playerConf.set(homeLoc + ".world", world);
                    playerConf.set(homeLoc + ".x", x);
                    playerConf.set(homeLoc + ".y", y);
                    playerConf.set(homeLoc + ".z", z);
                    playerConf.set(homeLoc + ".yaw", yaw);
                    playerConf.set(homeLoc + ".pitch", pitch);
                    playerConf.save(getPlayerDataFile(player));
                    future.complete(true);
                } else {
                    future.complete(false);
                }
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    public static void setMainHome(Player player, String world, double x, double y, double z, double yaw, double pitch) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(HeckersHomes.getInstance(), () -> {
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
            } catch (IOException ignored) {
            }
        });
    }

    public static CompletableFuture<Location> getHome(Player player, String home) {
        CompletableFuture<Location> future = new CompletableFuture<>();

        Bukkit.getServer().getScheduler().runTaskAsynchronously(HeckersHomes.getInstance(), () -> {
            FileConfiguration playerConf = getPlayerDataConfig(player);
            String homeLoc = "other-homes." + home.toLowerCase();
            String world = playerConf.getString(homeLoc + ".world");
            double x = playerConf.getDouble(homeLoc + ".x");
            double y = playerConf.getDouble(homeLoc + ".y");
            double z = playerConf.getDouble(homeLoc + ".z");
            double yaw = playerConf.getDouble(homeLoc + ".yaw");
            double pitch = playerConf.getDouble(homeLoc + ".pitch");

            // Construct the location synchronously inside the asynchronous task
            Location location = new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch);

            // Complete the future with the location
            future.complete(location);
        });

        return future;
    }

    public static CompletableFuture<Location> getMainHome(Player player) {
        CompletableFuture<Location> future = new CompletableFuture<>();

        Bukkit.getServer().getScheduler().runTaskAsynchronously(HeckersHomes.getInstance(), () -> {
            FileConfiguration playerConf = getPlayerDataConfig(player);
            String homeLoc = "main-home.";
            String world = playerConf.getString(homeLoc + ".world");
            double x = playerConf.getDouble(homeLoc + ".x");
            double y = playerConf.getDouble(homeLoc + ".y");
            double z = playerConf.getDouble(homeLoc + ".z");
            double yaw = playerConf.getDouble(homeLoc + ".yaw");
            double pitch = playerConf.getDouble(homeLoc + ".pitch");

            // Construct the location synchronously inside the asynchronous task
            Location location = new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch);

            // Complete the future with the location
            future.complete(location);
        });

        return future;
    }

    public static CompletableFuture<Boolean> delHome(Player player, String home) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Bukkit.getServer().getScheduler().runTaskAsynchronously(HeckersHomes.getInstance(), () -> {
            try {
                if (doesHomeExist(player, home)) {
                    FileConfiguration playerConf = getPlayerDataConfig(player);
                    playerConf.set("other-homes." + home.toLowerCase(), null);
                    playerConf.save(getPlayerDataFile(player));
                    future.complete(true);
                } else {
                    future.complete(false);
                }
            } catch (IOException e) {
                future.complete(false);
            }
        });

        return future;
    }

    public static CompletableFuture<Boolean> giveHomes(Player player, int amount) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Bukkit.getServer().getScheduler().runTaskAsynchronously(HeckersHomes.getInstance(), () -> {
            try {
                FileConfiguration playerConf = getPlayerDataConfig(player);
                int homesNow = playerConf.getInt("extra-homes", 0);
                playerConf.set("extra-homes", homesNow + amount);
                playerConf.save(getPlayerDataFile(player));
                future.complete(true);
            } catch (IOException e) {
                future.complete(false);
            }
        });

        return future;
    }

    public static CompletableFuture<Object[]> getHomes(Player player) {
        CompletableFuture<Object[]> future = new CompletableFuture<>();

        Bukkit.getServer().getScheduler().runTaskAsynchronously(HeckersHomes.getInstance(), () -> {
            ConfigurationSection homesSection = getPlayerDataConfig(player).getConfigurationSection("other-homes");

            if (homesSection != null) {
                Object[] homes = homesSection.getKeys(false).toArray();
                future.complete(homes);
            } else {
                future.complete(null);
            }
        });

        return future;
    }
}
