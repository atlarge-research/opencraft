package net.glowstone.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.ServerProvider;
import net.glowstone.block.MaterialValueManager.ValueCollection;
import net.glowstone.block.blocktype.BlockRedstone;
import net.glowstone.block.blocktype.BlockRedstoneTorch;
import net.glowstone.block.blocktype.BlockType;
import net.glowstone.block.entity.BlockEntity;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.entity.physics.BoundingBox;
import net.glowstone.net.message.play.game.BlockChangeMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import org.bukkit.material.Diode;
import org.bukkit.material.Gate;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Skull;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import org.bukkit.material.WoodenStep;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataStoreBase;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * Represents a single block in a world.
 */
public class GlowBlock implements Block {

    /**
     * The BlockFaces of a single-layer 3x3 area.
     */
    private static final BlockFace[] LAYER = new BlockFace[]{
        BlockFace.NORTH_WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SELF,
        BlockFace.WEST, BlockFace.SOUTH_WEST, BlockFace.SOUTH, BlockFace.SOUTH_EAST};

    /**
     * The BlockFaces of all directly adjacent.
     */
    private static final BlockFace[] ADJACENT = new BlockFace[]{
        BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP,
        BlockFace.DOWN};

    /**
     * The metadata store for blocks.
     */
    private static final MetadataStore<Block> metadata = new BlockMetadataStore();
    private static final Map<GlowBlock, List<Long>> counterMap = new HashMap<>();

    @Getter
    private final int x;
    @Getter
    private final int y;
    @Getter
    private final int z;
    @Getter
    private GlowWorld world;

    /**
     * Creates an object to refer to a block.
     *
     * @param chunk the chunk
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    public GlowBlock(GlowChunk chunk, int x, int y, int z) {
        world = chunk.getWorld();
        this.x = x;
        this.y = Math.min(256, Math.max(y, 0));
        this.z = z;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Basics
    @Override
    public GlowChunk getChunk() {
        return (GlowChunk) world.getChunkAt(this);
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), x, y, z);
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc == null) {
            return null;
        }
        loc.setWorld(getWorld());
        loc.setX(x);
        loc.setY(y);
        loc.setZ(z);
        return loc;
    }

    public BlockEntity getBlockEntity() {
        return getChunk().getEntity(x & 0xf, y, z & 0xf);
    }

    @Override
    public GlowBlockState getState() {
        BlockEntity entity = getBlockEntity();
        if (entity != null) {
            GlowBlockState state = entity.getState();
            if (state != null) {
                return state;
            }
        }
        return new GlowBlockState(this);
    }

    @Override
    public GlowBlockState getState(boolean useSnapshot) {
        return getState(); // TODO: disable use of snapshot
    }

    @Override
    public Biome getBiome() {
        return getWorld().getBiome(x, z);
    }

    @Override
    public void setBiome(Biome bio) {
        getWorld().setBiome(x, z, bio);
    }

    @Override
    public double getTemperature() {
        return getWorld().getTemperature(x, z);
    }

    @Override
    public double getHumidity() {
        return getWorld().getHumidity(x, z);
    }

    ////////////////////////////////////////////////////////////////////////////
    // getFace & getRelative
    @Override
    public BlockFace getFace(Block block) {
        for (BlockFace face : BlockFace.values()) {
            if (x + face.getModX() == block.getX() && y + face.getModY() == block.getY()
                    && z + face.getModZ() == block.getZ()) {
                return face;
            }
        }
        return null;
    }

    @Override
    public GlowBlock getRelative(int modX, int modY, int modZ) {
        return getWorld().getBlockAt(x + modX, y + modY, z + modZ);
    }

    @Override
    public GlowBlock getRelative(BlockFace face) {
        return getRelative(face.getModX(), face.getModY(), face.getModZ());
    }

    @Override
    public GlowBlock getRelative(BlockFace face, int distance) {
        return getRelative(
                face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Type and typeid getters/setters
    @Override
    public Material getType() {
        return Material.getMaterial(getTypeIdNoCache());
    }

    @Override
    public int getTypeId() {
        return getTypeIdNoCache();
    }

    @Deprecated
    private int getTypeIdNoCache() {
        return ((GlowChunk) world.getChunkAt(this)).getType(x & 0xf, z & 0xf, y);
    }

    @Override
    public void setType(Material type) {
        setTypeId(type.getId());
    }

    /**
     * Set the Material type of a block and optionally apply physics.
     */
    @Override
    public void setType(Material type, boolean applyPhysics) {
        setTypeId(type.getId(), applyPhysics);
    }

    /**
     * Set the Material type of a block with data and optionally apply physics.
     *
     * @param type The type to set the block to.
     * @param data The raw data to set the block to.
     * @param applyPhysics notify this block and surrounding blocks to update physics
     */
    public void setType(Material type, byte data, boolean applyPhysics) {
        setTypeIdAndData(type.getId(), data, applyPhysics);
    }

    @Override
    public boolean setTypeId(int type) {
        return setTypeId(type, true);
    }

    @Override
    public boolean setTypeId(int type, boolean applyPhysics) {
        return setTypeIdAndData(type, (byte) 0, applyPhysics);
    }

    @Override
    public boolean setTypeIdAndData(int type, byte data, boolean applyPhysics) {
        Material oldTypeId = getType();
        byte oldData = getData();

        GlowChunk chunk = (GlowChunk) world.getChunkAt(this);
        chunk.setType(x & 0xf, z & 0xf, y, type);
        chunk.setMetaData(x & 0xf, z & 0xf, y, data);

        if (oldTypeId == Material.DOUBLE_PLANT
                && getRelative(BlockFace.UP).getType() == Material.DOUBLE_PLANT) {
            world.getChunkAtAsync(this, c -> ((GlowChunk) c).setType(x & 0xf, z & 0xf, y + 1, 0));
            GlowChunk.Key key = GlowChunk.Key.of(x >> 4, z >> 4);
            BlockChangeMessage bcmsg = new BlockChangeMessage(x, y + 1, z, 0, 0);
            world.broadcastBlockChangeInRange(key, bcmsg);
        }

        if (applyPhysics) {
            applyPhysics(oldTypeId, type, oldData, data);
        }

        GlowChunk.Key key = GlowChunk.Key.of(x >> 4, z >> 4);
        BlockChangeMessage bcmsg = new BlockChangeMessage(x, y, z, type, data);
        world.broadcastBlockChangeInRange(key, bcmsg);

        return true;
    }

    @Override
    public boolean isEmpty() {
        return getTypeId() == 0;
    }

    @Override
    public boolean isLiquid() {
        Material mat = getType();
        return mat == Material.WATER || mat == Material.STATIONARY_WATER || mat == Material.LAVA
                || mat == Material.STATIONARY_LAVA;
    }

    /**
     * Get block material's flammable ability. (ability to have fire spread to it)
     *
     * @return if this block is flammable
     */
    public boolean isFlammable() {
        return getMaterialValues().getFlameResistance() >= 0;
    }

    /**
     * Get block material's burn ability. (ability to have fire consume it)
     *
     * @return if this block is burnable
     */
    public boolean isBurnable() {
        return getMaterialValues().getFireResistance() >= 0;
    }

    public ValueCollection getMaterialValues() {
        return ((GlowServer) ServerProvider.getServer())
                .getMaterialValueManager().getValues(getType());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Data and light getters/setters
    @Override
    public byte getData() {
        return (byte) ((GlowChunk) world.getChunkAt(this)).getMetaData(x & 0xf, z & 0xf, y);
    }

    @Override
    public void setData(byte data) {
        setData(data, true);
    }

    @Override
    public void setData(byte data, boolean applyPhysics) {
        byte oldData = getData();
        ((GlowChunk) world.getChunkAt(this)).setMetaData(x & 0xf, z & 0xf, y, data);
        if (applyPhysics) {
            applyPhysics(getType(), getTypeId(), oldData, data);
        }

        GlowChunk.Key key = GlowChunk.Key.of(x >> 4, z >> 4);
        BlockChangeMessage bcmsg = new BlockChangeMessage(x, y, z, getTypeId(), data);
        world.broadcastBlockChangeInRange(key, bcmsg);
    }

    @Override
    public byte getLightLevel() {
        return (byte) Math.max(getLightFromSky(), getLightFromBlocks());
    }

    @Override
    public byte getLightFromSky() {
        return ((GlowChunk) world.getChunkAt(this)).getSkyLight(x & 0xf, z & 0xf, y);
    }

    @Override
    public byte getLightFromBlocks() {
        return ((GlowChunk) world.getChunkAt(this)).getBlockLight(x & 0xf, z & 0xf, y);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Redstone
    @Override
    public boolean isBlockPowered() {
        // Strong powered?

        if (getType() == Material.REDSTONE_BLOCK) {
            return true;
        }

        if (getType() == Material.LEVER && ((Lever) getState().getData()).isPowered()) {
            return true;
        }

        if ((getType() == Material.WOOD_BUTTON || getType() == Material.STONE_BUTTON)
                && ((Button) getState().getData()).isPowered()) {
            return true;
        }

        // Now checking for power attached, only solid blocks transmit this..
        if (!getType().isSolid()) {
            return false;
        }

        for (BlockFace face : ADJACENT) {
            GlowBlock target = getRelative(face);
            switch (target.getType()) {
                case LEVER:
                    Lever lever = (Lever) target.getState().getData();
                    if (lever.isPowered() && lever.getAttachedFace() == target.getFace(this)) {
                        return true;
                    }
                    break;
                case STONE_BUTTON:
                case WOOD_BUTTON:
                    Button button = (Button) target.getState().getData();
                    if (button.isPowered() && button.getAttachedFace() == target.getFace(this)) {
                        return true;
                    }
                    break;
                case DIODE_BLOCK_ON:
                    if (((Diode) target.getState().getData()).getFacing() == target.getFace(this)) {
                        return true;
                    }
                    break;
                case REDSTONE_TORCH_ON:
                    if (face == BlockFace.DOWN) {
                        return true;
                    }
                    break;
                case REDSTONE_WIRE:
                    if (target.getData() > 0
                            && BlockRedstone.calculateConnections(target)
                            .contains(target.getFace(this))) {
                        return true;
                    }
                    break;
                default:
                    // do nothing
            }
        }

        return false;
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        // Is a nearby block directly powered?
        for (BlockFace face : ADJACENT) {
            GlowBlock block = getRelative(face);
            if (block.isBlockPowered()) {
                return true;
            }

            switch (block.getType()) {
                case REDSTONE_TORCH_ON:
                    if (face != BlockRedstoneTorch.getAttachedBlockFace(block).getOppositeFace()) {
                        return true;
                    }
                    break;
                case REDSTONE_WIRE:
                    if (block.getData() > 0 && BlockRedstone.calculateConnections(block)
                            .contains(block.getFace(this))) {
                        return true;
                    }
                    break;
                default:
                    // do nothing
            }
        }
        return false;
    }

    @Override
    public boolean isBlockFacePowered(BlockFace face) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getBlockPower(BlockFace face) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getBlockPower() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "GlowBlock{chunk=" + getChunk() + ",x=" + x + ",y=" + y + ",z=" + z + ",type="
                + getType() + ",data=" + getData() + "}";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Drops and breaking

    /**
     * Break the block naturally, randomly dropping only some of the drops.
     *
     * @param yield The approximate portion of the drops to actually drop.
     * @return true if the block was destroyed
     */
    public boolean breakNaturally(float yield) {
        return breakNaturally(yield, ItemTable.instance().getBlock(getType()).getMinedDrops(this));
    }

    /**
     * Breaks this block and drops items.
     *
     * @param yield the probability to drop each item
     * @param drops the items to potentially drop
     * @return true if broken; false if this block is already air
     */
    public boolean breakNaturally(float yield, Collection<ItemStack> drops) {
        if (getType() == Material.AIR) {
            return false;
        }

        Location location = getLocation();
        drops.stream().filter(stack -> ThreadLocalRandom.current().nextFloat() < yield)
                .forEach(stack -> getWorld().dropItemNaturally(location, stack));

        setType(Material.AIR);
        return true;
    }

    @Override
    public boolean breakNaturally() {
        return breakNaturally(1.0f);
    }

    @Override
    public boolean breakNaturally(ItemStack tool) {
        Collection<ItemStack> drops = getDrops(tool);
        if (!drops.isEmpty()) {
            return breakNaturally(1.0f, drops);
        } else {
            return setTypeId(Material.AIR.getId());
        }
    }

    @Override
    public Collection<ItemStack> getDrops() {
        return ItemTable.instance().getBlock(getType()).getMinedDrops(this);
    }

    @Override
    public Collection<ItemStack> getDrops(ItemStack tool) {
        return ItemTable.instance().getBlock(getType()).getDrops(this, tool);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Metadata
    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        metadata.setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return metadata.getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return metadata.hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        metadata.removeMetadata(this, metadataKey, owningPlugin);
    }

    /////////////////////////////////////////////////////////////////////////////
    // Physics

    /**
     * Notify this block and its surrounding blocks that this block has changed type and data.
     *
     * @param oldType the old block type
     * @param newTypeId the new block type
     * @param oldData the old data
     * @param newData the new data
     */
    public void applyPhysics(Material oldType, int newTypeId, byte oldData, byte newData) {
        if (!world.isInitialized()) {
            // World is still loading.
            return;
        }
        // notify the surrounding blocks that this block has changed
        ItemTable itemTable = ItemTable.instance();
        Material newType = Material.getMaterial(newTypeId);

        for (int y = -1; y <= 1; y++) {
            for (BlockFace face : LAYER) {
                if (y == 0 && face == BlockFace.SELF) {
                    continue;
                }

                GlowBlock notify = getRelative(face.getModX(), face.getModY() + y, face.getModZ());

                BlockFace blockFace;
                if (y == 0) {
                    blockFace = face.getOppositeFace();
                } else if (y == -1 && face == BlockFace.SELF) {
                    blockFace = BlockFace.UP;
                } else if (y == 1 && face == BlockFace.SELF) {
                    blockFace = BlockFace.DOWN;
                } else {
                    blockFace = null;
                }

                BlockType notifyType = itemTable.getBlock(notify.getType());
                if (notifyType != null) {
                    notifyType
                            .onNearBlockChanged(notify, blockFace, this, oldType, oldData,
                                    newType, newData);
                }
            }
        }

        BlockType type = itemTable.getBlock(oldType);
        if (type != null) {
            type.onBlockChanged(this, oldType, oldData, newType, newData);
        }
    }

    /**
     * Generates the correct bounding box for a fence block.
     *
     * @param location The location of the block
     * @return The correct fence boundingbox
     */
    private List<BoundingBox> getFenceGateBoundingBox(Location location) {

        double invertedFenceWidth = 1.0 / 4.0;
        BoundingBox box = BoundingBox.fromCenterAndSize(location.toVector(), invertedFenceWidth, 1.5);

        Gate gate = (Gate) this.getState().getData();
        BlockFace gateFace = gate.getFacing();


        if (gate.isOpen()) {
            return Collections.emptyList();
        }

        if (gateFace == BlockFace.NORTH || gateFace == BlockFace.SOUTH) {
            box.minCorner.setZ(box.minCorner.getZ() - invertedFenceWidth);
            box.maxCorner.setZ(box.maxCorner.getZ() + invertedFenceWidth);
            return Arrays.asList(box);
        }

        if (gateFace == BlockFace.WEST || gateFace == BlockFace.EAST) {
            box.minCorner.setX(box.minCorner.getX() - invertedFenceWidth);
            box.maxCorner.setX(box.maxCorner.getX() + invertedFenceWidth);
            return Arrays.asList(box);
        }

        return Arrays.asList(box);
    }

    /**
     * Generates the correct bounding box for a fence block.
     *
     * @param location The location of the block
     * @return The correct fence boundingbox
     */
    private List<BoundingBox> getFenceBoundingBox(Location location) {

        double invertedFenceWidth = 1.0 / 4.0;
        BoundingBox boxX = BoundingBox.fromCenterAndSize(location.toVector(), invertedFenceWidth, 1.5);
        BoundingBox boxZ = BoundingBox.fromCenterAndSize(location.toVector(), invertedFenceWidth, 1.5);

        if (canConnectFence(BlockFace.NORTH)) {
            boxX.minCorner.setZ(boxX.minCorner.getZ() - invertedFenceWidth);
        }

        if (canConnectFence(BlockFace.SOUTH)) {
            boxX.maxCorner.setZ(boxX.maxCorner.getZ() + invertedFenceWidth);
        }

        if (canConnectFence(BlockFace.WEST)) {
            boxZ.minCorner.setX(boxZ.minCorner.getX() - invertedFenceWidth);
        }

        if (canConnectFence(BlockFace.EAST)) {
            boxZ.maxCorner.setX(boxZ.maxCorner.getX() + invertedFenceWidth);
        }

        return Arrays.asList(boxX, boxZ);
    }

    /**
     * Generates the correct bounding box for a fence block.
     *
     * @param location The location of the block
     * @return The correct fence boundingbox
     */
    private List<BoundingBox> getPaneBoundingBox(Location location) {

        double paneWidth = 1.0 / 8.0;

        BoundingBox boxX = BoundingBox.fromCenterAndSize(location.toVector(), paneWidth, 1.0);
        BoundingBox boxZ = BoundingBox.fromCenterAndSize(location.toVector(), paneWidth, 1.0);

        if (canConnectPane(BlockFace.NORTH)) {
            boxX.minCorner.setZ(boxX.minCorner.getZ() - paneWidth);
        }

        if (canConnectPane(BlockFace.SOUTH)) {
            boxX.maxCorner.setZ(boxX.maxCorner.getZ() + paneWidth);
        }

        if (canConnectPane(BlockFace.WEST)) {
            boxZ.minCorner.setX(boxZ.minCorner.getX() - paneWidth);
        }

        if (canConnectPane(BlockFace.EAST)) {
            boxZ.maxCorner.setX(boxZ.maxCorner.getX() + paneWidth);
        }

        return Arrays.asList(boxX, boxZ);
    }

    /**
     * Generates the correct bounding box for a fence block.
     *
     * @param location The location of the block
     * @return The correct fence boundingbox
     */
    private List<BoundingBox> getWallBoundingBox(Location location) {

        double invertedWallWidth = 6.0 / 16.0;
        double invertedWallPostWidth = 1.0 / 4.0;

        BoundingBox boxX = BoundingBox.fromCenterAndSize(location.toVector(), invertedWallWidth, 1.5);
        BoundingBox boxZ = BoundingBox.fromCenterAndSize(location.toVector(), invertedWallWidth, 1.5);
        BoundingBox middle = BoundingBox.fromCenterAndSize(location.toVector(), invertedWallPostWidth, 1.5);

        ArrayList boxes = new ArrayList();
        boxes.addAll(Arrays.asList(boxX, boxZ));

        int connectedX = 0;
        int connectedZ = 0;

        if (canConnectWall(BlockFace.NORTH)) {
            connectedZ++;
            boxX.minCorner.setZ(boxX.minCorner.getZ() - invertedWallWidth);
        }

        if (canConnectWall(BlockFace.SOUTH)) {
            connectedZ++;
            boxX.maxCorner.setZ(boxX.maxCorner.getZ() + invertedWallWidth);
        }

        if (canConnectWall(BlockFace.WEST)) {
            connectedX++;
            boxZ.minCorner.setX(boxZ.minCorner.getX() - invertedWallWidth);
        }

        if (canConnectWall(BlockFace.EAST)) {
            connectedX++;
            boxZ.maxCorner.setX(boxZ.maxCorner.getX() + invertedWallWidth);
        }

        if (connectedX > 0 && connectedZ > 0) {
            boxes.add(middle);
        } else if (connectedX == 1 || connectedZ == 1) {
            boxes.add(middle);
        }

        return boxes;
    }

    public boolean isViableFenceGate(BlockFace face) {
        GlowBlock block = getRelative(face);
        switch (block.getType()) {
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
                BlockFace gateFace = ((Gate) block.getState().getData()).getFacing();
                BlockFace oppositeGateFace = face.getOppositeFace();
                return gateFace != face && oppositeGateFace != gateFace;
            default:
                return false;
        }
    }

    /**
     * Returns a boolean specifying whether or not the block is of type fence.
     *
     * @param face The face of the block that has to be checked
     * @return true if the block is a fence, false otherwise
     */
    public boolean canConnectFence(BlockFace face) {
        GlowBlock block = getRelative(face);
        switch (block.getType()) {
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
                return isViableFenceGate(face);
            case FENCE:
            case NETHER_FENCE:
            case ACACIA_FENCE:
            case BIRCH_FENCE:
            case DARK_OAK_FENCE:
            case IRON_FENCE:
            case JUNGLE_FENCE:
            case SPRUCE_FENCE:
                return true;
            default:
                return block.getType().isOccluding();
        }
    }

    /**
     * Returns a boolean specifying whether or not the block is of type Wall.
     *
     * @param face The face of the block that has to be checked
     * @return true if the block is a where a wall can connect to, false otherwise
     */
    public boolean canConnectWall(BlockFace face) {
        GlowBlock block = getRelative(face);
        switch (block.getType()) {
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
                return isViableFenceGate(face);
            case COBBLE_WALL:
                return true;
            default:
                return block.getType().isOccluding();
        }
    }

    /**
     * Returns a boolean specifying whether or not the block is of type fence.
     *
     * @param face The face of the block that has to be checked
     * @return true if the block is a fence, false otherwise
     */
    public boolean canConnectPane(BlockFace face) {
        GlowBlock block = getRelative(face);
        switch (block.getType()) {
            case THIN_GLASS:
            case STAINED_GLASS_PANE:
            case IRON_FENCE:
                return true;
            default:
                return block.getType().isOccluding();
        }
    }

    /**
     * Builds the skull bounding box which is dependent on position and facing.
     *
     * @param loc  The location of the skull
     * @param face The direction the skull is facing
     * @return The bounding box for the skull
     */
    public List<BoundingBox> getSkullBoundingBox(Location loc, BlockFace face) {

        if (face == BlockFace.UP) {
            return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 0.5, 0.5));
        }

        Location skullLoc = loc.clone().add(0, 0.25, 0);

        if (face == BlockFace.SOUTH) {
            skullLoc.add(0, 0, -0.25);
        } else if (face == BlockFace.NORTH) {
            skullLoc.add(0, 0, 0.25);
        } else if (face == BlockFace.WEST) {
            skullLoc.add(0.25, 0, 0);
        } else if (face == BlockFace.EAST) {
            skullLoc.add(-0.25, 0, 0);
        }

        return Arrays.asList(BoundingBox.fromCenterAndSize(skullLoc.toVector(), 0.5, 0.5));
    }

    /**
     * Returns the bounding box for the stair by checking the stair data and its surroundings.
     *
     * @param loc The location of the stair
     * @param stairs The stair data
     * @return The bounding box of the stair
     */
    public List<BoundingBox> getStairsBoundingBox(Location loc, Stairs stairs) {
        BoundingBox base;
        BoundingBox head = null;

        if (stairs.isInverted()) {
            base = BoundingBox.fromCenterAndSize(loc.add(0, 0.5, 0).toVector(), 1.0, 0.5);
        } else {
            base = BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 0.5);
        }

        BlockFace face = stairs.getAscendingDirection();

        Vector origin = loc.clone().toVector();
        Vector originHalfX = loc.clone().add(0.5, 0.0, 0.0).toVector();
        Vector originHalfZ = loc.clone().add(0.0, 0.0, 0.5).toVector();

        Vector south = loc.clone().add(1.0, 1.0, 0.5).toVector();
        Vector full = loc.clone().add(1.0, 1.0, 1.0).toVector();
        Vector west = loc.clone().add(0.5, 1.0, 1.0).toVector();

        if (face == BlockFace.NORTH) {
            head = BoundingBox.fromCorners(origin, south);
        } else if (face == BlockFace.SOUTH) {
            head = BoundingBox.fromCorners(originHalfZ, full);
        } else if (face == BlockFace.WEST) {
            head = BoundingBox.fromCorners(origin, west);
        } else if (face == BlockFace.EAST) {
            head = BoundingBox.fromCorners(originHalfX, full);
        }

        //TODO: Implement stair corners with the getStairFaceMethod

        if (head != null) {
            return Arrays.asList(head, base);
        } else {
            return Arrays.asList(base);
        }
    }


    /**
     * Returns the cauldron bounding box.
     *
     * @param loc The location of the cauldron
     * @return Returns the bounding boxes necessary to construct the cauldron collision
     */
    public List<BoundingBox> getCauldronBoundingBox(Location loc, double internalHeight) {

        double cauldronWidth = 2.0 / 16.0;
        Vector min = loc.clone().toVector();
        Vector maxZ = loc.clone().add(cauldronWidth, 1.0, 1.0).toVector();
        Vector maxX = loc.clone().add(1.0, 1.0, cauldronWidth).toVector();

        Vector minX = loc.clone().add(1.0 - cauldronWidth, 1.0, 0.0).toVector();
        Vector minZ = loc.clone().add(0.0, 1.0, 1.0 - cauldronWidth).toVector();
        Vector maxXZ = loc.clone().add(1.0, 1.0, 1.0).toVector();

        return Arrays.asList(
                BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, internalHeight),
                BoundingBox.fromCorners(min, maxX),
                BoundingBox.fromCorners(min, maxZ),
                BoundingBox.fromCorners(minX, maxXZ),
                BoundingBox.fromCorners(minZ, maxXZ)
        );

    }

    /**
     * Returns the appropriate slab bounding box.
     *
     * @param loc The location of the block
     * @return The List of boundingboxes for the block
     */
    public List<BoundingBox> getSlabBoundingBox(Location loc) {
        MaterialData data = this.getState().getData();

        boolean inverted;

        if (data instanceof Step) {
            inverted = ((Step) data).isInverted();
        } else {
            inverted = ((WoodenStep) data).isInverted();
        }

        if (inverted) {
            return Arrays.asList(BoundingBox.fromCenterAndSize(loc.add(0.0, 0.5, 0.0).toVector(), 1.0, 0.5));
        } else {
            return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 0.5));
        }
    }

    /**
     * Returns the bounding box corresponding to this glow block.
     *
     * @return The bounding box
     */
    public List<BoundingBox> getBoundingBoxes() {
        Location loc = this.getLocation().clone();
        switch (this.getType()) {
            case STEP:
            case WOOD_STEP:
            case PURPUR_SLAB:
            case STONE_SLAB2:
                return getSlabBoundingBox(loc);
            case FENCE:
            case NETHER_FENCE:
            case ACACIA_FENCE:
            case BIRCH_FENCE:
            case DARK_OAK_FENCE:
            case JUNGLE_FENCE:
            case SPRUCE_FENCE:
                return getFenceBoundingBox(loc);
            case COBBLE_WALL:
                return getWallBoundingBox(loc);
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
                return getFenceGateBoundingBox(loc);
            case SNOW:
                double snowHeight = this.getState().getData().getData() * 1.0 / 7.0;
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, snowHeight));
            case ENCHANTMENT_TABLE:
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 3.0 / 4.0));
            case CHEST:
            case ENDER_CHEST:
            case TRAPPED_CHEST:
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 15.0 / 16.0, 7.0 / 8.0));
            case CACTUS:
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 15.0 / 16.0, 1.0));
            case BED_BLOCK:
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 9.0 / 16.0));
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 3.0 / 8.0));
            case FLOWER_POT:
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 3.0 / 8.0, 3.0 / 8.0));
            case SOUL_SAND:
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 7.0 / 8.0));
            case ENDER_PORTAL_FRAME:
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 13.0 / 16.0));
            case WATER_LILY:
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 1.0 / 64.0));
            case CAKE_BLOCK:
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 7.0 / 8.0, 7.0 / 16.0));
            case CAULDRON:
                return getCauldronBoundingBox(loc, 5.0 / 16.0);
            case HOPPER:
                return getCauldronBoundingBox(loc, 9.0 / 16.0);
            case BREWING_STAND:
                return Arrays.asList(
                        BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 1.0 / 8.0),
                        BoundingBox.fromCenterAndSize(loc.toVector(), 2.0 / 16.0, 7.0 / 8.0)
                );
            case THIN_GLASS:
            case STAINED_GLASS_PANE:
            case IRON_FENCE:
                return getPaneBoundingBox(loc);
            case TRAP_DOOR:
            case IRON_TRAPDOOR:
            case REDSTONE_COMPARATOR:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 1.0 / 8.0));
            case CARPET:
                return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 1.0 / 16.0));
            case SKULL:
                return getSkullBoundingBox(loc, ((Skull) this.getState().getData()).getFacing());
            case ACACIA_STAIRS:
            case BIRCH_WOOD_STAIRS:
            case DARK_OAK_STAIRS:
            case JUNGLE_WOOD_STAIRS:
            case SPRUCE_WOOD_STAIRS:
            case WOOD_STAIRS:
            case COBBLESTONE_STAIRS:
            case SANDSTONE_STAIRS:
            case BRICK_STAIRS:
            case NETHER_BRICK_STAIRS:
            case SMOOTH_STAIRS:
            case QUARTZ_STAIRS:
            case PURPUR_STAIRS:
            case RED_SANDSTONE_STAIRS:
                return getStairsBoundingBox(loc, ((Stairs) this.getState().getData()));
            default:
                if (this.getType().isSolid()) {
                    return Arrays.asList(BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 1.0));
                } else {
                    return Collections.emptyList();
                }
        }
    }

    /**
     * Increments the count of recent state changes. Used to implement redstone-torch burnout.
     *
     * @param timeout the number of game ticks before this state change is no longer considered recent
     */
    public void count(int timeout) {
        GlowBlock target = this;
        List<Long> gameTicks = new ArrayList<>();
        for (GlowBlock block : counterMap.keySet()) {
            if (block.getLocation().equals(getLocation())) {
                gameTicks = counterMap.get(block);
                target = block;
                break;
            }
        }

        long time = getWorld().getFullTime();
        gameTicks.add(time + timeout);

        counterMap.put(target, gameTicks);
    }

    /**
     * Returns the number of recent state changes, as defined by {@link #count(int)}. Used to
     * implement redstone-torch burnout.
     *
     * @return the number of recent state changes
     */
    public int getCounter() {
        GlowBlock target = this;
        List<Long> gameTicks = new ArrayList<>();
        for (GlowBlock block : counterMap.keySet()) {
            if (block.getLocation().equals(getLocation())) {
                gameTicks = counterMap.get(block);
                target = block;
                break;
            }
        }

        long time = getWorld().getFullTime();

        gameTicks.removeIf(rate -> rate < time);

        counterMap.put(target, gameTicks);
        return gameTicks.size();
    }

    @Override
    public int hashCode() {
        return y << 24 ^ x ^ z ^ getWorld().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GlowBlock other = (GlowBlock) obj;
        return x == other.x && y == other.y && z == other.z && getWorld().equals(other.getWorld());
    }

    /**
     * The metadata store class for blocks.
     */
    private static final class BlockMetadataStore extends MetadataStoreBase<Block> implements
            MetadataStore<Block> {

        @Override
        protected String disambiguate(Block subject, String metadataKey) {
            return subject.getWorld() + "," + subject.getX() + "," + subject.getY() + "," + subject
                    .getZ() + ":" + metadataKey;
        }
    }
}

