package science.atlarge.opencraft.opencraft.block.itemtype;

import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.util.TickUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemPoisonousPotato extends ItemFood {

    public ItemPoisonousPotato() {
        super(2, 1.2f);
    }

    @Override
    public boolean eat(GlowPlayer player, ItemStack item) {
        if (!super.eat(player, item)) {
            return false;
        }

        if (Math.random() < 0.6) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,
                    TickUtil.secondsToTicks(5), 0), true);
        }
        return true;
    }
}
