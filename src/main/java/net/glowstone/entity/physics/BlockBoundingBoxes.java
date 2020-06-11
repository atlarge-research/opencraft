package net.glowstone.entity.physics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.glowstone.block.GlowBlock;
import org.bukkit.Location;
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
     * @param face The face of the block to be checked as gate.
     * @param block The block that is of type fence.
     * @return True if the gate is aligned parallel to the fence, false if not.
     */
    private static boolean isFenceGateAligned(BlockFace face, GlowBlock block) {
        GlowBlock nextBlock = block.getRelative(face);
        switch (nextBlock.getType()) {
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
                BlockFace gateFace = ((Gate) nextBlock.getState().getData()).getFacing();
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
     * @param block The location of the block
     * @return true if the block is a fence, false otherwise
     */
    private static boolean canConnectFence(BlockFace face, GlowBlock block) {
        GlowBlock nextBlock = block.getRelative(face);
        switch (nextBlock.getType()) {
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
                return isFenceGateAligned(face, block);
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
                return nextBlock.getType().isOccluding();
        }
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
        switch (nextBlock.getType()) {
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
                return isFenceGateAligned(face, block);
            case COBBLE_WALL:
                return true;
            default:
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
     * Returns the bounding box corresponding to block glow block.
     *
     * @param block The location of the block
     * @return The bounding box
     */
    public static List<BoundingBox> getBoundingBoxes(GlowBlock block) {
        Location loc = block.getLocation().clone();
        switch (block.getType()) {
            case STEP:
            case WOOD_STEP:
            case PURPUR_SLAB:
            case STONE_SLAB2:
                return getSlabBoundingBoxes(loc, block);
            case FENCE:
            case NETHER_FENCE:
            case ACACIA_FENCE:
            case BIRCH_FENCE:
            case DARK_OAK_FENCE:
            case JUNGLE_FENCE:
            case SPRUCE_FENCE:
                return getFenceBoundingBoxes(loc, block);
            case COBBLE_WALL:
                return getWallBoundingBoxes(loc, block);
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
                return getFenceGateBoundingBoxes(loc, block);
            case SNOW:
                double snowHeight = block.getState().getRawData() * 1.0 / 7.0;
                return getBlockBoundingBoxesWithDimension(loc, 1.0, snowHeight);
            case ENCHANTMENT_TABLE:
                return getBlockBoundingBoxesWithDimension(loc, 1.0, 3.0 / 4.0);
            case CHEST:
            case ENDER_CHEST:
            case TRAPPED_CHEST:
                return getBlockBoundingBoxesWithDimension(loc, 14.0 / 16.0, 7.0 / 8.0);
            case CACTUS:
                return getBlockBoundingBoxesWithDimension(loc, 14.0 / 16.0, 1.0);
            case BED_BLOCK:
                return getBlockBoundingBoxesWithDimension(loc, 1.0, 9.0 / 16.0);
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
                return getBlockBoundingBoxesWithDimension(loc, 1.0, 3.0 / 8.0);
            case FLOWER_POT:
                return getBlockBoundingBoxesWithDimension(loc, 3.0 / 8.0, 3.0 / 8.0);
            case SOUL_SAND:
                return getBlockBoundingBoxesWithDimension(loc, 1.0, 7.0 / 8.0);
            case ENDER_PORTAL_FRAME:
                return getBlockBoundingBoxesWithDimension(loc, 1.0, 13.0 / 16.0);
            case WATER_LILY:
                return getBlockBoundingBoxesWithDimension(loc, 1.0, 1.0 / 64.0);
            case CAKE_BLOCK:
                return getBlockBoundingBoxesWithDimension(loc, 7.0 / 8.0, 7.0 / 16.0);
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
            case TRAP_DOOR:
            case IRON_TRAPDOOR:
            case REDSTONE_COMPARATOR:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
                return getBlockBoundingBoxesWithDimension(loc, 1.0, 1.0 / 8.0);
            case CARPET:
                return getBlockBoundingBoxesWithDimension(loc, 1.0, 1.0 / 16.0);
            case SKULL:
                return getSkullBoundingBoxes(loc, ((Skull) block.getState().getData()));
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
                return getStairsBoundingBoxes(loc, ((Stairs) block.getState().getData()));
            case GOLD_PLATE:
            case STONE_PLATE:
            case IRON_PLATE:
            case WOOD_PLATE:
                return getBlockBoundingBoxesWithDimension(loc, 14.0 / 16.0, 0.01);
            case CHORUS_PLANT:
                return getBlockBoundingBoxesWithDimension(loc, 10.0 / 16.0, 3.0 / 4.0);
            case SOIL:
                return getBlockBoundingBoxesWithDimension(loc, 1.0, 15.0 / 16.0);
            case END_ROD:
                return getBlockBoundingBoxesWithDimension(loc, 4.0 / 16.0, 1.0);
            case DARK_OAK_DOOR:
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case IRON_DOOR:
            case JUNGLE_DOOR:
            case SPRUCE_DOOR:
            case WOODEN_DOOR:
            case WOOD_DOOR:
            case IRON_DOOR_BLOCK:
                return getDoorBoundingBoxes(loc, block);
            default:
                if (block.getType().isSolid()) {
                    return getBlockBoundingBoxesWithDimension(loc, 1.0, 1.0);
                } else {
                    return Collections.emptyList();
                }
        }
    }
}
