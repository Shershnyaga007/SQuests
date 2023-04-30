package me.shershnyaga.squests.quests.conditions.item;

import me.shershnyaga.squests.npc.SNpc;
import me.shershnyaga.squests.quests.MathCount;
import me.shershnyaga.squests.quests.rewards.Reward;
import me.shershnyaga.squests.quests.conditions.Quest;
import me.shershnyaga.squests.quests.conditions.QuestStatus;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemQuest extends Quest {
    public final FindItems findItems;
    public ItemQuest(String id, Component name, Component description, Reward reward, FindItems items) {
        super(id, name, description, reward);
        this.findItems = items;
    }

    @Override
    public void complete(Player player) {
        ItemStack questItem = player.getInventory().getItemInMainHand();

        int rewardCount = questItem.getItemMeta().getPersistentDataContainer().get(
                Reward.REWARD_COUNT, PersistentDataType.INTEGER
        );

        int questMaterialCount = questItem.getItemMeta().getPersistentDataContainer().get(
                Quest.QUEST_MATERIAL_COUNT,
                PersistentDataType.INTEGER);

        player.getInventory().removeItem(new ItemStack(findItems.material, questMaterialCount));

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

        int questMaterialCount = item.getItemMeta().getPersistentDataContainer().get(Quest.QUEST_MATERIAL_COUNT,
                PersistentDataType.INTEGER);

        if (player.getInventory().contains(findItems.material, questMaterialCount))
            return QuestStatus.WAITING_TO_COMPLETE;

        return QuestStatus.RECIEVED;
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
        lore.add(Component.text(ChatColor.WHITE + "Принести: " + ChatColor.AQUA +
                findItems.material.toString() + " " + reward.getCountNow()));
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

    public static class FindItems {
        public final Material material;
        public final MathCount count;

        public FindItems(Material material, MathCount count) {
            this.material = material;
            this.count = count;
        }
    }
}
