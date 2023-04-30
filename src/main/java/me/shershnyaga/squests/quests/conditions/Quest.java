package me.shershnyaga.squests.quests.conditions;

import me.shershnyaga.squests.npc.SNpc;
import me.shershnyaga.squests.quests.rewards.Reward;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;


public abstract class Quest {
    public final static NamespacedKey QUEST_ID = NamespacedKey.fromString("quest_id");
    public final static NamespacedKey QUEST_MATERIAL_COUNT =
            NamespacedKey.fromString("quest_material_count");
    public final static List<Quest> QUEST_LIST = new ArrayList<>();
    public final String id;
    public final Component name;
    public final Component description;
    public final Reward reward;

    public Quest(String id, Component name, Component description, Reward reward) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.reward = reward;
    }

    public static Quest getQuestFromItem(ItemStack item) {

        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

        if (container.has(QUEST_ID, PersistentDataType.STRING)) {
            String questId = container.get(QUEST_ID, PersistentDataType.STRING);

            for(Quest quest: QUEST_LIST)
                if(quest.id.equals(questId))
                    return quest;
        }

        return null;
    }

    public boolean isAssignedTo(SNpc sNpc) {
        if (!sNpc.questsList.contains(this)) {
            return false;
        }
        return true;
    }

    public abstract void complete(Player player);

    public abstract QuestStatus checkQuestStatus(Player player, SNpc sNpc);
    public abstract ItemStack getQuestItem();
}
