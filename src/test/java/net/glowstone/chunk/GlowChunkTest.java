package net.glowstone.chunk;

import static org.junit.jupiter.api.Assertions.*;

import net.glowstone.util.Coordinates;
import org.junit.jupiter.api.Test;

class GlowChunkTest {

    @Test
    void getCenterCoordinates() {
        final int chunkX = 4;
        final int chunkZ = -3;
        final int centerX = 72;
        final int centerZ = -40;
        final Coordinates coordinates = new Coordinates(centerX, centerZ);

        GlowChunk chunk = new GlowChunk(null, chunkX, chunkZ);

        assertEquals(coordinates, chunk.getCenterCoordinates());
    }
}