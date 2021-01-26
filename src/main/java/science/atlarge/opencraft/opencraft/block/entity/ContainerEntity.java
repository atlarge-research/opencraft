package science.atlarge.opencraft.opencraft.block.entity;

import lombok.Getter;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.inventory.GlowInventory;
import science.atlarge.opencraft.opencraft.io.nbt.NbtSerialization;
import science.atlarge.opencraft.opencraft.population.serialization.json.annotations.ExcludeField;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.inventory.ItemStack;

/**
 * Base class for container block entities (those with inventories).
 */
public abstract class ContainerEntity extends BlockEntity {

    @Getter
    @ExcludeField
    private final GlowInventory inventory;

    public ContainerEntity(GlowBlock block, GlowInventory inventory) {
        super(block);
        this.inventory = inventory;
    }

    public void setContents(ItemStack... contents) {
        inventory.setContents(contents);
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        tag.readCompoundList("Items", items ->
            inventory.setContents(NbtSerialization.readInventory(items, 0, inventory.getSize()))
        );
        tag.readString("CustomName", inventory::setTitle);
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        tag.putCompoundList("Items", NbtSerialization.writeInventory(inventory.getContents(), 0));
        if (!inventory.getTitle().equals(inventory.getType().getDefaultTitle())) {
            tag.putString("CustomName", inventory.getTitle());
        }
    }
}
