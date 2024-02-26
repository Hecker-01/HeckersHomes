package net.heckerdev.heckershomes.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.heckerdev.heckershomes.utils.StorageUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@CommandAlias("delhome")
@Description("Delete one of your homes.")
public class DelhomeCommand extends BaseCommand{

    @Default
    @Syntax("<home>")
    @CommandCompletion("<home> |")
    public void onDefault(@NotNull CommandSender sender, String[] args) {
        if (!sender.hasPermission("heckershomes.command.delhome")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>⚠ You do not have permission to use this command!"));
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> You need to specify a home te delete! <gray>Usage: /delhome <u><home></u>"));
                return;
            }
            if (StorageUtil.delHome(player, args[0])) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green><bold>✔</bold> Successfully deleted " + args[0] + "!"));
            } else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>⚠ " + args[0] + " does not exist!<gray> Usage: /delhome <u><home></u>"));
            }
        } else {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("You need be a player to do this!"));
        }
    }
}
