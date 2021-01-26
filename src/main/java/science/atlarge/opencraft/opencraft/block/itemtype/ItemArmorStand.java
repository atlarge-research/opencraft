package science.atlarge.opencraft.opencraft.block.itemtype;

import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.ItemTable;
import science.atlarge.opencraft.opencraft.block.blocktype.BlockType;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.entity.objects.GlowArmorStand;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemArmorStand extends ItemType {

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face,
                                ItemStack holding, Vector clickedLoc, EquipmentSlot hand) {
        BlockType type = ItemTable.instance().getBlock(target.getType());

        GlowBlock newTarget =
            type.canAbsorb(target, face, holding) ? target : target.getRelative(face);
        type = ItemTable.instance().getBlock(newTarget.getType());

        GlowBlock upper = newTarget.getRelative(BlockFace.UP);
        BlockType up = ItemTable.instance().getBlock(upper.getType());

        Location loc = newTarget.getLocation().add(0.5, 0, 0.5);
        if ((newTarget.isEmpty() || type == null || type.canAbsorb(target, face, holding))
            && (upper.isEmpty() || up == null || up.canAbsorb(target, face, holding))
            && loc.getWorld().getNearbyEntities(loc.clone().add(0, 0.5, 0), 0.5, 0.5, 0.5).isEmpty()
            && loc.getWorld().getNearbyEntities(loc.clone().add(0, 1.5, 0), 0.5, 0.5, 0.5)
            .isEmpty()) {
            newTarget.setType(Material.AIR);
            upper.setType(Material.AIR);
            float yaw = player.getLocation().getYaw();
            float finalYaw = Math.round(yaw / 22.5f / 2) * 45;
            loc.setYaw(finalYaw - 180);
            ((GlowWorld) loc.getWorld())
                .spawn(loc, GlowArmorStand.class, CreatureSpawnEvent.SpawnReason.DEFAULT);
            if (player.getGameMode() != GameMode.CREATIVE) {
                holding.setAmount(holding.getAmount() - 1);
            }
        }
    }
}
