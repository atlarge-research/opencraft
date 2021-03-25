package science.atlarge.opencraft.opencraft.serverless;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import science.atlarge.opencraft.opencraft.block.GlowBlock;
import science.atlarge.opencraft.opencraft.block.GlowBlockState;

import java.util.Arrays;

public class BlockSet {

    static class Coordinates {
        int x;
        int y;
        int z;

        Coordinates(int x, int y, int z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        static Coordinates from(int x, int y, int z){
            return new Coordinates(x,y,z);
        }
    }

    @Setter
    @Getter
    GlowBlockState[] blocks;

    @Setter
    @Getter
    int size_1;

    @Setter
    @Getter
    int size_2;

    @Setter
    @Getter
    int size_3;

    @Setter
    @Getter
    int offset;

    public BlockSet(){}

    public BlockSet(BlockSet s){
        //copy constructor
    }

    public BlockSet(GlowBlock centerBlock, int size){
        if(size <= 0){
            throw new RuntimeException("parameter size must be a positive non-zero number");
        }

        this.size_1 = (size * 2) + 1;
        this.size_2 = size_1 * size_1;
        this.size_3 = size_2 * size_1;
        this.offset = size;

        this.blocks = new GlowBlockState[size_3];

        for (int i = 0; i < size_1; i++){
            for (int j = 0; j < size_1; j++){
                for (int k = 0; k < size_1; k++){
                    blocks[CoordinatesToIndex(k,j,i)] =
                        centerBlock
                            .getRelative( k - offset, j - offset, i - offset)
                            .getState();
                }
            }
        }
    }

    int CoordinatesToIndex(Coordinates c){
        return c.x + (c.y * size_1) + (c.z * size_2);
    }
    int CoordinatesToIndex(int x, int y, int z){
        return x + (y * size_1) + (z * size_2);
    }

    Coordinates indexToCoordinates(int index){
        return new Coordinates(index % size_1, (index / size_1) % size_1, index / size_2);
    }

    GlowBlockState getRelative(int index, Coordinates coords){

        Coordinates c = indexToCoordinates(index);
        Coordinates target = Coordinates.from(c.x + coords.x, c.y + coords.y, c.z + coords.x);

        try{
            return blocks[CoordinatesToIndex(target)];
        }catch(IndexOutOfBoundsException e){
            return null;
        }
    }

    //Sugarcane behavior

    int currentlyGrowing = 0;
    public void update(){

        if(currentlyGrowing >= 2) return;

        int index = CoordinatesToIndex(offset, offset + currentlyGrowing, offset);
        GlowBlockState currentBlock = blocks[index];
        GlowBlockState blockAbove = getRelative(index, Coordinates.from(0,1,0));

        if(blockAbove.getTypeId() != 0) return;

            if (currentBlock.getRawData() < 15) {
                // increase age
                currentBlock.setRawData((byte) (currentBlock.getRawData() + 1));
//                currentBlock.update(true);
            } else {
                // grow the sugar cane on the above block
                currentBlock.setRawData((byte) 0);
//                currentBlock.update(true);

                blockAbove.setType(Material.SUGAR_CANE_BLOCK);
                blockAbove.setRawData((byte) 0);
                currentlyGrowing++;
                //TODO: handle event triggers, updates and physics server-side
//                BlockGrowEvent growEvent = new BlockGrowEvent(blockAbove, state);
//                EventFactory.getInstance().callEvent(growEvent);
//                if (!growEvent.isCancelled()) {
//                    state.update(true);
//                }
//                updatePhysics(blockAbove);
            }
    }

    public boolean contains(int x, int y, int z){
        return Arrays.stream(blocks)
                .anyMatch(block -> block.getX() == x && block.getY() == y && block.getZ() == z);
    }

    public GlowBlockState get(int x, int y, int z){
        return blocks[CoordinatesToIndex(x,y,z)];
    }
}
