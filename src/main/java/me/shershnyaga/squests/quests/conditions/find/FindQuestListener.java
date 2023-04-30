package me.shershnyaga.squests.quests.conditions.find;

import me.shershnyaga.squests.quests.conditions.Quest;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class FindQuestListener implements Listener {

    @EventHandler
    private void onPlayerChangeBiome(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!event.hasChangedBlock())
            return;

        for (ItemStack item: player.getInventory().getStorageContents()) {

            if (item == null)
                continue;

            ItemMeta meta = item.getItemMeta();
            if (meta == null)
                continue;

            if (meta.getPersistentDataContainer().has(FindQuest.FIND_BIOME,
                    PersistentDataType.INTEGER))
                continue;

            Quest quest = Quest.getQuestFromItem(item);
            if (quest == null)
                continue;
            if (quest.getClass() != FindQuest.class)
                continue;
            FindQuest findQuest = (FindQuest) quest;

            Biome biome = event.getTo().getBlock().getBiome();
            if (findQuest.visitBiome.name().equals(biome.name())) {
                player.sendMessage(ChatColor.GREEN + "Вы посетили биом " + findQuest.visitBiome +
                        ". Можете возвращаться за наградой!");
                meta.getPersistentDataContainer().set(FindQuest.FIND_BIOME,
                        PersistentDataType.INTEGER, 1);
                item.setItemMeta(meta);
            }
        }
    }
}
