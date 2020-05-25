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

}
