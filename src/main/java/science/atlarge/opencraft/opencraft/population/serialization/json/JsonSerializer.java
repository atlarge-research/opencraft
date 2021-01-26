package science.atlarge.opencraft.opencraft.population.serialization.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import it.unimi.dsi.fastutil.ints.IntList;
import science.atlarge.opencraft.opencraft.block.entity.BannerEntity;
import science.atlarge.opencraft.opencraft.block.entity.BeaconEntity;
import science.atlarge.opencraft.opencraft.block.entity.BedEntity;
import science.atlarge.opencraft.opencraft.block.entity.BlockEntity;
import science.atlarge.opencraft.opencraft.block.entity.BrewingStandEntity;
import science.atlarge.opencraft.opencraft.block.entity.ChestEntity;
import science.atlarge.opencraft.opencraft.block.entity.DispenserEntity;
import science.atlarge.opencraft.opencraft.block.entity.DropperEntity;
import science.atlarge.opencraft.opencraft.block.entity.EnchantingTableEntity;
import science.atlarge.opencraft.opencraft.block.entity.FlowerPotEntity;
import science.atlarge.opencraft.opencraft.block.entity.FurnaceEntity;
import science.atlarge.opencraft.opencraft.block.entity.HopperEntity;
import science.atlarge.opencraft.opencraft.block.entity.JukeboxEntity;
import science.atlarge.opencraft.opencraft.block.entity.MobSpawnerEntity;
import science.atlarge.opencraft.opencraft.block.entity.NoteblockEntity;
import science.atlarge.opencraft.opencraft.block.entity.SignEntity;
import science.atlarge.opencraft.opencraft.block.entity.SkullEntity;
import science.atlarge.opencraft.opencraft.chunk.GlowChunk;
import science.atlarge.opencraft.opencraft.generator.NetherGenerator;
import science.atlarge.opencraft.opencraft.generator.OverworldGenerator;
import science.atlarge.opencraft.opencraft.generator.SuperflatGenerator;
import science.atlarge.opencraft.opencraft.generator.TheEndGenerator;
import science.atlarge.opencraft.opencraft.generator.biomegrid.BiomeEdgeMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.BiomeMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.BiomeThinEdgeMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.BiomeVariationMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.ConstantBiomeMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.DeepOceanMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.ErosionMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.MapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.NoiseMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.RarePlainsMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.RiverMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.ShoreMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.SmoothMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.WhittakerMapLayer;
import science.atlarge.opencraft.opencraft.generator.biomegrid.ZoomMapLayer;
import science.atlarge.opencraft.opencraft.generator.decorators.EntityDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.nether.FireDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.nether.GlowstoneDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.nether.LavaDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.CactusDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.DeadBushDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.DesertWellDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.DoublePlantDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.EmeraldOreDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.FlowerDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.FlowingLiquidDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.IceDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.InfestedStoneDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.LakeDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.MelonDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.PumpkinDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.StoneBoulderDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.SugarCaneDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.SurfaceCaveDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.TallGrassDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.TreeDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.UnderwaterDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.overworld.WaterLilyDecorator;
import science.atlarge.opencraft.opencraft.generator.decorators.theend.ObsidianPillarDecorator;
import science.atlarge.opencraft.opencraft.generator.populators.NetherPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.OverworldPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.StructurePopulator;
import science.atlarge.opencraft.opencraft.generator.populators.TheEndPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.BiomePopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.BirchForestMountainsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.BirchForestPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.DesertMountainsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.DesertPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.DungeonPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.ExtremeHillsPlusPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.ExtremeHillsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.FlowerForestPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.ForestPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.IcePlainsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.IcePlainsSpikesPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.JungleEdgePopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.JunglePopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.MegaSpruceTaigaPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.MegaTaigaPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.MesaForestPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.MesaPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.MushroomIslandPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.OceanPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.PlainsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.RoofedForestPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.SavannaMountainsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.SavannaPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.SnowPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.SunflowerPlainsPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.SwamplandPopulator;
import science.atlarge.opencraft.opencraft.generator.populators.overworld.TaigaPopulator;
import science.atlarge.opencraft.opencraft.population.serialization.GlowSerializer;
import science.atlarge.opencraft.opencraft.population.serialization.json.adapters.*;
import science.atlarge.opencraft.opencraft.population.serialization.json.annotations.ExcludeField;
import science.atlarge.opencraft.opencraft.population.serialization.json.annotations.ExposeClass;
import science.atlarge.opencraft.opencraft.util.noise.PerlinNoise;
import science.atlarge.opencraft.opencraft.util.noise.PerlinOctaveGenerator;
import science.atlarge.opencraft.opencraft.util.noise.SimplexNoise;
import science.atlarge.opencraft.opencraft.util.noise.SimplexOctaveGenerator;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.PerlinNoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import java.lang.reflect.Type;


public class JsonSerializer implements GlowSerializer {
    private final Gson gson = getGson();

    private static Gson getGson() {
        ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                // always exclude if marked with ExcludeField
                if (fieldAttributes.getAnnotation(ExcludeField.class) != null) {
                    return true;
                }

                // if a class is not marked with ExposeClass, then always include all its fields
                if (!fieldAttributes.getDeclaringClass().isAnnotationPresent(ExposeClass.class)) {
                    return false;
                }

                // from the classes marked with ExposeClass, only include the fields marked with Expose
                return fieldAttributes.getAnnotation(Expose.class) == null;
            }

            @Override
            public boolean shouldSkipClass(Class<?> thisClass) {
                return false;
            }
        };

        RuntimeTypeAdapterFactory<MapLayer> mapLayerAdapter = RuntimeTypeAdapterFactory.of(MapLayer.class, "subclassType")
                .registerSubtype(BiomeEdgeMapLayer.class)
                .registerSubtype(BiomeMapLayer.class)
                .registerSubtype(BiomeThinEdgeMapLayer.class)
                .registerSubtype(BiomeVariationMapLayer.class)
                .registerSubtype(ConstantBiomeMapLayer.class)
                .registerSubtype(DeepOceanMapLayer.class)
                .registerSubtype(ErosionMapLayer.class)
                .registerSubtype(NoiseMapLayer.class)
                .registerSubtype(RarePlainsMapLayer.class)
                .registerSubtype(RiverMapLayer.class)
                .registerSubtype(ShoreMapLayer.class)
                .registerSubtype(SmoothMapLayer.class)
                .registerSubtype(WhittakerMapLayer.class)
                .registerSubtype(ZoomMapLayer.class);

        RuntimeTypeAdapterFactory<BlockEntity> blockEntityAdapter = RuntimeTypeAdapterFactory.of(BlockEntity.class, "subclassType")
                .registerSubtype(BannerEntity.class)
                .registerSubtype(BeaconEntity.class)
                .registerSubtype(BedEntity.class)
                .registerSubtype(BrewingStandEntity.class)
                .registerSubtype(ChestEntity.class)
                .registerSubtype(DispenserEntity.class)
                .registerSubtype(DropperEntity.class)
                .registerSubtype(FlowerPotEntity.class)
                .registerSubtype(FurnaceEntity.class)
                .registerSubtype(JukeboxEntity.class)
                .registerSubtype(HopperEntity.class)
                .registerSubtype(MobSpawnerEntity.class)
                .registerSubtype(NoteblockEntity.class)
                .registerSubtype(SignEntity.class)
                .registerSubtype(SkullEntity.class)
                .registerSubtype(EnchantingTableEntity.class);

        RuntimeTypeAdapterFactory<NoiseGenerator> noiseGeneratorAdapter = RuntimeTypeAdapterFactory.of(NoiseGenerator.class, "subclassType")
                .registerSubtype(PerlinNoise.class)
                .registerSubtype(PerlinNoiseGenerator.class)
                .registerSubtype(SimplexNoise.class)
                .registerSubtype(SimplexNoiseGenerator.class);

        RuntimeTypeAdapterFactory<OctaveGenerator> octaveGeneratorAdapter = RuntimeTypeAdapterFactory.of(OctaveGenerator.class, "subclassType")
                .registerSubtype(PerlinOctaveGenerator.class)
                .registerSubtype(SimplexOctaveGenerator.class);

        RuntimeTypeAdapterFactory<BlockPopulator> blockPopulatorAdapter = RuntimeTypeAdapterFactory.of(BlockPopulator.class, "subclassType")
                .registerSubtype(EntityDecorator.class)
                .registerSubtype(EmeraldOreDecorator.class)
                .registerSubtype(IceDecorator.class)
                .registerSubtype(MelonDecorator.class)
                .registerSubtype(PumpkinDecorator.class)
                .registerSubtype(StoneBoulderDecorator.class)
                .registerSubtype(NetherPopulator.class)
                .registerSubtype(OverworldPopulator.class)
                .registerSubtype(StructurePopulator.class)
                .registerSubtype(TheEndPopulator.class)
                .registerSubtype(science.atlarge.opencraft.opencraft.generator.populators.nether.OrePopulator.class, "NetherOrePopulator")
                .registerSubtype(BiomePopulator.class)
                .registerSubtype(DungeonPopulator.class)
                .registerSubtype(science.atlarge.opencraft.opencraft.generator.populators.overworld.OrePopulator.class, "OverworldOrePopulator")
                .registerSubtype(SnowPopulator.class)
                .registerSubtype(TallGrassDecorator.class)
                .registerSubtype(FireDecorator.class)
                .registerSubtype(GlowstoneDecorator.class)
                .registerSubtype(LavaDecorator.class)
                .registerSubtype(science.atlarge.opencraft.opencraft.generator.decorators.overworld.MushroomDecorator.class, "OverworldMushroomDecorator")
                .registerSubtype(science.atlarge.opencraft.opencraft.generator.decorators.nether.MushroomDecorator.class, "NetherMushroomDecorator")
                .registerSubtype(CactusDecorator.class)
                .registerSubtype(DeadBushDecorator.class)
                .registerSubtype(DesertWellDecorator.class)
                .registerSubtype(DoublePlantDecorator.class)
                .registerSubtype(FlowerDecorator.class)
                .registerSubtype(FlowingLiquidDecorator.class)
                .registerSubtype(InfestedStoneDecorator.class)
                .registerSubtype(LakeDecorator.class)
                .registerSubtype(SugarCaneDecorator.class)
                .registerSubtype(SurfaceCaveDecorator.class)
                .registerSubtype(TreeDecorator.class)
                .registerSubtype(UnderwaterDecorator.class)
                .registerSubtype(WaterLilyDecorator.class)
                .registerSubtype(ObsidianPillarDecorator.class)
                .registerSubtype(DesertPopulator.class)
                .registerSubtype(ExtremeHillsPlusPopulator.class)
                .registerSubtype(ExtremeHillsPopulator.class)
                .registerSubtype(ForestPopulator.class)
                .registerSubtype(IcePlainsPopulator.class)
                .registerSubtype(JunglePopulator.class)
                .registerSubtype(MesaPopulator.class)
                .registerSubtype(MushroomIslandPopulator.class)
                .registerSubtype(OceanPopulator.class)
                .registerSubtype(PlainsPopulator.class)
                .registerSubtype(SavannaPopulator.class)
                .registerSubtype(SwamplandPopulator.class)
                .registerSubtype(TaigaPopulator.class)
                .registerSubtype(BirchForestPopulator.class)
                .registerSubtype(DesertMountainsPopulator.class)
                .registerSubtype(FlowerForestPopulator.class)
                .registerSubtype(IcePlainsSpikesPopulator.class)
                .registerSubtype(JungleEdgePopulator.class)
                .registerSubtype(MegaTaigaPopulator.class)
                .registerSubtype(MesaForestPopulator.class)
                .registerSubtype(RoofedForestPopulator.class)
                .registerSubtype(SavannaMountainsPopulator.class)
                .registerSubtype(SunflowerPlainsPopulator.class)
                .registerSubtype(BirchForestMountainsPopulator.class)
                .registerSubtype(MegaSpruceTaigaPopulator.class);

        RuntimeTypeAdapterFactory<ChunkGenerator> chunkGeneratorAdapter = RuntimeTypeAdapterFactory.of(ChunkGenerator.class, "subclassType")
                .registerSubtype(NetherGenerator.class)
                .registerSubtype(OverworldGenerator.class)
                .registerSubtype(TheEndGenerator.class)
                .registerSubtype(SuperflatGenerator.class);

        return new GsonBuilder()
            .setExclusionStrategies(exclusionStrategy)
            .registerTypeAdapterFactory(mapLayerAdapter)
            .registerTypeAdapterFactory(blockEntityAdapter)
            .registerTypeAdapterFactory(noiseGeneratorAdapter)
            .registerTypeAdapterFactory(octaveGeneratorAdapter)
            .registerTypeAdapterFactory(blockPopulatorAdapter)
            .registerTypeAdapterFactory(chunkGeneratorAdapter)
            .registerTypeAdapter(TreeDecorator.TreeDecoration.class, new TreeDecorationSerializer())
            .registerTypeAdapter(TreeDecorator.TreeDecoration.class, new TreeDecorationDeserializer())
            .registerTypeAdapter(IntList.class, new IntListSerializer())
            .registerTypeAdapter(IntList.class, new IntListDeserializer())
            .registerTypeAdapter(GlowChunk.class, new ChunkSerializer())
            .enableComplexMapKeySerialization()  // enables Map<ComplexObject, Object>
            //.setPrettyPrinting()
            .create();
    }

    public String serialize(Object src, Type typeOfSrc) {
        return gson.toJson(src, typeOfSrc);
    }

    public String serialize(Object src) {
        return gson.toJson(src);
    }

    public <T> T deserialize(String src, Type typeOfT) {
        return gson.fromJson(src, typeOfT);
    }
}
