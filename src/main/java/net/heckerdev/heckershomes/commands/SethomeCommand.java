package net.heckerdev.heckershomes.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.heckershomes.utils.StorageUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAlias("sethome")
@Description("Set a new home.")
public class SethomeCommand extends BaseCommand{

    @Default
    @Syntax("(optional) <home>")
    @CommandCompletion("<home>")
    public void onDefault(@NotNull CommandSender sender, String[] args) {
        if (!sender.hasPermission("heckershomes.command.sethome")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>⚠ You do not have permission to use this command!"));
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            Location playerLoc = player.getLocation();
            if (args.length == 0) {
                StorageUtil.setMainHome(player, playerLoc.getWorld().getName(), playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), playerLoc.getYaw(), playerLoc.getPitch());
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green><bold>✔</bold> Successfully set your new main home!"));
            } else {
                if (StorageUtil.getHomeCount(player) >= StorageUtil.getMaxHomes(player)) {
                    String storeLink = StorageUtil.getMainConfig().getString("store-link");
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> You have reached the maximum amount of homes, More home slots can be purchased at <click:OPEN_URL:" + storeLink + ">" + storeLink + "</click>!"));
                } else {
                    if (StorageUtil.setNewHome(player, args[0], playerLoc.getWorld().getName(), playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), playerLoc.getYaw(), playerLoc.getPitch())) {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<green><bold>✔</bold> Successfully set " + args[0] + "!"));
                    } else {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> That home already exists, if you want to overwrite that home delete it with /delhome first!"));
                    }
                }
            }
        } else {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("You need be a player to do this!"));
        }
    }
}
