package me.shershnyaga.squests.commands;

import me.shershnyaga.squests.npc.SNpc;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQuestsTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {

        Player player = (Player) sender;

        if (args.length == 1) {
            List<String> list = Collections.singletonList("count");

            if (player.hasPermission("me.shershnyaga.squests.spawn"))
                list.add("spawn");

            return list;
        }
        else if (args.length == 2 && args[0].equals("spawn")) {
            return getSNpcIds();
        }
        else return new ArrayList<>();
    }

    private List<String> getSNpcIds() {
        List<String> ids = new ArrayList<>();
        for (SNpc sNpc: SNpc.S_NPC_LIST)
            ids.add(sNpc.id);

        return ids;
    }
}
