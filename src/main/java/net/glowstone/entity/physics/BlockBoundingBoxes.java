package net.glowstone.entity.physics;

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
import static org.bukkit.Material.STONE_PLATE;
import static org.bukkit.Material.TRAPPED_CHEST;
import static org.bukkit.Material.TRAP_DOOR;
import static org.bukkit.Material.WATER_LILY;
import static org.bukkit.Material.WOODEN_DOOR;
import static org.bukkit.Material.WOOD_DOOR;
import static org.bukkit.Material.WOOD_PLATE;
import static org.bukkit.Material.WOOD_STAIRS;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.glowstone.block.GlowBlock;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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

    private static final Set<Material> stairs;
    private static final Set<Material> fences;
    private static final Set<Material> doors;
    private static final Set<Material> fenceGates;
    private static final ImmutableMap<Material, ImmutablePair<Double, Double>> MATERIALS_BOUNDINGBOX_SIZE;

    static {
        stairs = ImmutableSet.of(
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
        fences = ImmutableSet.of(
                FENCE,
                NETHER_FENCE,
                ACACIA_FENCE,
                BIRCH_FENCE,
                DARK_OAK_FENCE,
                IRON_FENCE,
                JUNGLE_FENCE,
                SPRUCE_FENCE
        );
        doors = ImmutableSet.of(
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
        fenceGates = ImmutableSet.of(
                FENCE_GATE,
                ACACIA_FENCE_GATE,
                BIRCH_FENCE_GATE,
                DARK_OAK_FENCE_GATE,
                JUNGLE_FENCE_GATE,
                SPRUCE_FENCE_GATE
        );

        ImmutableMap.Builder<Material, ImmutablePair<Double, Double>> builder = ImmutableMap.builder();
        MATERIALS_BOUNDINGBOX_SIZE = builder.put(ENCHANTMENT_TABLE, ImmutablePair.of(1.0, 3.0 / 4.0))
                                            .put(CHEST, ImmutablePair.of(14.0 / 16.0, 7.0 / 8.0))
                                            .put(ENDER_CHEST, ImmutablePair.of(14.0 / 16.0, 7.0 / 8.0))
                                            .put(TRAPPED_CHEST, ImmutablePair.of(14.0 / 16.0, 7.0 / 8.0))
                                            .put(CACTUS, ImmutablePair.of(14.0 / 16.0, 1.0))
                                            .put(BED_BLOCK, ImmutablePair.of(1.0, 9.0 / 16.0))
                                            .put(DAYLIGHT_DETECTOR, ImmutablePair.of(1.0, 3.0 / 8.0))
                                            .put(DAYLIGHT_DETECTOR_INVERTED, ImmutablePair.of(1.0, 3.0 / 8.0))
                                            .put(FLOWER_POT, ImmutablePair.of(3.0 / 8.0, 3.0 / 8.0))
                                            .put(SOUL_SAND, ImmutablePair.of(1.0, 7.0 / 8.0))
                                            .put(ENDER_PORTAL_FRAME, ImmutablePair.of(1.0, 13.0 / 16.0))
                                            .put(WATER_LILY, ImmutablePair.of(1.0, 1.0 / 64.0))
                                            .put(CAKE_BLOCK, ImmutablePair.of(7.0 / 8.0, 7.0 / 16.0))
                                            .put(TRAP_DOOR, ImmutablePair.of(1.0, 1.0 / 8.0))
                                            .put(IRON_TRAPDOOR, ImmutablePair.of(1.0, 1.0 / 8.0))
                                            .put(REDSTONE_COMPARATOR, ImmutablePair.of(1.0, 1.0 / 8.0))
                                            .put(REDSTONE_COMPARATOR_OFF, ImmutablePair.of(1.0, 1.0 / 8.0))
                                            .put(REDSTONE_COMPARATOR_ON, ImmutablePair.of(1.0, 1.0 / 8.0))
                                            .put(CARPET, ImmutablePair.of(1.0, 1.0 / 16.0))
                                            .put(GOLD_PLATE, ImmutablePair.of(14.0 / 16.0, 0.01))
                                            .put(STONE_PLATE, ImmutablePair.of(14.0 / 16.0, 0.01))
                                            .put(IRON_PLATE, ImmutablePair.of(14.0 / 16.0, 0.01))
                                            .put(WOOD_PLATE, ImmutablePair.of(14.0 / 16.0, 0.01))
                                            .put(CHORUS_PLANT, ImmutablePair.of(10.0 / 16.0, 3.0 / 4.0))
                                            .put(SOIL, ImmutablePair.of(1.0, 15.0 / 16.0))
                                            .put(END_ROD, ImmutablePair.of(4.0 / 16.0, 1.0))
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
        if (fenceGates.contains(nextBlock.getType())) {
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

        if (fences.contains(material)) {
            return true;
        } else if (fenceGates.contains(material)) {
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
        if (fenceGates.contains(nextBlock.getType())) {
            return isFenceGateAligned(face, block);
        } else if (nextBlock.getType() == COBBLE_WALL) {
            return true;
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
        switch (nextBlock.getType()) {
            case THIN_GLASS:
            case STAINED_GLASS_PANE:
            case IRON_FENCE:
                return true;
            default:
                return nextBlock.getType().isOccluding();
        }
    }

    /**
     * Builds the skull bounding box which is dependent on position and facing.
     *
     * @param loc The location of the skull
     * @param skull The Skull data
     * @return The bounding box for the skull
     */
    private static List<BoundingBox> getSkullBoundingBoxes(Location loc, Skull skull) {

        BlockFace face = skull.getFacing();
        if (face == BlockFace.SELF) {
            return Collections.singletonList(BoundingBox.fromCenterAndSize(loc.toVector(), 0.5, 0.5));
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

        return Collections.singletonList(BoundingBox.fromCenterAndSize(skullLoc.toVector(), 0.5, 0.5));
    }

    /**
     * Returns the bounding box for the stair by checking the stair data and its surroundings.
     *
     * @param loc The location of the stair
     * @param stairs The stair data
     * @return The bounding box of the stair
     */
    private static List<BoundingBox> getStairsBoundingBoxes(Location loc, Stairs stairs) {
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
            return Collections.singletonList(base);
        }
    }


    /**
     * Returns the cauldron bounding box.
     *
     * @param loc The location of the cauldron
     * @param internalHeight The height of the inside of the cauldron
     * @return Returns the bounding boxes necessary to construct the cauldron collision
     */
    private static List<BoundingBox> getCauldronBoundingBoxes(Location loc, double internalHeight) {

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
     * @param block The location of the block
     * @return The List of boundingboxes for the block
     */
    private static List<BoundingBox> getSlabBoundingBoxes(Location loc, GlowBlock block) {
        MaterialData data = block.getState().getData();

        boolean inverted;

        if (data instanceof Step) {
            inverted = ((Step) data).isInverted();
        } else {
            inverted = ((WoodenStep) data).isInverted();
        }

        if (inverted) {
            Vector higherOrigin = loc.add(0.0, 0.5, 0.0).toVector();
            return Collections.singletonList(BoundingBox.fromCenterAndSize(higherOrigin, 1.0, 0.5));
        } else {
            return Collections.singletonList(BoundingBox.fromCenterAndSize(loc.toVector(), 1.0, 0.5));
        }
    }

    /**
     * Returns a boundingboxList with one boundingbox that is centered on the location.
     *
     * @param loc The location of the boundingbox
     * @param xzSize The width in the x and z axis
     * @param ySize The height
     * @return A boundingboxList with the specified boundingbox
     */
    private static List<BoundingBox> getBlockBoundingBoxesWithDimension(Location loc, double xzSize, double ySize) {
        Vector locationVector = loc.toVector();
        BoundingBox box = BoundingBox.fromCenterAndSize(locationVector, xzSize, ySize);
        return Collections.singletonList(box);
    }

    /**
     * Returns the brewing stand bounding boxes.
     *
     * @param loc The location of the boundingbox
     * @return A list with the brewing stand bounding boxes
     */
    private static List<BoundingBox> getBrewingStandBoundingBoxes(Location loc) {
        Vector locationVector = loc.toVector();
        return Arrays.asList(
                BoundingBox.fromCenterAndSize(locationVector, 1.0, 1.0 / 8.0),
                BoundingBox.fromCenterAndSize(locationVector, 2.0 / 16.0, 7.0 / 8.0)
        );
    }

    /**
     * Returns all the bounding boxes that are not edge cases or require special functions.
     *
     * @param loc The location of the bounding box
     * @param block The corresponding block
     * @return The List of boundingboxes that corresponds to the block
     */
    private static List<BoundingBox> getRemainingBoundingBoxes(Location loc, GlowBlock block) {
        switch (block.getType()) {
            case STEP:
            case WOOD_STEP:
            case PURPUR_SLAB:
            case STONE_SLAB2:
                return getSlabBoundingBoxes(loc, block);
            case COBBLE_WALL:
                return getWallBoundingBoxes(loc, block);
            case SNOW:
                double snowHeight = block.getState().getRawData() * 1.0 / 7.0;
                return getBlockBoundingBoxesWithDimension(loc, 1.0, snowHeight);
            case CAULDRON:
                return getCauldronBoundingBoxes(loc, 5.0 / 16.0);
            case HOPPER:
                return getCauldronBoundingBoxes(loc, 9.0 / 16.0);
            case BREWING_STAND:
                return getBrewingStandBoundingBoxes(loc);
            case THIN_GLASS:
            case STAINED_GLASS_PANE:
            case IRON_FENCE:
                return getPaneBoundingBoxes(loc, block);
            case SKULL:
                return getSkullBoundingBoxes(loc, ((Skull) block.getState().getData()));
            default:
                if (block.getType().isSolid()) {
                    return getBlockBoundingBoxesWithDimension(loc, 1.0, 1.0);
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

        Pair<Double, Double> size = MATERIALS_BOUNDINGBOX_SIZE.get(blockType);
        if (size != null) {
            return getBlockBoundingBoxesWithDimension(loc, size.getLeft(), size.getRight());
        }

        if (fences.contains(blockType)) {
            return getFenceBoundingBoxes(loc, block);
        } else if (fenceGates.contains(blockType)) {
            return getFenceGateBoundingBoxes(loc, block);
        } else if (stairs.contains(blockType)) {
            return getStairsBoundingBoxes(loc, ((Stairs) block.getState().getData()));
        } else if (doors.contains(blockType)) {
            return getDoorBoundingBoxes(loc, block);
        }

        return getRemainingBoundingBoxes(loc, block);
    }
}
