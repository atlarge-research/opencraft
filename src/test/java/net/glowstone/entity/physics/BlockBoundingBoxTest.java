package net.glowstone.entity.physics;

import net.glowstone.entity.physics.BlockBoundingBox;
import net.glowstone.util.Vectors;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class BlockBoundingBoxTest {

    @Mock
    private Block block;

    private BlockBoundingBox box;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(block.getX()).thenReturn(0);
        when(block.getY()).thenReturn(0);
        when(block.getZ()).thenReturn(0);
        box = new BlockBoundingBox(block);
    }

    @Test
    public void constructorTest() {
        assertTrue(Vectors.equals(box.minCorner, new Vector(0, 0, 0)));
        assertTrue(Vectors.equals(box.maxCorner, new Vector(1, 0.95, 1)));
    }

    @Test
    public void getSizeTest() {
        assertTrue(Vectors.equals(box.getSize(), new Vector(1, 1, 1)));
    }
}
