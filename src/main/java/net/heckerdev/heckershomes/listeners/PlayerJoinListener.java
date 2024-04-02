package net.heckerdev.heckershomes.listeners;

import net.heckerdev.heckershomes.HeckersHomes;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class PlayerJoinListener implements Listener {

    private final HeckersHomes plugin;

    public PlayerJoinListener(HeckersHomes plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        getServer().getScheduler().runTaskAsynchronously(HeckersHomes.getInstance(), () -> {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();

            File dataFolder = new File(Bukkit.getPluginsFolder() + "/HeckersHomes", "player-data");
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }

            File playerDataFile = new File(dataFolder, uuid + ".yml");
            if (!playerDataFile.exists()) {
                try {
                    playerDataFile.createNewFile();
                } catch (IOException exception) {
                    Bukkit.getLogger().warning(exception.getMessage());
                    return;
                }

                FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);
                try {
                    playerData.set("extra-homes", 0);
                    playerData.save(playerDataFile);
                } catch (IOException exception) {
                    Bukkit.getLogger().warning(exception.getMessage());
                }

            }
        });
    }
}
