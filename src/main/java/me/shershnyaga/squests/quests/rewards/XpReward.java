package me.shershnyaga.squests.quests.rewards;

import me.shershnyaga.squests.quests.MathCount;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class XpReward extends Reward {
    public int rewardNow;
    public XpReward(MathCount count) {
        super(count);

        rewardNow = count.getCount();
    }

    @Override
    public int getCountNow() {
        return rewardNow;
    }

    @Override
    public Component getRewardStr() {
        return Component.text(ChatColor.WHITE + "Награда: " + ChatColor.AQUA +
                 rewardNow + " опыта");
    }

    @Override
    public void assignReward(Player player, int count) {
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
        player.giveExp(count);
    }
}
