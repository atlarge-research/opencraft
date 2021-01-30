package science.atlarge.opencraft.opencraft.block.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;
import science.atlarge.opencraft.opencraft.block.blocktype.BlockBanner;
import science.atlarge.opencraft.opencraft.block.entity.state.GlowBanner;
import science.atlarge.opencraft.opencraft.constants.GlowBlockEntity;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.util.nbt.CompoundTag;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;

public class BannerEntity extends BlockEntity {

    @Getter
    @Setter
    private DyeColor base = DyeColor.WHITE;
    private List<Pattern> patterns = new ArrayList<>();

    public BannerEntity(GlowBlock block) {
        super(block);
        setSaveId("minecraft:banner");
    }

    @Override
    public void loadNbt(CompoundTag tag) {
        super.loadNbt(tag);
        tag.readCompoundList("Patterns",
            patternTags -> patterns = BlockBanner.fromNbt(patternTags));
        tag.readInt("Base", color -> base = DyeColor.getByDyeData((byte) color));
    }

    @Override
    public void saveNbt(CompoundTag tag) {
        super.saveNbt(tag);
        tag.putCompoundList("Patterns", BlockBanner.toNbt(patterns));
        tag.putInt("Base", base.getDyeData());
    }

    @Override
    public GlowBlockState getState() {
        return new GlowBanner(block);
    }

    @Override
    public void update(GlowPlayer player) {
        super.update(player);
        CompoundTag nbt = new CompoundTag();
        GlowWorld world = player.getWorld();
        saveNbt(nbt);
        // TODO: it is possible that this causes a broadcast message to be sent multiple times.
        world.sendBlockEntityChange(getBlock().getLocation(), GlowBlockEntity.BANNER, nbt);
    }

    public List<Pattern> getPatterns() {
        // TODO: Defensive copy?
        return patterns;
    }

    public void setPatterns(List<Pattern> patterns) {
        // TODO: Defensive copy
        this.patterns = patterns;
    }
}
