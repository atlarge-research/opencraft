package net.glowstone.testutils;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.ItemTable;
import science.atlarge.opencraft.opencraft.block.blocktype.BlockType;
import science.atlarge.opencraft.opencraft.block.entity.BlockEntity;
import science.atlarge.opencraft.opencraft.chunk.GlowChunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.mockito.Mockito;

public class InMemoryBlockStorage {
    private Map<Location, GlowBlock> mockBlocks = new HashMap<>();

    private GlowBlock createMockBlock(Location location, Material type, byte data) {
        GlowBlock mockBlock = Mockito.mock(GlowBlock.class);
        when(mockBlock.getLocation()).thenReturn(location);
        when(mockBlock.getType()).thenReturn(type);
        when(mockBlock.getTypeId()).thenReturn(type.getId());
        when(mockBlock.getData()).thenReturn(data);

        BlockEntity entity = null;
        BlockType blockType = ItemTable.instance().getBlock(type);
        if (blockType != null) {
            GlowChunk mockChunk = Mockito.mock(GlowChunk.class);
            when(mockChunk.getBlock(anyInt(), anyInt(), anyInt())).thenReturn(mockBlock);
            entity = blockType.createBlockEntity(mockChunk, 0, 0, 0);
        }
        when(mockBlock.getBlockEntity()).thenReturn(entity);
        return mockBlock;
    }

    public GlowBlock getBlockAt(Location location) {
        return mockBlocks.computeIfAbsent(location, (k) -> createMockBlock(location, Material.AIR, (byte) 0));
    }

    public void setBlockType(Location location, Material type) {
        mockBlocks.put(location, createMockBlock(location, type, (byte) 0));
    }

    public void setBlockData(Location location, byte data) {
        mockBlocks.computeIfPresent(location, (k, v) -> {
            if (v.getType() == Material.AIR) {
                return v;
            } else {
                return createMockBlock(location, v.getType(), data);
            }
        });
    }
}
