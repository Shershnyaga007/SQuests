package me.shershnyaga.squests.npc;

import me.shershnyaga.squests.SQuests;
import me.shershnyaga.squests.quests.conditions.Quest;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SNpc{

    public final static NamespacedKey SNPC_ID = NamespacedKey.fromString("snpc_id");
    public static final List<SNpc> S_NPC_LIST = new ArrayList<>();

    public final String id;
    public final String name;
    public final List<Quest> questsList;
    private final int updateTime;
    public final Inventory sNPCInventory;

    public SNpc(String id, String name, List<Quest> questsList, int updateTime) {
        this.id = id;
        this.name = name;
        this.questsList = questsList;
        this.updateTime = updateTime;

        sNPCInventory = createNpcInv(name);

        genQuestsInv();
    }

    public static SNpc getSNpcFromId(String id) {
        for(SNpc sNpc: S_NPC_LIST)
            if (sNpc.id.equals(id))
                return sNpc;

        return null;
    }

    private Inventory createNpcInv(String name) {
        final Inventory sNPCInventory;
        sNPCInventory = Bukkit.createInventory(null, 18, name);
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(ChatColor.RED + "Выход"));
        item.setItemMeta(meta);
        sNPCInventory.setItem(13, item);
        return sNPCInventory;
    }

    public void spawn(Location location) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);

        SNpcTrait trait = new SNpcTrait();
        trait.name = id;
        npc.addTrait(trait);

        npc.spawn(location);
        Entity entity = npc.getEntity();

        entity.getPersistentDataContainer().set(Objects.requireNonNull(SNPC_ID),
                PersistentDataType.STRING, this.id);

        genQuestsInv();
    }

    private void genQuestsInv() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(SQuests.getPlugin(), () -> {
            for (int i=0; i<9; i++) {
                sNPCInventory.setItem(i, null);
            }

            Collections.shuffle(questsList);

            if (questsList.size() < 9 && questsList.size() > 0) {
                for (int i=0; i < questsList.size(); i++) {
                    ItemStack item = questsList.get(i).getQuestItem();
                    sNPCInventory.setItem(i, item);
                }
            }
            else {
                for(int i=0; i < 9; i++) {
                    ItemStack item = questsList.get(i).getQuestItem();
                    sNPCInventory.setItem(i, item);
                }
            }
        }, 0, updateTime * 20L);
    }

    private Quest getQuestFromStr(String questId) {
        for (Quest quest: questsList) {

            if (quest.id.equals(questId))
                return quest;

        }
        return null;
    }
}
