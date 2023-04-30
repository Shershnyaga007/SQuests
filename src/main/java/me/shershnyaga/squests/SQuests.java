package me.shershnyaga.squests;

import lombok.Getter;
import me.shershnyaga.squests.commands.SQuestsCommand;
import me.shershnyaga.squests.commands.SQuestsTabCompleter;
import me.shershnyaga.squests.db.Database;
import me.shershnyaga.squests.npc.SNpcListener;
import me.shershnyaga.squests.npc.SNpcTrait;
import me.shershnyaga.squests.quests.Parser;
import me.shershnyaga.squests.quests.conditions.find.FindQuestListener;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Set;

public class SQuests extends JavaPlugin implements Listener {

    @Getter
    private static SQuests plugin;
    public static Database database;

    @Override
    public void onEnable() {
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(SNpcTrait.class));
        plugin = this;
        saveDefaultConfig();
        reloadConfig();

        Bukkit.getPluginManager().registerEvents(new SNpcListener(), this);
        Bukkit.getPluginManager().registerEvents(new FindQuestListener(), this);
        Bukkit.getPluginManager().registerEvents(this, this);

        database = new Database();

        Objects.requireNonNull(getServer().getPluginCommand("squests")).setExecutor(new SQuestsCommand());
        Objects.requireNonNull(getServer().getPluginCommand("squests")).setTabCompleter(
                new SQuestsTabCompleter());
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        Set<String> npcsList = getConfig().getConfigurationSection("NPC").getKeys(false);
        for (String npcId: npcsList) {
            getLogger().info("Register SNpc " + npcId);
            Parser.parseSNPC(npcId);
        }
    }

    @EventHandler
    private void onPlayerConnect(PlayerJoinEvent event) {
        database.createUser(event.getPlayer().getName(), 1);
    }

}
