package me.shershnyaga.squests.quests.rewards;

import me.shershnyaga.squests.quests.MathCount;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public abstract class Reward {
    public final static NamespacedKey REWARD_COUNT = NamespacedKey.fromString("reward");
    public final MathCount count;
    public Reward(MathCount count) {
        this.count = count;
    }
    public abstract int getCountNow();
    public abstract Component getRewardStr();
    public abstract void assignReward(Player player, int count);
}
