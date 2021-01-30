package science.atlarge.opencraft.opencraft.generator.decorators.overworld;

import java.util.Random;
import science.atlarge.opencraft.opencraft.generator.decorators.BlockDecorator;
import science.atlarge.opencraft.opencraft.generator.objects.SugarCane;
import org.bukkit.Chunk;
import org.bukkit.World;

public class SugarCaneDecorator extends BlockDecorator {

    @Override
    public void decorate(World world, Random random, Chunk source) {
        int sourceX = (source.getX() << 4) + random.nextInt(16);
        int sourceZ = (source.getZ() << 4) + random.nextInt(16);
        int maxY = world.getHighestBlockYAt(sourceX, sourceZ);
        if (maxY <= 0) {
            return;
        }
        int sourceY = random.nextInt(maxY << 1);
        for (int j = 0; j < 20; j++) {
            int x = sourceX + random.nextInt(4) - random.nextInt(4);
            int z = sourceZ + random.nextInt(4) - random.nextInt(4);
            new SugarCane().generate(world, random, x, sourceY, z);
        }
    }
}
