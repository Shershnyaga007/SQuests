package me.shershnyaga.squests.commands;

import me.shershnyaga.squests.SQuests;
import me.shershnyaga.squests.npc.SNpc;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SQuestsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {

        Player player = (Player) sender;

        if (args.length == 0)
            return false;

        if (args[0].equals("spawn") && args.length == 2 && player
                .hasPermission("me.shershnyaga.squests.spawn")) {
            for (SNpc sNpc: SNpc.S_NPC_LIST) {
                if (sNpc.id.equals(args[1])) {
                    sNpc.spawn(player.getLocation());
                    return true;
                }
            }
            player.sendMessage(Component.text(ChatColor.RED + "Не найдет такой нпс!"));
        }
        else if (args[0].equals("count") && args.length == 1) {
            SQuests.database.printQuestsCount(player, player.getName());
        }
        else {
            return false;
        }

        return false;
    }
}

