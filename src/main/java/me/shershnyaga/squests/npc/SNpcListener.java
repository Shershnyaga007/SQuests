package me.shershnyaga.squests.npc;

import me.shershnyaga.squests.SQuests;
import me.shershnyaga.squests.quests.conditions.Quest;
import me.shershnyaga.squests.quests.conditions.QuestStatus;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class SNpcListener implements Listener {

    @EventHandler
    private void click(NPCRightClickEvent event) {
        if (!event.getNPC().hasTrait(SNpcTrait.class)) {
            for (SNpc sNpc: SNpc.S_NPC_LIST)
                if (event.getNPC().getName().equals(sNpc.name)) {
                    SNpcTrait trait = new SNpcTrait();
                    trait.name = sNpc.id;
                    event.getNPC().addTrait(trait);
                }
        }
        SNpcTrait sNpcTrait = event.getNPC().getOrAddTrait(SNpcTrait.class);

        SNpc sNpc = SNpc.getSNpcFromId(sNpcTrait.name);

        if (sNpc == null)
            return;

        Player player = event.getClicker();

        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        if (itemInMainHand.getType() == Material.PAPER && itemInMainHand.getItemMeta()
                .getPersistentDataContainer().has(Quest.QUEST_ID, PersistentDataType.STRING)) {

            Quest quest = Quest.getQuestFromItem(itemInMainHand);
            QuestStatus status = quest.checkQuestStatus(player, sNpc);
            if (quest.checkQuestStatus(player, sNpc) == QuestStatus.NOT_THIS_NPC) {
                player.sendMessage(Component.text(ChatColor.RED + "Это не мой квест!"));
            }
            else if (status == QuestStatus.RECIEVED) {
                player.sendMessage(Component.text(ChatColor.RED + "Не выполенены условия квеста!"));
            }
            else if (status == QuestStatus.WAITING_TO_COMPLETE) {
                player.sendMessage(Component.text(ChatColor.GREEN + "Квест выполнен!"));
                SQuests.database.increaseQuestCount(player.getName());
                quest.complete(player);
            }

        }
        else {
            player.openInventory(sNpc.sNPCInventory);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerClickInSNpcInv(InventoryClickEvent event) {

        SNpc sNpc = null;
        for (SNpc sNpc1: SNpc.S_NPC_LIST)
            if (sNpc1.sNPCInventory.equals(event.getInventory())) {
                sNpc = sNpc1;
                break;
            }

        if (sNpc == null)
            return;

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem().getType() == Material.BARRIER) {
            player.closeInventory();
            return;
        }

        player.getInventory().addItem(event.getCurrentItem());
        event.getInventory().setItem(event.getSlot(), null);
    }
}
