package science.atlarge.opencraft.opencraft.entity.physics;

import static science.atlarge.opencraft.opencraft.entity.physics.BoundingBox.Dimensions;
import static org.bukkit.Material.ACACIA_DOOR;
import static org.bukkit.Material.ACACIA_FENCE;
import static org.bukkit.Material.ACACIA_FENCE_GATE;
import static org.bukkit.Material.ACACIA_STAIRS;
import static org.bukkit.Material.BED_BLOCK;
import static org.bukkit.Material.BIRCH_DOOR;
import static org.bukkit.Material.BIRCH_FENCE;
import static org.bukkit.Material.BIRCH_FENCE_GATE;
import static org.bukkit.Material.BIRCH_WOOD_STAIRS;
import static org.bukkit.Material.BRICK_STAIRS;
import static org.bukkit.Material.CACTUS;
import static org.bukkit.Material.CAKE_BLOCK;
import static org.bukkit.Material.CARPET;
import static org.bukkit.Material.CHEST;
import static org.bukkit.Material.CHORUS_PLANT;
import static org.bukkit.Material.COBBLESTONE_STAIRS;
import static org.bukkit.Material.COBBLE_WALL;
import static org.bukkit.Material.DARK_OAK_DOOR;
import static org.bukkit.Material.DARK_OAK_FENCE;
import static org.bukkit.Material.DARK_OAK_FENCE_GATE;
import static org.bukkit.Material.DARK_OAK_STAIRS;
import static org.bukkit.Material.DAYLIGHT_DETECTOR;
import static org.bukkit.Material.DAYLIGHT_DETECTOR_INVERTED;
import static org.bukkit.Material.ENCHANTMENT_TABLE;
import static org.bukkit.Material.ENDER_CHEST;
import static org.bukkit.Material.ENDER_PORTAL_FRAME;
import static org.bukkit.Material.END_ROD;
import static org.bukkit.Material.FENCE;
import static org.bukkit.Material.FENCE_GATE;
import static org.bukkit.Material.FLOWER_POT;
import static org.bukkit.Material.GOLD_PLATE;
import static org.bukkit.Material.IRON_DOOR;
import static org.bukkit.Material.IRON_DOOR_BLOCK;
import static org.bukkit.Material.IRON_FENCE;
import static org.bukkit.Material.IRON_PLATE;
import static org.bukkit.Material.IRON_TRAPDOOR;
import static org.bukkit.Material.JUNGLE_DOOR;
import static org.bukkit.Material.JUNGLE_FENCE;
import static org.bukkit.Material.JUNGLE_FENCE_GATE;
import static org.bukkit.Material.JUNGLE_WOOD_STAIRS;
import static org.bukkit.Material.NETHER_BRICK_STAIRS;
import static org.bukkit.Material.NETHER_FENCE;
import static org.bukkit.Material.PURPUR_SLAB;
import static org.bukkit.Material.PURPUR_STAIRS;
import static org.bukkit.Material.QUARTZ_STAIRS;
import static org.bukkit.Material.REDSTONE_COMPARATOR;
import static org.bukkit.Material.REDSTONE_COMPARATOR_OFF;
import static org.bukkit.Material.REDSTONE_COMPARATOR_ON;
import static org.bukkit.Material.RED_SANDSTONE_STAIRS;
import static org.bukkit.Material.SANDSTONE_STAIRS;
import static org.bukkit.Material.SMOOTH_STAIRS;
import static org.bukkit.Material.SOIL;
import static org.bukkit.Material.SOUL_SAND;
import static org.bukkit.Material.SPRUCE_DOOR;
import static org.bukkit.Material.SPRUCE_FENCE;
import static org.bukkit.Material.SPRUCE_FENCE_GATE;
import static org.bukkit.Material.SPRUCE_WOOD_STAIRS;
import static org.bukkit.Material.STAINED_GLASS_PANE;
import static org.bukkit.Material.STEP;
import static org.bukkit.Material.STONE_PLATE;
import static org.bukkit.Material.STONE_SLAB2;
import static org.bukkit.Material.THIN_GLASS;
import static org.bukkit.Material.TRAPPED_CHEST;
import static org.bukkit.Material.TRAP_DOOR;
import static org.bukkit.Material.WATER_LILY;
import static org.bukkit.Material.WOODEN_DOOR;
import static org.bukkit.Material.WOOD_DOOR;
import static org.bukkit.Material.WOOD_PLATE;
import static org.bukkit.Material.WOOD_STAIRS;
import static org.bukkit.Material.WOOD_STEP;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Door;
import org.bukkit.material.Gate;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Skull;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import org.bukkit.material.WoodenStep;
import org.bukkit.util.Vector;

/**
 * This class generates all the boundingboxes for all blocktypes.
 */
public class BlockBoundingBoxes {

    private static final Dimensions FULL_BLOCK = Dimensions.create(1.0, 1.0);
    private static final Dimensions HALF_BLOCK = Dimensions.create(1.0, 0.5);
    private static final Set<Material> STAIRS;
    private static final Set<Material> FENCES;
    private static final Set<Material> DOORS;
    private static final Set<Material> PANES;
    private static final Set<Material> GATES;
    private static final Set<Material> SLABS;
    private static final Map<Material, Dimensions> BOUNDINGBOX_SIZES;

    static {

        STAIRS = ImmutableSet.of(
                ACACIA_STAIRS,
                BIRCH_WOOD_STAIRS,
                DARK_OAK_STAIRS,
                JUNGLE_WOOD_STAIRS,
                SPRUCE_WOOD_STAIRS,
                WOOD_STAIRS,
                COBBLESTONE_STAIRS,
                SANDSTONE_STAIRS,
                BRICK_STAIRS,
                NETHER_BRICK_STAIRS,
                SMOOTH_STAIRS,
                QUARTZ_STAIRS,
                PURPUR_STAIRS,
                RED_SANDSTONE_STAIRS
        );

        FENCES = ImmutableSet.of(
                FENCE,
                NETHER_FENCE,
                ACACIA_FENCE,
                BIRCH_FENCE,
                DARK_OAK_FENCE,
                IRON_FENCE,
                JUNGLE_FENCE,
                SPRUCE_FENCE
        );

        DOORS = ImmutableSet.of(
                DARK_OAK_DOOR,
                ACACIA_DOOR,
                BIRCH_DOOR,
                IRON_DOOR,
                JUNGLE_DOOR,
                SPRUCE_DOOR,
                WOODEN_DOOR,
                WOOD_DOOR,
                IRON_DOOR_BLOCK
        );

        GATES = ImmutableSet.of(
                FENCE_GATE,
                ACACIA_FENCE_GATE,
                BIRCH_FENCE_GATE,
                DARK_OAK_FENCE_GATE,
                JUNGLE_FENCE_GATE,
                SPRUCE_FENCE_GATE
        );

        PANES = ImmutableSet.of(
                THIN_GLASS,
                STAINED_GLASS_PANE,
                IRON_FENCE
        );

        SLABS = ImmutableSet.of(
                STEP,
                WOOD_STEP,
                PURPUR_SLAB,
                STONE_SLAB2
        );

        ImmutableMap.Builder<Material, Dimensions> builder = ImmutableMap.builder();
        BOUNDINGBOX_SIZES = builder.put(ENCHANTMENT_TABLE, Dimensions.create(1.0, 3.0 / 4.0))
                                   .put(CHEST, Dimensions.create(14.0 / 16.0, 7.0 / 8.0))
                                   .put(ENDER_CHEST, Dimensions.create(14.0 / 16.0, 7.0 / 8.0))
                                   .put(TRAPPED_CHEST, Dimensions.create(14.0 / 16.0, 7.0 / 8.0))
                                   .put(CACTUS, Dimensions.create(14.0 / 16.0, 1.0))
                                   .put(BED_BLOCK,Dimensions.create(1.0, 9.0 / 16.0))
                                   .put(DAYLIGHT_DETECTOR, Dimensions.create(1.0, 3.0 / 8.0))
                                   .put(DAYLIGHT_DETECTOR_INVERTED, Dimensions.create(1.0, 3.0 / 8.0))
                                   .put(FLOWER_POT, Dimensions.create(3.0 / 8.0, 3.0 / 8.0))
                                   .put(SOUL_SAND, Dimensions.create(1.0, 7.0 / 8.0))
                                   .put(ENDER_PORTAL_FRAME, Dimensions.create(1.0, 13.0 / 16.0))
                                   .put(WATER_LILY, Dimensions.create(1.0, 1.0 / 64.0))
                                   .put(CAKE_BLOCK, Dimensions.create(7.0 / 8.0, 7.0 / 16.0))
                                   .put(TRAP_DOOR, Dimensions.create(1.0, 1.0 / 8.0))
                                   .put(IRON_TRAPDOOR, Dimensions.create(1.0, 1.0 / 8.0))
                                   .put(REDSTONE_COMPARATOR, Dimensions.create(1.0, 1.0 / 8.0))
                                   .put(REDSTONE_COMPARATOR_OFF, Dimensions.create(1.0, 1.0 / 8.0))
                                   .put(REDSTONE_COMPARATOR_ON, Dimensions.create(1.0, 1.0 / 8.0))
                                   .put(CARPET, Dimensions.create(1.0, 1.0 / 16.0))
                                   .put(GOLD_PLATE, Dimensions.create(14.0 / 16.0, 0.01))
                                   .put(STONE_PLATE, Dimensions.create(14.0 / 16.0, 0.01))
                                   .put(IRON_PLATE, Dimensions.create(14.0 / 16.0, 0.01))
                                   .put(WOOD_PLATE, Dimensions.create(14.0 / 16.0, 0.01))
                                   .put(CHORUS_PLANT, Dimensions.create(10.0 / 16.0, 3.0 / 4.0))
                                   .put(SOIL, Dimensions.create(1.0, 15.0 / 16.0))
                                   .put(END_ROD, Dimensions.create(4.0 / 16.0, 1.0))
                                   .build();

    }

    /**
     * Generates the correct bounding box for a fence block.
     *
     * @param location The location of the block
     * @param block The location of the block
     * @return The correct fence boundingbox
     */
    private static List<BoundingBox> getFenceGateBoundingBoxes(Location location, GlowBlock block) {

        double invertedFenceWidth = 1.0 / 4.0;
        BoundingBox box = BoundingBox.fromCenterAndSize(location.toVector(), invertedFenceWidth, 1.5);

        Gate gate = (Gate) block.getState().getData();
        BlockFace gateFace = gate.getFacing();

        if (gate.isOpen()) {
            return Collections.emptyList();
        }

        if (gateFace == BlockFace.NORTH || gateFace == BlockFace.SOUTH) {
            box.minCorner.setZ(box.minCorner.getZ() - invertedFenceWidth);
            box.maxCorner.setZ(box.maxCorner.getZ() + invertedFenceWidth);
            return Collections.singletonList(box);
        }

        if (gateFace == BlockFace.WEST || gateFace == BlockFace.EAST) {
            box.minCorner.setX(box.minCorner.getX() - invertedFenceWidth);
            box.maxCorner.setX(box.maxCorner.getX() + invertedFenceWidth);
            return Collections.singletonList(box);
        }

        return Collections.singletonList(box);
    }

    /**
     * Generates the correct bounding box for a fence block.
     *
     * @param location The location of the block
     * @param block The location of the block
     * @return The correct fence boundingbox
     */
    private static List<BoundingBox> getDoorBoundingBoxes(Location location, GlowBlock block) {

        double doorWidth = 3.0 / 16.0;
        double invDoorWidth = 1.0 - doorWidth;
        Vector origin = location.toVector();
        Vector maxVector = location.add(1.0, 1.0, 1.0).toVector();

        BoundingBox north = BoundingBox.fromCorners(origin, location.add(1.0, 1.0, doorWidth).toVector());
        BoundingBox east = BoundingBox.fromCorners(location.add(invDoorWidth, 0.0, 0.0).toVector(), maxVector);
        BoundingBox south = BoundingBox.fromCorners(location.add(0.0, 0.0, invDoorWidth).toVector(), maxVector);
        BoundingBox west = BoundingBox.fromCorners(origin, location.add(doorWidth, 1.0, 1.0).toVector());
        Door door = (Door) block.getState().getData();
        BlockFace doorFace = door.getFacing();

        //TODO fix door hinge

        if (doorFace == BlockFace.NORTH && !door.isOpen() || doorFace == BlockFace.WEST && door.isOpen()) {
            return Collections.singletonList(north);
        }

        if (doorFace == BlockFace.EAST && !door.isOpen() || doorFace == BlockFace.SOUTH && door.isOpen()) {
            return Collections.singletonList(east);
        }

        if (doorFace == BlockFace.SOUTH && !door.isOpen() || doorFace == BlockFace.WEST && door.isOpen()) {
            return Collections.singletonList(south);
        }

        if (doorFace == BlockFace.WEST && !door.isOpen() || doorFace == BlockFace.SOUTH && door.isOpen()) {
            return Collections.singletonList(west);
        }

        return Collections.singletonList(north);
    }

    /**
     * Generates the correct bounding box for a fence block.
     *
     * @param location The location of the block
     * @param block The location of the block
     * @return The correct fence boundingbox
     */
    private static List<BoundingBox> getFenceBoundingBoxes(Location location, GlowBlock block) {

        double invertedFenceWidth = 1.0 / 4.0;
        BoundingBox boxX = BoundingBox.fromCenterAndSize(location.toVector(), invertedFenceWidth, 1.5);
        BoundingBox boxZ = BoundingBox.fromCenterAndSize(location.toVector(), invertedFenceWidth, 1.5);

        if (canConnectFence(BlockFace.NORTH, block)) {
            boxX.minCorner.setZ(boxX.minCorner.getZ() - invertedFenceWidth);
        }

        if (canConnectFence(BlockFace.SOUTH, block)) {
            boxX.maxCorner.setZ(boxX.maxCorner.getZ() + invertedFenceWidth);
        }

        if (canConnectFence(BlockFace.WEST, block)) {
            boxZ.minCorner.setX(boxZ.minCorner.getX() - invertedFenceWidth);
        }

        if (canConnectFence(BlockFace.EAST, block)) {
            boxZ.maxCorner.setX(boxZ.maxCorner.getX() + invertedFenceWidth);
        }

        return Arrays.asList(boxX, boxZ);
    }

    /**
     * Generates the correct bounding box for a fence block.
     *
     * @param location The location of the block
     * @param block The location of the block
     * @return The correct fence boundingbox
     */
    private static List<BoundingBox> getPaneBoundingBoxes(Location location, GlowBlock block) {

        double paneWidth = 1.0 / 8.0;

        BoundingBox boxX = BoundingBox.fromCenterAndSize(location.toVector(), paneWidth, 1.0);
        BoundingBox boxZ = BoundingBox.fromCenterAndSize(location.toVector(), paneWidth, 1.0);

        if (canConnectPane(BlockFace.NORTH, block)) {
            boxX.minCorner.setZ(boxX.minCorner.getZ() - paneWidth);
        }

        if (canConnectPane(BlockFace.SOUTH, block)) {
            boxX.maxCorner.setZ(boxX.maxCorner.getZ() + paneWidth);
        }

        if (canConnectPane(BlockFace.WEST, block)) {
            boxZ.minCorner.setX(boxZ.minCorner.getX() - paneWidth);
        }

        if (canConnectPane(BlockFace.EAST, block)) {
            boxZ.maxCorner.setX(boxZ.maxCorner.getX() + paneWidth);
        }

        return Arrays.asList(boxX, boxZ);
    }

    /**
     * Generates the correct bounding box for a fence block.
     *
     * @param location The location of the block
     * @param block The location of the block
     * @return The correct fence boundingbox
     */
    private static List<BoundingBox> getWallBoundingBoxes(Location location, GlowBlock block) {

        double invertedWallWidth = 6.0 / 16.0;
        double invertedWallPostWidth = 1.0 / 4.0;

        BoundingBox boxX = BoundingBox.fromCenterAndSize(location.toVector(), invertedWallWidth, 1.5);
        BoundingBox boxZ = BoundingBox.fromCenterAndSize(location.toVector(), invertedWallWidth, 1.5);
        BoundingBox middle = BoundingBox.fromCenterAndSize(location.toVector(), invertedWallPostWidth, 1.5);

        ArrayList<BoundingBox> boxes = new ArrayList<>(Arrays.asList(boxX, boxZ));

        int connectedX = 0;
        int connectedZ = 0;

        if (canConnectWall(BlockFace.NORTH, block)) {
            connectedZ++;
            boxX.minCorner.setZ(boxX.minCorner.getZ() - invertedWallWidth);
        }

        if (canConnectWall(BlockFace.SOUTH, block)) {
            connectedZ++;
            boxX.maxCorner.setZ(boxX.maxCorner.getZ() + invertedWallWidth);
        }

        if (canConnectWall(BlockFace.WEST, block)) {
            connectedX++;
            boxZ.minCorner.setX(boxZ.minCorner.getX() - invertedWallWidth);
        }

        if (canConnectWall(BlockFace.EAST, block)) {
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

    /**
     * Returns a boolean indicating the alignment of the fence relative to the block.
     *
     * @param face The face of the block to be checked as gate.
     * @param block The block that is of type fence.
     * @return True if the gate is aligned parallel to the fence, false if not.
     */
    private static boolean isFenceGateAligned(BlockFace face, GlowBlock block) {
        GlowBlock nextBlock = block.getRelative(face);
        if (GATES.contains(nextBlock.getType())) {
            BlockFace gateFace = ((Gate) nextBlock.getState().getData()).getFacing();
            BlockFace oppositeGateFace = face.getOppositeFace();
            return gateFace != face && oppositeGateFace != gateFace;
        }

        return false;
    }

    /**
     * Returns a boolean specifying whether or not the block is of type fence.
     *
     * @param face The face of the block that has to be checked
     * @param block The location of the block
     * @return true if the block is a fence, false otherwise
     */
    private static boolean canConnectFence(BlockFace face, GlowBlock block) {
        GlowBlock nextBlock = block.getRelative(face);
        Material material = nextBlock.getType();

        if (FENCES.contains(material)) {
            return true;
        } else if (GATES.contains(material)) {
            return isFenceGateAligned(face, block);
        }

        return nextBlock.getType().isOccluding();
    }

    /**
     * Returns a boolean specifying whether or not the block is of type Wall.
     *
     * @param face The face of the block that has to be checked
     * @param block The location of the block
     * @return true if the block is a where a wall can connect to, false otherwise
     */
    private static boolean canConnectWall(BlockFace face, GlowBlock block) {
        GlowBlock nextBlock = block.getRelative(face);
        if (nextBlock.getType() == COBBLE_WALL) {
            return true;
        } else if (GATES.contains(nextBlock.getType())) {
            return isFenceGateAligned(face, block);
        } else {
            return nextBlock.getType().isOccluding();
        }
    }

    /**
     * Returns a boolean specifying whether or not the block is of type fence.
     *
     * @param face The face of the block that has to be checked
     * @param block The location of the block
     * @return true if the block is a fence, false otherwise
     */
    private static boolean canConnectPane(BlockFace face, GlowBlock block) {
        GlowBlock nextBlock = block.getRelative(face);
        if (PANES.contains(nextBlock.getType())) {
            return true;
        }
        return nextBlock.getType().isOccluding();
    }

    /**
     * Builds the skull bounding box which is dependent on position and facing.
     *
     * @param location The location of the skull
     * @param skull The Skull data
     * @return The bounding box for the skull
     */
    private static List<BoundingBox> getSkullBoundingBoxes(Location location, Skull skull) {

        BlockFace face = skull.getFacing();
        if (face == BlockFace.SELF) {
            return Collections.singletonList(BoundingBox.fromCenterAndSize(location.toVector(), 0.5, 0.5));
        }

        Location skullLoc = location.clone().add(0, 0.25, 0);

        if (face == BlockFace.SOUTH) {
            skullLoc.add(0, 0, -0.25);
        } else if (face == BlockFace.NORTH) {
            skullLoc.add(0, 0, 0.25);
        } else if (face == BlockFace.WEST) {
            skullLoc.add(0.25, 0, 0);
        } else if (face == BlockFace.EAST) {
            skullLoc.add(-0.25, 0, 0);
        }

        return Collections.singletonList(BoundingBox.fromCenterAndSize(skullLoc.toVector(), 0.5, 0.5));
    }

    /**
     * Returns the bounding box for the stair by checking the stair data and its surroundings.
     *
     * @param location The location of the stair
     * @param stairs The stair data
     * @return The bounding box of the stair
     */
    private static List<BoundingBox> getStairsBoundingBoxes(Location location, Stairs stairs) {
        BoundingBox base;
        BoundingBox head = null;

        if (stairs.isInverted()) {
            base = BoundingBox.fromDimension(location.add(0, 0.5, 0).toVector(), HALF_BLOCK);
        } else {
            base = BoundingBox.fromDimension(location.toVector(), HALF_BLOCK);
        }

        BlockFace face = stairs.getAscendingDirection();

        Vector origin = location.clone().toVector();
        Vector originHalfX = location.clone().add(0.5, 0.0, 0.0).toVector();
        Vector originHalfZ = location.clone().add(0.0, 0.0, 0.5).toVector();

        Vector south = location.clone().add(1.0, 1.0, 0.5).toVector();
        Vector full = location.clone().add(1.0, 1.0, 1.0).toVector();
        Vector west = location.clone().add(0.5, 1.0, 1.0).toVector();

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
            return Collections.singletonList(base);
        }
    }


    /**
     * Returns the cauldron bounding box.
     *
     * @param location The location of the cauldron
     * @param internalHeight The height of the inside of the cauldron
     * @return Returns the bounding boxes necessary to construct the cauldron collision
     */
    private static List<BoundingBox> getCauldronBoundingBoxes(Location location, double internalHeight) {

        double cauldronWidth = 2.0 / 16.0;
        Vector min = location.clone().toVector();
        Vector maxZ = location.clone().add(cauldronWidth, 1.0, 1.0).toVector();
        Vector maxX = location.clone().add(1.0, 1.0, cauldronWidth).toVector();

        Vector minX = location.clone().add(1.0 - cauldronWidth, 1.0, 0.0).toVector();
        Vector minZ = location.clone().add(0.0, 1.0, 1.0 - cauldronWidth).toVector();
        Vector maxXZ = location.clone().add(1.0, 1.0, 1.0).toVector();

        Dimensions bottom = Dimensions.create(1.0, internalHeight);

        return Arrays.asList(
                BoundingBox.fromDimension(location.toVector(), bottom),
                BoundingBox.fromCorners(min, maxX),
                BoundingBox.fromCorners(min, maxZ),
                BoundingBox.fromCorners(minX, maxXZ),
                BoundingBox.fromCorners(minZ, maxXZ)
        );

    }

    /**
     * Returns the appropriate slab bounding box.
     *
     * @param location The location of the block
     * @param block The location of the block
     * @return The List of boundingboxes for the block
     */
    private static List<BoundingBox> getSlabBoundingBoxes(Location location, GlowBlock block) {
        MaterialData data = block.getState().getData();

        boolean inverted;

        if (data instanceof Step) {
            inverted = ((Step) data).isInverted();
        } else {
            inverted = ((WoodenStep) data).isInverted();
        }

        if (inverted) {
            Vector higherOrigin = location.add(0.0, 0.5, 0.0).toVector();
            return Collections.singletonList(BoundingBox.fromDimension(higherOrigin, HALF_BLOCK));
        } else {
            return Collections.singletonList(BoundingBox.fromDimension(location.toVector(), HALF_BLOCK));
        }
    }

    /**
     * Returns a boundingboxList with one boundingbox that is centered on the location.
     *
     * @param location The location of the boundingbox
     * @param dimensions The dimensions of the block
     * @return A boundingboxList with the specified boundingbox
     */
    private static List<BoundingBox> getBlockBoundingBoxesWithDimension(Location location, Dimensions dimensions) {
        Vector locationVector = location.toVector();
        BoundingBox box = BoundingBox.fromDimension(locationVector, dimensions);
        return Collections.singletonList(box);
    }

    /**
     * Returns the brewing stand bounding boxes.
     *
     * @param location The location of the boundingbox
     * @return A list with the brewing stand bounding boxes
     */
    private static List<BoundingBox> getBrewingStandBoundingBoxes(Location location) {
        Vector locationVector = location.toVector();
        Dimensions base = Dimensions.create(1.0, 1.0 / 8.0);
        Dimensions pole = Dimensions.create(2.0 / 16.0, 7.0 / 8.0);
        return Arrays.asList(
                BoundingBox.fromDimension(locationVector, base),
                BoundingBox.fromDimension(locationVector, pole)
        );
    }

    /**
     * Returns the boundingbox that corresponds to the correct snowheight.
     * @param location The location of the boundingbox
     * @param block The block of the boundingbox
     * @return A full width boundingbox with a height corresponding to the amount of snow
     */
    private static List<BoundingBox> getSnowBlockBoundingBox(Location location, GlowBlock block) {
        double snowHeight = block.getState().getRawData() * 1.0 / 7.0;
        Dimensions dimensions = Dimensions.create(1.0, snowHeight);
        return getBlockBoundingBoxesWithDimension(location, dimensions);
    }

    /**
     * Returns all the bounding boxes that are not edge cases or require special functions.
     *
     * @param location The location of the bounding box
     * @param block The corresponding block
     * @return The List of boundingboxes that corresponds to the block
     */
    private static List<BoundingBox> getBoundingBoxesRemainingBlocks(Location location, GlowBlock block) {
        switch (block.getType()) {
            case COBBLE_WALL:
                return getWallBoundingBoxes(location, block);
            case SNOW:
                return getSnowBlockBoundingBox(location, block);
            case CAULDRON:
                return getCauldronBoundingBoxes(location, 5.0 / 16.0);
            case HOPPER:
                return getCauldronBoundingBoxes(location, 9.0 / 16.0);
            case BREWING_STAND:
                return getBrewingStandBoundingBoxes(location);
            case SKULL:
                return getSkullBoundingBoxes(location, ((Skull) block.getState().getData()));
            default:
                if (block.getType().isSolid()) {
                    return getBlockBoundingBoxesWithDimension(location, FULL_BLOCK);
                } else {
                    return Collections.emptyList();
                }
        }
    }

    /**
     * Returns the bounding box corresponding to block glow block.
     *
     * @param block The location of the block
     * @return The bounding box
     */
    public static List<BoundingBox> getBoundingBoxes(GlowBlock block) {

        Location loc = block.getLocation().clone();
        Material blockType = block.getType();

        Dimensions dimensions = BOUNDINGBOX_SIZES.get(blockType);
        if (dimensions != null) {
            return getBlockBoundingBoxesWithDimension(loc, dimensions);
        }

        if (FENCES.contains(blockType)) {
            return getFenceBoundingBoxes(loc, block);
        } else if (GATES.contains(blockType)) {
            return getFenceGateBoundingBoxes(loc, block);
        } else if (STAIRS.contains(blockType)) {
            return getStairsBoundingBoxes(loc, ((Stairs) block.getState().getData()));
        } else if (DOORS.contains(blockType)) {
            return getDoorBoundingBoxes(loc, block);
        } else if (PANES.contains(blockType)) {
            return getPaneBoundingBoxes(loc, block);
        } else if (SLABS.contains(blockType)) {
            return getSlabBoundingBoxes(loc, block);
        }

        return getBoundingBoxesRemainingBlocks(loc, block);
    }
}
