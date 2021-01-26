package science.atlarge.opencraft.opencraft.messaging.policies;

import java.util.Objects;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

/**
 * The simplified chunk class represents a chunk without actual content that can be used to test the chunk policy.
 */
final class SimpleChunk implements Chunk {

    private final World world;
    private final int x;
    private final int z;

    /**
     * Create a simple chunk.
     *
     * @param world the chunk's world.
     * @param x the chunk's x-coordinate.
     * @param z the chunk's z - coordinate.
     */
    SimpleChunk(World world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Block getBlock(int i, int i1, int i2) {
        return null;
    }

    @Override
    public ChunkSnapshot getChunkSnapshot() {
        return null;
    }

    @Override
    public ChunkSnapshot getChunkSnapshot(boolean b, boolean b1, boolean b2) {
        return null;
    }

    @Override
    public Entity[] getEntities() {
        return new Entity[0];
    }

    @Override
    public BlockState[] getTileEntities() {
        return new BlockState[0];
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public boolean load(boolean b) {
        return false;
    }

    @Override
    public boolean load() {
        return false;
    }

    @Override
    public boolean unload(boolean b, boolean b1) {
        return false;
    }

    @Override
    public boolean unload(boolean b) {
        return false;
    }

    @Override
    public boolean unload() {
        return false;
    }

    @Override
    public boolean isSlimeChunk() {
        return false;
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        SimpleChunk that = (SimpleChunk) other;
        return x == that.x && z == that.z && Objects.equals(world, that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }
}