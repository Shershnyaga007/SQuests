package me.shershnyaga.squests.quests.conditions.find;

import me.shershnyaga.squests.npc.SNpc;
import me.shershnyaga.squests.quests.rewards.Reward;
import me.shershnyaga.squests.quests.conditions.Quest;
import me.shershnyaga.squests.quests.conditions.QuestStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class FindQuest extends Quest {
    public final static NamespacedKey FIND_BIOME = NamespacedKey.fromString("find_biome");
    public final Biome visitBiome;
    public FindQuest(String id, Component name, Component description, Reward reward, Biome visitBiome) {
        super(id, name, description, reward);
        this.visitBiome = visitBiome;
    }

    @Override
    public void complete(Player player) {
        ItemStack questItem = player.getInventory().getItemInMainHand();
        int rewardCount = questItem.getItemMeta().getPersistentDataContainer().get(
                Reward.REWARD_COUNT, PersistentDataType.INTEGER
        );
        questItem.setAmount(0);
        reward.assignReward(player, rewardCount);
    }

    @Override
    public QuestStatus checkQuestStatus(Player player, SNpc sNpc) {
        if (!this.isAssignedTo(sNpc)) {
            return QuestStatus.NOT_THIS_NPC;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.PAPER)
            return QuestStatus.NULL;

        if (!item.getItemMeta().getPersistentDataContainer().has(Quest.QUEST_ID, PersistentDataType.STRING))
            return QuestStatus.NULL;

        if (!item.getItemMeta().getPersistentDataContainer().has(FIND_BIOME, PersistentDataType.INTEGER))
            return QuestStatus.RECIEVED;

        return QuestStatus.WAITING_TO_COMPLETE;
    }

    @Override
    public ItemStack getQuestItem() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(ChatColor.YELLOW + "Задание"));

        List<Component> lore = new ArrayList<>();
        lore.add(name);
        lore.add(description);
        lore.add(Component.text(ChatColor.GRAY + "----------"));
        lore.add(Component.text(ChatColor.WHITE + "Посетить: " + ChatColor.AQUA + visitBiome.name()));
        lore.add(reward.getRewardStr());

        meta.lore(lore);
        meta.getPersistentDataContainer().set(Quest.QUEST_ID, PersistentDataType.STRING,
                id);
        meta.getPersistentDataContainer().set(Quest.QUEST_MATERIAL_COUNT,
                PersistentDataType.INTEGER, reward.getCountNow());
        meta.getPersistentDataContainer().set(Reward.REWARD_COUNT, PersistentDataType.INTEGER,
                reward.getCountNow());

        item.setItemMeta(meta);

        return item;
    }
}
