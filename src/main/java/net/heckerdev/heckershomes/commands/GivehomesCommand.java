package net.heckerdev.heckershomes.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2RTFDTM;
import net.heckerdev.heckershomes.utils.StorageUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

@CommandAlias("givehomes")
@Description("Go to one of your homes.")
public class GivehomesCommand extends BaseCommand{

    @Default
    @Syntax("<player>")
    @CommandCompletion("@players <amount> |")
    public void onDefault(@NotNull CommandSender sender, String[] args) {
        if (!sender.hasPermission("heckershomes.command.givehomes")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>⚠ You do not have permission to use this command!"));
        } else {
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[0]);
                int addedHomes = Integer.parseInt(args[1]);
                if (StorageUtil.giveHomes(target, addedHomes)) {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<green><bold>✔</bold> Successfully gave " + target.getName() + " " + addedHomes + " extra homes!"));
                } else {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> An error occurred while giving " + target.getName() + " " + addedHomes + " homes!"));
                }
            } else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>❌</bold> Invalid usage! Correct usage: /givehomes <player> <amount>"));
            }
        }
    }
}
