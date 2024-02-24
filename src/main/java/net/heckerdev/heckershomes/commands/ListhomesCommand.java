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

@CommandAlias("listhomes|lhomes")
@Description("List your homes.")
public class ListhomesCommand extends BaseCommand{

    @Default
    @Syntax("")
    public void onDefault(@NotNull CommandSender sender) {
        if (!sender.hasPermission("heckershomes.command.listhomes")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>⚠ You do not have permission to use this command!"));
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Your homes are: <gold>"));
        } else {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("You need be a player to do this!"));
        }
    }
}
