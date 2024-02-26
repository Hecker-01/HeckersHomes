package net.heckerdev.heckershomes.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.heckershomes.utils.StorageUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

@CommandAlias("home")
@Description("Go to one of your homes.")
public class HomeCommand extends BaseCommand{

    private final HashMap<UUID, Long> cooldown;

    public HomeCommand() {
        this.cooldown = new HashMap<>();
    }

    @Default
    @Syntax("(optional) [home]")
    @CommandCompletion("[home] |")
    public void onDefault(@NotNull CommandSender sender, String[] args) {
        if (!sender.hasPermission("heckershomes.command.home")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>⚠ You do not have permission to use this command!"));
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            if (this.cooldown.containsKey(player.getUniqueId())) {
                if (System.currentTimeMillis() - cooldown.get(player.getUniqueId()) < 3000) {
                    int secondsLeft = (int) (3 - (System.currentTimeMillis() - cooldown.get(player.getUniqueId())) / 1000);
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> You need to wait " + secondsLeft + " seconds before using this command again!"));
                } else {
                    this.cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                    executeCommand(player, args);
                }
            } else {
                this.cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                executeCommand(player, args);
            }

        } else {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("You need be a player to do this!"));
        }
    }

    private void executeCommand(Player player, String[] args) {
        if (args.length == 0) {
            if (StorageUtil.doesMainHomeExist(player)) {
                player.teleport(StorageUtil.getMainHome(player));
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green><bold>✔</bold> Successfully sent you to your home!"));
            } else {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Your main home doesn't exist yet, create it with /sethome first!"));
            }
        } else {
            if (StorageUtil.doesHomeExist(player, args[0])) {
                player.teleport(StorageUtil.getHome(player, args[0]));
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green><bold>✔</bold> Successfully sent you to " + args[0] + "!"));
            } else {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> That home doesn't exist, create it with /sethome <home> first!"));
            }
        }

    }
}
