package me.shershnyaga.squests.quests.rewards;

import me.shershnyaga.squests.quests.MathCount;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
public class ItemReward extends Reward {
    private Material material;
    private int rewardNow;

    public ItemReward(Material material, MathCount count) {
        super(count);
        this.material = material;

        rewardNow = count.getCount();
    }

    @Override
    public int getCountNow() {
        return rewardNow;
    }

    @Override
    public Component getRewardStr() {
        ItemStack item = new ItemStack(material);
        return Component.text(ChatColor.WHITE + "Награда: " + ChatColor.AQUA +
                item.getItemMeta().getLocalizedName() + " " + rewardNow);
    }

    @Override
    public void assignReward(Player player, int rewardCount) {
        player.getInventory().addItem(new ItemStack(material, rewardCount));
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
        player.getInventory().getItemInMainHand().setAmount(0);
    }
}
