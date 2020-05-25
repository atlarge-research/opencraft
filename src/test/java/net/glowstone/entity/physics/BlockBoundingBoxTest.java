package net.glowstone.entity.physics;

import java.util.List;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.util.Vectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.material.Step;
import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BlockBoundingBoxTest {

    @Mock
    private GlowBlock block;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(block.getLocation()).thenReturn(new Location(null,0,0,0));
    }

    @Test
    public void stepTest() {
        when(block.getType()).thenReturn(Material.STEP);
        Step step = new Step();
        step.setInverted(true);
        GlowBlockState state = mock(GlowBlockState.class);
        when(state.getData()).thenReturn(step);
        when(block.getState()).thenReturn(state);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(0, 0.5, 0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(1, 1, 1)));
    }

    @Test
    public void invertedStepTest() {
        when(block.getType()).thenReturn(Material.STEP);
        Step step = new Step();
        step.setInverted(false);
        GlowBlockState state = mock(GlowBlockState.class);
        when(state.getData()).thenReturn(step);
        when(block.getState()).thenReturn(state);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(0, 0, 0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(1, 0.5, 1)));
    }

    @Test
    public void snowTest() {
        when(block.getType()).thenReturn(Material.SNOW);
        byte data = 2;
        GlowBlockState state = new GlowBlockState(block);
        state.setRawData(data);
        when(block.getState()).thenReturn(state);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(0, 0, 0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(1, 2.0/7.0, 1)));
    }

    @Test
    public void enchantmentTableTest() {
        when(block.getType()).thenReturn(Material.ENCHANTMENT_TABLE);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(0, 0, 0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(1, 3.0/4.0, 1)));
    }

    @Test
    public void chestTest() {
        when(block.getType()).thenReturn(Material.CHEST);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(1.0 / 16.0, 0, 1.0 / 16.0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(15.0 / 16.0, 14.0 / 16.0, 15.0 / 16.0)));
    }

    @Test
    public void cactusTest() {
        when(block.getType()).thenReturn(Material.CACTUS);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(1.0 / 16.0, 0, 1.0 / 16.0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(15.0 / 16.0, 1.0, 15.0 / 16.0)));
    }

    @Test
    public void bedTest() {
        when(block.getType()).thenReturn(Material.BED_BLOCK);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(0, 0, 0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(1.0, 9.0 / 16.0, 1.0)));
    }

    @Test
    public void daylightDetectorTest() {
        when(block.getType()).thenReturn(Material.DAYLIGHT_DETECTOR);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(0, 0, 0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(1.0, 3.0 / 8.0, 1.0)));
    }

    @Test
    public void flowerPotTest() {
        when(block.getType()).thenReturn(Material.FLOWER_POT);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(5.0 / 16.0, 0, 5.0 / 16.0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(11.0 / 16.0, 3.0 / 8.0, 11.0 / 16.0)));
    }

    @Test
    public void soulSandTest() {
        when(block.getType()).thenReturn(Material.SOUL_SAND);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(0, 0, 0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(1.0, 7.0 / 8.0, 1.0)));
    }

    @Test
    public void endPortalFrameTest() {
        when(block.getType()).thenReturn(Material.ENDER_PORTAL_FRAME);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(0, 0, 0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(1.0, 13.0 / 16.0, 1.0)));
    }

    @Test
    public void waterLilyTest() {
        when(block.getType()).thenReturn(Material.WATER_LILY);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(0, 0, 0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(1.0, 1.0 / 64.0, 1.0)));
    }

    @Test
    public void cakeBlockTest() {
        when(block.getType()).thenReturn(Material.CAKE_BLOCK);
        List<BoundingBox> boxes = BlockBoundingBox.getBoundingBoxes(block);
        assertEquals(boxes.size(), 1);
        BoundingBox box = boxes.get(0);
        assertTrue(Vectors.equals(box.minCorner, new Vector(1.0 / 16.0, 0, 1.0 / 16.0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(15.0 / 16.0, 7.0 / 16.0, 15.0 / 16.0)));
    }

}
