package net.glowstone;

import net.glowstone.block.BlockProperties;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.net.message.Message;
import net.glowstone.net.message.game.ChunkDataMessage;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Represents a chunk of the map.
 * @author Graham Edgecombe
 */
public final class GlowChunk implements Chunk {

    /**
     * A chunk key represents the X and Z coordinates of a chunk and implements
     * the {@link #hashCode()} and {@link #equals(Object)} methods making it
     * suitable for use as a key in a hash table or set.
     * @author Graham Edgecombe
     */
    public static final class Key {

        /**
         * The coordinates.
         */
        private final int x, z;

        /**
         * Creates a new chunk key with the specified X and Z coordinates.
         * @param x The X coordinate.
         * @param z The Z coordinate.
         */
        public Key(int x, int z) {
            this.x = x;
            this.z = z;
        }

        /**
         * Gets the X coordinate.
         * @return The X coordinate.
         */
        public int getX() {
            return x;
        }

        /**
         * Gets the Z coordinate.
         * @return The Z coordinate.
         */
        public int getZ() {
            return z;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + z;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Key other = (Key) obj;
            if (x != other.x)
                return false;
            if (z != other.z)
                return false;
            return true;
        }

    }

    /**
     * The dimensions of a chunk (width: x, height: z, depth: y).
     */
    public static final int WIDTH = 16, HEIGHT = 16, DEPTH = 128;

    /**
     * The Y depth of a single chunk section.
     */
    private static final int SEC_DEPTH = 16;

    /**
     * A single cubic section of a chunk, with all data.
     */
    public static final class ChunkSection {
        private static final int ARRAY_SIZE = WIDTH * HEIGHT * SEC_DEPTH;

        // these probably should be made non-public
        public final byte[] types;
        public final byte[] metaData;
        public final byte[] skyLight;
        public final byte[] blockLight;

        /**
         * Create a new, empty ChunkSection.
         */
        public ChunkSection() {
            types = new byte[ARRAY_SIZE];
            metaData = new byte[ARRAY_SIZE];
            skyLight = new byte[ARRAY_SIZE];
            blockLight = new byte[ARRAY_SIZE];
        }

        /**
         * Create a ChunkSection with the specified chunk data.
         */
        public ChunkSection(byte[] types, byte[] metaData, byte[] skyLight, byte[] blockLight) {
            if (types.length != ARRAY_SIZE || metaData.length != ARRAY_SIZE || skyLight.length != ARRAY_SIZE || blockLight.length != ARRAY_SIZE) {
                throw new IllegalArgumentException("An array length was not " + ARRAY_SIZE + ": " + types.length + " " + metaData.length + " " + skyLight.length + " " + blockLight.length);
            }
            this.types = types;
            this.metaData = metaData;
            this.skyLight = skyLight;
            this.blockLight = blockLight;
            /*System.arraycopy(types, 0, this.types, 0, ARRAY_SIZE);
            System.arraycopy(metaData, 0, this.metaData, 0, ARRAY_SIZE);
            System.arraycopy(skyLight, 0, this.skyLight, 0, ARRAY_SIZE);
            System.arraycopy(blockLight, 0, this.blockLight, 0, ARRAY_SIZE);*/
        }

        public int index(int x, int y, int z) {
            if (x < 0 || z < 0 || x >= WIDTH || z >= HEIGHT) {
                throw new IndexOutOfBoundsException("Coords (x=" + x + ",z=" + z + ") out of section bounds");
            }
            return ((y & 0xf) << 8) | (z << 4) | x;
        }

        public boolean isEmpty() {
            for (byte type : types) {
                if (type != 0) return false;
            }
            return true;
        }
    }
    
    /**
     * The world of this chunk.
     */
    private final GlowWorld world;

    /**
     * The coordinates of this chunk.
     */
    private final int x, z;

    /**
     * The array of chunk sections this chunk contains, or null if it is unloaded.
     */
    private ChunkSection[] sections;
    
    /**
     * The tile entities that reside in this chunk.
     */
    private final HashMap<Integer, GlowBlockState> tileEntities = new HashMap<Integer, GlowBlockState>();
    
    /**
     * Whether the chunk has been populated by special features.
     * Used in map generation.
     */
    private boolean populated = false;

    /**
     * Creates a new chunk with a specified X and Z coordinate.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     */
    public GlowChunk(GlowWorld world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }
    
    // ======== Basic stuff ========

    public GlowWorld getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public GlowBlock getBlock(int x, int y, int z) {
        return getWorld().getBlockAt(this.x << 4 | x, y, this.z << 4 | z);
    }

    public Entity[] getEntities() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public GlowBlockState[] getTileEntities() {
        return tileEntities.values().toArray(new GlowBlockState[tileEntities.size()]);
    }

    public ChunkSnapshot getChunkSnapshot() {
        return getChunkSnapshot(true, false, false);
    }

    public ChunkSnapshot getChunkSnapshot(boolean includeMaxblocky, boolean includeBiome, boolean includeBiomeTempRain) {
        return null;
        //return new GlowChunkSnapshot(x, z, world, types, metaData, skyLight, blockLight, includeMaxblocky, includeBiome, includeBiomeTempRain);
    }
    
    /**
     * Gets whether this chunk has been populated by special features.
     * @return Population status.
     */
    public boolean getPopulated() {
        return populated;
    }
    
    /**
     * Sets the population status of this chunk.
     * @param populated Population status.
     */
    public void setPopulated(boolean populated) {
        this.populated = populated;
    }
    
    // ======== Helper Functions ========

    public boolean isLoaded() {
        return sections != null;
    }

    public boolean load() {
        return load(true);
    }

    public boolean load(boolean generate) {
        return isLoaded() || world.getChunkManager().loadChunk(x, z, generate);
    }

    public boolean unload() {
        return unload(true, true);
    }

    public boolean unload(boolean save) {
        return unload(save, true);
    }

    public boolean unload(boolean save, boolean safe) {
        if (safe && world.isChunkInUse(x, z)) {
            return false;
        }
        
        if (save && !world.getChunkManager().forceSave(x, z)) {
            return false;
        }

        sections = null;
        return true;
    }

    /**
     * Sets the types of all tiles within the chunk.
     * @param types The array of types.
     */
    public void initializeTypes(byte[] types) {
        if (isLoaded()) {
            GlowServer.logger.log(Level.SEVERE, "Tried to initialize already loaded chunk ({0},{1})", new Object[]{x, z});
            return;
        }

        this.sections = new ChunkSection[world.getMaxHeight() / SEC_DEPTH];

        final int oldDepth = 128;
        int height = types.length / (WIDTH * HEIGHT);
        if (height != oldDepth) {
            // old code using this method must provide the correct height
            throw new IllegalArgumentException("Types should have depth " + oldDepth + ", were " + height);
        }

        for (int y = 0; y < oldDepth; y += SEC_DEPTH) {
            ChunkSection sec = new ChunkSection();
            System.arraycopy(types, WIDTH * HEIGHT * y, sec.types, 0, sec.types.length);
            Arrays.fill(sec.skyLight, (byte) 15);
            sections[y / SEC_DEPTH] = sec;
        }
        
        for (int cx = 0; cx < WIDTH; ++cx) {
            for (int cy = 0; cy < oldDepth; ++cy) {
                for (int cz = 0; cz < HEIGHT; ++cz) {
                    createEntity(cx, cy, cz, getType(cx, cz, cy));
                }
            }
        }
    }

    /**
     * Initialize this chunk from the given sections.
     * @param sections The ChunkSections to use.
     */
    public void initializeSections(ChunkSection[] sections) {
        this.sections = new ChunkSection[DEPTH / SEC_DEPTH];
        System.arraycopy(sections, 0, this.sections, 0, this.sections.length);
    }

    /**
     * Get the ChunkSections contained in this chunk. Care should be taken that they are not modified.
     * @return The ChunkSection array.
     */
    public ChunkSection[] getSections() {
        return sections;
    }

    /**
     * If needed, create a new tile entity at the given location.
     */
    private void createEntity(int cx, int cy, int cz, int type) {
        BlockProperties properties = BlockProperties.get(type);
        if (properties == null) return;

        Class<? extends GlowBlockState> clazz = properties.getEntityClass();
        if (clazz == null || clazz == GlowBlockState.class) return;

        try {
            Constructor<? extends GlowBlockState> constructor = clazz.getConstructor(GlowBlock.class);
            GlowBlockState state = constructor.newInstance(getBlock(cx, cy, cz));
            tileEntities.put(coordToIndex(cx, cz, cy), state);
        } catch (Exception ex) {
            GlowServer.logger.log(Level.SEVERE, "Unable to initialize tile entity {0}: {1}", new Object[]{clazz.getName(), ex.getMessage()});
            ex.printStackTrace();
        }
    }

    // ======== Data access ========

    /**
     * Attempt to get the ChunkSection at the specified height.
     * @param y the y value.
     * @return The ChunkSection, or null if it is empty.
     */
    private ChunkSection getSection(int y) {
        if (y < 0 || y >= DEPTH) {
            return null;
        }
        if (!isLoaded() && !load()) {
            return null;
        }
        int idx = y >> 4;
        if (idx >= sections.length) {
            return null;
        }
        return sections[idx];
    }
    
    /**
     * Attempt to get the tile entity located at the given coordinates.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return A GlowBlockState if the entity exists, or null otherwise.
     */
    public GlowBlockState getEntity(int x, int y, int z) {
        if (y >= world.getMaxHeight() || y < 0) return null;
        load();
        return tileEntities.get(coordToIndex(x, z, y));
    }

    /**
     * Gets the type of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The type.
     */
    public int getType(int x, int z, int y) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : (section.types[section.index(x, y, z)] & 0xff);
    }

    /**
     * Sets the type of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @param type The type.
     */
    public void setType(int x, int z, int y, int type) {
        if (type < 0 || type >= 256)
            throw new IllegalArgumentException("Block type out of range: " + type);

        ChunkSection section = getSection(y);
        if (section == null) {
            if (type == 0) {
                // don't need to create chunk for air
                return;
            } else {
                section = new ChunkSection();
            }
        }

        // destroy any tile entity there
        if (tileEntities.containsKey(coordToIndex(x, z, y))) {
            getEntity(x, y, z).destroy();
            tileEntities.remove(coordToIndex(x, z, y));
        }

        // update the type
        section.types[section.index(x, y, z)] = (byte) type;

        if (type == 0 && section.isEmpty()) {
            // destroy the empty section
            sections[y / SEC_DEPTH] = null;
            return;
        }

        // create a new tile entity if we need
        createEntity(x, y, z, type);
    }

    /**
     * Gets the metadata of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The metadata.
     */
    public int getMetaData(int x, int z, int y) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : section.metaData[section.index(x, y, z)];
    }

    /**
     * Sets the metadata of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @param metaData The metadata.
     */
    public void setMetaData(int x, int z, int y, int metaData) {
        if (metaData < 0 || metaData >= 16)
            throw new IllegalArgumentException("Metadata out of range: " + metaData);
        ChunkSection section = getSection(y);
        if (section == null) return;  // can't set metadata on an empty section
        section.metaData[section.index(x, y, z)] = (byte) metaData;
    }

    /**
     * Gets the sky light level of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The sky light level.
     */
    public byte getSkyLight(int x, int z, int y) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : section.skyLight[section.index(x, y, z)];
    }

    /**
     * Sets the sky light level of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @param skyLight The sky light level.
     */
    public void setSkyLight(int x, int z, int y, int skyLight) {
        ChunkSection section = getSection(y);
        if (section == null) return;  // can't set light on an empty section
        section.skyLight[section.index(x, y, z)] = (byte) skyLight;
    }

    /**
     * Gets the block light level of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The block light level.
     */
    public byte getBlockLight(int x, int z, int y) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : section.blockLight[section.index(x, y, z)];
    }

    /**
     * Sets the block light level of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @param blockLight The block light level.
     */
    public void setBlockLight(int x, int z, int y, int blockLight) {
        ChunkSection section = getSection(y);
        if (section == null) return;  // can't set light on an empty section
        section.blockLight[section.index(x, y, z)] = (byte) blockLight;
    }

    public byte[] getTypes() {
        return new byte[16 * 16 * 128];
        //load();
        //return types.clone();
    }
    
    // ======== Helper functions ========

    /**
     * Converts a three-dimensional coordinate to an index within the
     * one-dimensional arrays.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The index within the arrays.
     */
    private int coordToIndex(int x, int z, int y) {
        if (y >= world.getMaxHeight())
            y = 127; // temporary hack because the world height is wrong
        if (x < 0 || z < 0 || y < 0 || x >= WIDTH || z >= HEIGHT || y >= world.getMaxHeight())
            throw new IndexOutOfBoundsException("Coords (x=" + x + ",y=" + y + ",z=" + z + ") invalid");

        return (y * 16 + z) * 16 + x;  // make constants cleaner later
        //return (x * HEIGHT + z) * world.getMaxHeight() + y;
    }

    /**
     * Creates a new {@link Message} which can be sent to a client to stream
     * this entire chunk to them.
     * @return The {@link ChunkDataMessage}.
     */
    public Message toMessage() {
        load();
        return toMessage(true, 0);
    }

    /**
     * Creates a new {@link Message} which can be sent to a client to stream
     * parts of this chunk to them.
     * @return The {@link ChunkDataMessage}.
     */
    public Message toMessage(boolean entireChunk, int sectionBitmask) {
        // filter sectionBitmask based on actual chunk contents
        int sectionCount;
        if (sections == null) {
            sectionBitmask = 0;
            sectionCount = 0;
        } else {
            final int maxBitmask = (1 << sections.length) - 1;
            if (entireChunk) {
                sectionBitmask = maxBitmask;
                sectionCount = sections.length;
            } else {
                sectionBitmask &= maxBitmask;
                sectionCount = countBits(sectionBitmask);
            }

            for (int i = 0; i < sections.length; ++i) {
                if (sections[i] == null) {
                    // remove empty sections from bitmask
                    sectionBitmask &= ~(1 << i);
                    sectionCount--;
                }
            }
        }

        // break out early if there's nothing to send
        if (sections == null || sectionBitmask == 0) {
            return new ChunkDataMessage(x, z, entireChunk, 0, 0, new byte[0]);
        }

        // in future, take care of additional data
        int additionalBitmask = 0, additionalCount = 0;

        boolean skylight = world.getEnvironment() == World.Environment.NORMAL;

        // calculate how big the data will need to be
        int numBlocks = WIDTH * HEIGHT * SEC_DEPTH;
        int sectionSize = numBlocks * 2;  // data + metaData/2 + blockLight/2
        if (skylight) {
            sectionSize += numBlocks / 2;  // + skyLight/2
        }
        int byteSize = sectionCount * sectionSize + additionalCount * (numBlocks / 2);  // + additional/2
        if (entireChunk) {
            byteSize += 256;  // + biomes
        }

        // get the list of sections
        ChunkSection[] sendSections = new ChunkSection[sectionCount];
        int pos = 0;
        for (int i = 0; i < sections.length; ++i) {
            if ((sectionBitmask & (1 << i)) != 0) {
                sendSections[pos++] = sections[i];
            }
        }

        // fill up the data
        byte[] tileData = new byte[byteSize];
        pos = 0;

        for (ChunkSection sec : sendSections) {
            System.arraycopy(sec.types, 0, tileData, pos, sec.types.length);
            pos += sec.types.length;
        }

        for (ChunkSection sec : sendSections) {
            byte[] metaData = sec.metaData;
            for (int i = 0; i < metaData.length; i += 2) {
                byte meta1 = metaData[i];
                byte meta2 = metaData[i + 1];
                tileData[pos++] = (byte) ((meta2 << 4) | meta1);
            }
        }

        for (ChunkSection sec : sendSections) {
            byte[] blockLight = sec.blockLight;
            for (int i = 0; i < blockLight.length; i += 2) {
                byte light1 = blockLight[i];
                byte light2 = blockLight[i + 1];
                tileData[pos++] = (byte) ((light2 << 4) | light1);
            }
        }

        for (ChunkSection sec : sendSections) {
            if (!skylight) break;
            byte[] skyLight = sec.skyLight;
            for (int i = 0; i < skyLight.length; i += 2) {
                byte light1 = skyLight[i];
                byte light2 = skyLight[i + 1];
                tileData[pos++] = (byte) ((light2 << 4) | light1);
            }
        }

        // additional data goes here using additionalBitmask

        // biomes
        if (entireChunk) {
            for (int i = 0; i < 256; ++i) {
                tileData[pos++] = 0;
            }
        }

        if (pos != byteSize) {
            throw new IllegalStateException("only wrote " + pos + " out of expected " + byteSize + " bytes");
        }

        return new ChunkDataMessage(x, z, entireChunk, sectionBitmask, additionalBitmask, tileData);
    }

    private int countBits(int v) {
        // http://graphics.stanford.edu/~seander/bithacks.html#CountBitsSetKernighan
        int c;
        for (c = 0; v > 0; c++) {
            v &= v - 1;
        }
        return c;
    }

}
