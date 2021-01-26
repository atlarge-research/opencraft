package science.atlarge.opencraft.opencraft.block.itemtype;

import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.entity.EntityRegistry;
import science.atlarge.opencraft.opencraft.entity.GlowEntity;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.entity.physics.BlockBoundingBoxes;
import science.atlarge.opencraft.opencraft.inventory.GlowMetaSpawn;
import science.atlarge.opencraft.opencraft.io.entity.EntityStorage;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemSpawn extends ItemType {

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock against, BlockFace face,
                                ItemStack holding, Vector clickedLoc, EquipmentSlot hand) {
        Location location = against.getLocation()
            .add(face.getModX(), face.getModY(), face.getModZ());
        // TODO: change mob spawner when clicked by monster egg
        if (holding.hasItemMeta() && holding.getItemMeta() instanceof GlowMetaSpawn) {
            GlowMetaSpawn meta = (GlowMetaSpawn) holding.getItemMeta();
            EntityType type = meta.getSpawnedType();
            CompoundTag tag = meta.getEntityTag();

            if (face == BlockFace.UP) {
                if (BlockBoundingBoxes.getBoundingBoxes(against).stream().anyMatch(box -> box.getSize().getY() > 1.0)) {
                    location.add(0, 0.5, 0);
                }
            }

            if (type != null) {
                GlowEntity entity = against.getWorld().spawn(
                       location.add(0.5, GlowEntity.COLLISION_OFFSET, 0.5),
                       EntityRegistry.getEntity(type),
                       SpawnReason.SPAWNER_EGG
                );
                if (tag != null) {
                    EntityStorage.load(entity, tag);
                }
                if (player.getGameMode() != GameMode.CREATIVE) {
                    holding.setAmount(holding.getAmount() - 1);
                }
            }
        }
    }
}
