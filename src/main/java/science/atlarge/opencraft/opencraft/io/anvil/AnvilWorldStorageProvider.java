package science.atlarge.opencraft.opencraft.io.anvil;

import java.io.File;
import lombok.Getter;
import science.atlarge.opencraft.opencraft.GlowWorld;
import science.atlarge.opencraft.opencraft.io.FunctionIoService;
import science.atlarge.opencraft.opencraft.io.PlayerDataService;
import science.atlarge.opencraft.opencraft.io.ScoreboardIoService;
import science.atlarge.opencraft.opencraft.io.StructureDataService;
import science.atlarge.opencraft.opencraft.io.WorldStorageProvider;
import science.atlarge.opencraft.opencraft.io.data.WorldFunctionIoService;
import science.atlarge.opencraft.opencraft.io.json.JsonPlayerStatisticIoService;
import science.atlarge.opencraft.opencraft.io.nbt.NbtPlayerDataService;
import science.atlarge.opencraft.opencraft.io.nbt.NbtScoreboardIoService;
import science.atlarge.opencraft.opencraft.io.nbt.NbtStructureDataService;
import science.atlarge.opencraft.opencraft.io.nbt.NbtWorldMetadataService;

/**
 * A {@link WorldStorageProvider} for the Anvil map format.
 */
public class AnvilWorldStorageProvider implements WorldStorageProvider {

    @Getter
    private final File folder;
    private final File dataDir;
    private GlowWorld world;
    @Getter
    private AnvilChunkIoService chunkIoService;
    @Getter
    private NbtWorldMetadataService metadataService;
    @Getter
    private StructureDataService structureDataService;
    @Getter(lazy = true)
    private final PlayerDataService playerDataService
            = new NbtPlayerDataService(world.getServer(), new File(folder, "playerdata"));
    @Getter(lazy = true)
    private final ScoreboardIoService scoreboardIoService
            = new NbtScoreboardIoService(world.getServer(), new File(folder, "data"));
    @Getter(lazy = true)
    private final JsonPlayerStatisticIoService playerStatisticIoService
            = new JsonPlayerStatisticIoService(world.getServer(), new File(folder, "stats"));
    @Getter(lazy = true)
    private final FunctionIoService functionIoService = new WorldFunctionIoService(world, dataDir);

    /**
     * Create an instance for the given root folder.
     * @param folder the root folder
     */
    public AnvilWorldStorageProvider(File folder) {
        this.folder = folder;
        this.dataDir = new File(folder, "data");
        this.dataDir.mkdirs();
    }

    @Override
    public void setWorld(GlowWorld world) {
        if (this.world != null) {
            throw new IllegalArgumentException("World is already set");
        }
        this.world = world;
        chunkIoService = new AnvilChunkIoService(folder);
        metadataService = new NbtWorldMetadataService(world, folder);
        dataDir.mkdirs();
        structureDataService = new NbtStructureDataService(world, dataDir);
    }
}
