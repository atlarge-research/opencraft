package net.glowstone.lambda.population.serialization.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import it.unimi.dsi.fastutil.ints.IntList;
import net.glowstone.block.entity.BannerEntity;
import net.glowstone.block.entity.BeaconEntity;
import net.glowstone.block.entity.BedEntity;
import net.glowstone.block.entity.BlockEntity;
import net.glowstone.block.entity.BrewingStandEntity;
import net.glowstone.block.entity.ChestEntity;
import net.glowstone.block.entity.DispenserEntity;
import net.glowstone.block.entity.DropperEntity;
import net.glowstone.block.entity.EnchantingTableEntity;
import net.glowstone.block.entity.FlowerPotEntity;
import net.glowstone.block.entity.FurnaceEntity;
import net.glowstone.block.entity.HopperEntity;
import net.glowstone.block.entity.JukeboxEntity;
import net.glowstone.block.entity.MobSpawnerEntity;
import net.glowstone.block.entity.NoteblockEntity;
import net.glowstone.block.entity.SignEntity;
import net.glowstone.block.entity.SkullEntity;
import net.glowstone.chunk.GlowChunk;
import net.glowstone.generator.NetherGenerator;
import net.glowstone.generator.OverworldGenerator;
import net.glowstone.generator.SuperflatGenerator;
import net.glowstone.generator.TheEndGenerator;
import net.glowstone.generator.biomegrid.BiomeEdgeMapLayer;
import net.glowstone.generator.biomegrid.BiomeMapLayer;
import net.glowstone.generator.biomegrid.BiomeThinEdgeMapLayer;
import net.glowstone.generator.biomegrid.BiomeVariationMapLayer;
import net.glowstone.generator.biomegrid.ConstantBiomeMapLayer;
import net.glowstone.generator.biomegrid.DeepOceanMapLayer;
import net.glowstone.generator.biomegrid.ErosionMapLayer;
import net.glowstone.generator.biomegrid.MapLayer;
import net.glowstone.generator.biomegrid.NoiseMapLayer;
import net.glowstone.generator.biomegrid.RarePlainsMapLayer;
import net.glowstone.generator.biomegrid.RiverMapLayer;
import net.glowstone.generator.biomegrid.ShoreMapLayer;
import net.glowstone.generator.biomegrid.SmoothMapLayer;
import net.glowstone.generator.biomegrid.WhittakerMapLayer;
import net.glowstone.generator.biomegrid.ZoomMapLayer;
import net.glowstone.generator.decorators.EntityDecorator;
import net.glowstone.generator.decorators.nether.FireDecorator;
import net.glowstone.generator.decorators.nether.GlowstoneDecorator;
import net.glowstone.generator.decorators.nether.LavaDecorator;
import net.glowstone.generator.decorators.overworld.CactusDecorator;
import net.glowstone.generator.decorators.overworld.DeadBushDecorator;
import net.glowstone.generator.decorators.overworld.DesertWellDecorator;
import net.glowstone.generator.decorators.overworld.DoublePlantDecorator;
import net.glowstone.generator.decorators.overworld.EmeraldOreDecorator;
import net.glowstone.generator.decorators.overworld.FlowerDecorator;
import net.glowstone.generator.decorators.overworld.FlowingLiquidDecorator;
import net.glowstone.generator.decorators.overworld.IceDecorator;
import net.glowstone.generator.decorators.overworld.InfestedStoneDecorator;
import net.glowstone.generator.decorators.overworld.LakeDecorator;
import net.glowstone.generator.decorators.overworld.MelonDecorator;
import net.glowstone.generator.decorators.overworld.PumpkinDecorator;
import net.glowstone.generator.decorators.overworld.StoneBoulderDecorator;
import net.glowstone.generator.decorators.overworld.SugarCaneDecorator;
import net.glowstone.generator.decorators.overworld.SurfaceCaveDecorator;
import net.glowstone.generator.decorators.overworld.TallGrassDecorator;
import net.glowstone.generator.decorators.overworld.TreeDecorator;
import net.glowstone.generator.decorators.overworld.UnderwaterDecorator;
import net.glowstone.generator.decorators.overworld.WaterLilyDecorator;
import net.glowstone.generator.decorators.theend.ObsidianPillarDecorator;
import net.glowstone.generator.populators.NetherPopulator;
import net.glowstone.generator.populators.OverworldPopulator;
import net.glowstone.generator.populators.StructurePopulator;
import net.glowstone.generator.populators.TheEndPopulator;
import net.glowstone.generator.populators.overworld.BiomePopulator;
import net.glowstone.generator.populators.overworld.BirchForestMountainsPopulator;
import net.glowstone.generator.populators.overworld.BirchForestPopulator;
import net.glowstone.generator.populators.overworld.DesertMountainsPopulator;
import net.glowstone.generator.populators.overworld.DesertPopulator;
import net.glowstone.generator.populators.overworld.DungeonPopulator;
import net.glowstone.generator.populators.overworld.ExtremeHillsPlusPopulator;
import net.glowstone.generator.populators.overworld.ExtremeHillsPopulator;
import net.glowstone.generator.populators.overworld.FlowerForestPopulator;
import net.glowstone.generator.populators.overworld.ForestPopulator;
import net.glowstone.generator.populators.overworld.IcePlainsPopulator;
import net.glowstone.generator.populators.overworld.IcePlainsSpikesPopulator;
import net.glowstone.generator.populators.overworld.JungleEdgePopulator;
import net.glowstone.generator.populators.overworld.JunglePopulator;
import net.glowstone.generator.populators.overworld.MegaSpruceTaigaPopulator;
import net.glowstone.generator.populators.overworld.MegaTaigaPopulator;
import net.glowstone.generator.populators.overworld.MesaForestPopulator;
import net.glowstone.generator.populators.overworld.MesaPopulator;
import net.glowstone.generator.populators.overworld.MushroomIslandPopulator;
import net.glowstone.generator.populators.overworld.OceanPopulator;
import net.glowstone.generator.populators.overworld.PlainsPopulator;
import net.glowstone.generator.populators.overworld.RoofedForestPopulator;
import net.glowstone.generator.populators.overworld.SavannaMountainsPopulator;
import net.glowstone.generator.populators.overworld.SavannaPopulator;
import net.glowstone.generator.populators.overworld.SnowPopulator;
import net.glowstone.generator.populators.overworld.SunflowerPlainsPopulator;
import net.glowstone.generator.populators.overworld.SwamplandPopulator;
import net.glowstone.generator.populators.overworld.TaigaPopulator;
import net.glowstone.lambda.population.serialization.GlowSerializer;
import net.glowstone.lambda.population.serialization.json.adapters.*;
import net.glowstone.lambda.population.serialization.json.annotations.ExcludeField;
import net.glowstone.lambda.population.serialization.json.annotations.ExposeClass;
import net.glowstone.util.noise.PerlinNoise;
import net.glowstone.util.noise.PerlinOctaveGenerator;
import net.glowstone.util.noise.SimplexNoise;
import net.glowstone.util.noise.SimplexOctaveGenerator;
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
                .registerSubtype(net.glowstone.generator.populators.nether.OrePopulator.class, "NetherOrePopulator")
                .registerSubtype(BiomePopulator.class)
                .registerSubtype(DungeonPopulator.class)
                .registerSubtype(net.glowstone.generator.populators.overworld.OrePopulator.class, "OverworldOrePopulator")
                .registerSubtype(SnowPopulator.class)
                .registerSubtype(TallGrassDecorator.class)
                .registerSubtype(FireDecorator.class)
                .registerSubtype(GlowstoneDecorator.class)
                .registerSubtype(LavaDecorator.class)
                .registerSubtype(net.glowstone.generator.decorators.overworld.MushroomDecorator.class, "OverworldMushroomDecorator")
                .registerSubtype(net.glowstone.generator.decorators.nether.MushroomDecorator.class, "NetherMushroomDecorator")
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
