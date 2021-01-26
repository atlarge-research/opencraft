package science.atlarge.opencraft.opencraft.block.itemtype;

import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparingInt;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Art;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.chunk.GlowChunk;
import science.atlarge.opencraft.opencraft.entity.GlowPlayer;
import science.atlarge.opencraft.opencraft.entity.objects.GlowPainting;

public class ItemPainting extends ItemType {

    /**
     * Contains all Arts. Key is the size of the art in descending order.
     */
    private static final ListMultimap<GlowChunk.Key, Art> ART_BY_SIZE;

    static {
        ART_BY_SIZE = MultimapBuilder.treeKeys(
                reverseOrder(
                        comparingInt(GlowChunk.Key::getX)
                                .thenComparingInt(GlowChunk.Key::getZ)
                )
        ).arrayListValues().build();

        Arrays.stream(Art.values()).forEach(art -> ART_BY_SIZE
                .put(GlowChunk.Key.of(art.getBlockHeight(), art.getBlockWidth()), art));
    }

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face,
            ItemStack holding, Vector clickedLoc, EquipmentSlot hand) {
        Location center = target.getRelative(face).getLocation();
        GlowPainting painting = new GlowPainting(center, face);

        for (GlowChunk.Key key : ART_BY_SIZE.keySet()) {
            List<Art> arts = ART_BY_SIZE.get(key);
            painting.setArtInternal(arts.get(0));

            if (!painting.isObstructed()) {
                int i = ThreadLocalRandom.current().nextInt(0, arts.size());
                painting.setArtInternal(arts.get(i));
                return;
            }
        }

        player.playSound(center, Sound.ENTITY_PAINTING_PLACE, SoundCategory.NEUTRAL, 1.0f, 1.0f);
    }
}
