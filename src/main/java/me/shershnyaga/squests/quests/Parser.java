package me.shershnyaga.squests.quests;

import me.shershnyaga.squests.SQuests;
import me.shershnyaga.squests.npc.SNpc;
import me.shershnyaga.squests.quests.conditions.find.FindQuest;
import me.shershnyaga.squests.quests.conditions.item.ItemQuest;
import me.shershnyaga.squests.quests.conditions.Quest;
import me.shershnyaga.squests.quests.rewards.ItemReward;
import me.shershnyaga.squests.quests.rewards.Reward;
import me.shershnyaga.squests.quests.rewards.XpReward;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Parser {

    private static final FileConfiguration CONFIG = SQuests.getPlugin().getConfig();
    private static final String CONFIG_PATH_TO_QUESTS = "Quests.";
    private static final String CONFIG_PATH_TO_NPCS = "NPC.";
    public static SNpc parseSNPC(String snpcId) {
        String name = ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(CONFIG.getString(CONFIG_PATH_TO_NPCS + snpcId + ".Name")));
        int updateTime = CONFIG.getInt(CONFIG_PATH_TO_NPCS + snpcId + ".UpdateTime");
        List<String> questsList = CONFIG.getStringList(CONFIG_PATH_TO_NPCS + snpcId + ".Quests");

        List<Quest> quests = new ArrayList<>();

        for (String questId: questsList) {
            quests.add(parseQuest(questId));
        }

        SNpc sNpc = new SNpc(snpcId, name, quests, updateTime);

        boolean isRegistered = false;
        for (SNpc sNpc1: SNpc.S_NPC_LIST)
            if (sNpc1.id.equals(snpcId)) {
                isRegistered = true;
                break;
            }

        if (!isRegistered)
            SNpc.S_NPC_LIST.add(sNpc);

        return sNpc;
    }

    public static Quest parseQuest(String questId) {
        Component name = Component.text(ChatColor.translateAlternateColorCodes('&',
                ChatColor.WHITE + Objects.requireNonNull(
                CONFIG.getString(CONFIG_PATH_TO_QUESTS + questId + ".Name"))));

        Component description = Component.text(ChatColor.translateAlternateColorCodes('&',
                ChatColor.WHITE +  Objects.requireNonNull(
                        CONFIG.getString(CONFIG_PATH_TO_QUESTS + questId + ".Description"))));

        String questStr = CONFIG.getString(CONFIG_PATH_TO_QUESTS + questId + ".Quest");

        Reward reward = parseReward(questId);

        String[] words = Objects.requireNonNull(questStr).split(" ");

        MathCount count;
        Quest quest = null;

        switch (words[0].toLowerCase()) {
            case "find":
                Material materialTofFind = Material.matchMaterial(words[1]);

                if (questStr.contains("RANDOM"))
                    count = getCount(Integer.parseInt(words[3]), Integer.parseInt(words[4]));
                else
                    count = getCount(Integer.parseInt(words[2]));

                quest = new ItemQuest(questId, name, description, reward, new ItemQuest.FindItems(
                        materialTofFind, count
                ));
                break;
            case "visit":
                Biome biomeToVisit = Biome.valueOf(words[1]);
                quest = new FindQuest(questId, name, description, reward, biomeToVisit);
                break;
        }

        boolean isRegistered = false;
        for (Quest quest1: Quest.QUEST_LIST)
            if (quest1.id.equals(questId)) {
                isRegistered = true;
                break;
            }

        if (!isRegistered)
            Quest.QUEST_LIST.add(quest);

        return quest;
    }

    public static Reward parseReward(String questId) {
        String rewardStr = CONFIG.getString(CONFIG_PATH_TO_QUESTS + questId + ".Reward");
        String[] words = Objects.requireNonNull(rewardStr).split(" ");

        MathCount count;

        switch (words[0]) {
            case "ITEM": {
                Material materialTofFind = Material.matchMaterial(words[1]);

                if (rewardStr.contains("RANDOM"))
                    count = getCount(Integer.parseInt(words[3]), Integer.parseInt(words[4]));
                else
                    count = getCount(Integer.parseInt(words[2]));

                return new ItemReward(materialTofFind, count);
            }
            case "XP": {
                if (rewardStr.contains("RANDOM"))
                    count = getCount(Integer.parseInt(words[2]), Integer.parseInt(words[3]));
                else
                    count = getCount(Integer.parseInt(words[1]));

                return new XpReward(count);
            }
            default: return null;
        }
    }

    private static MathCount getCount(int min, int max) {
        return () -> {
                    Random random = new Random(System.currentTimeMillis());
                    return random.nextInt(max - min + 1) + max;
                };
        }

    private static MathCount getCount(int num) {
        return () -> num;
    }
}
