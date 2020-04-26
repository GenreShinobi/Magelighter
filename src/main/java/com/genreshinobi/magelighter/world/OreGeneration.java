package com.genreshinobi.magelighter.world;

import com.genreshinobi.magelighter.lists.BlockList;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class OreGeneration {

    public static void generateOre() {
        // Iterate over all biomes registered in Forge
        for(Biome biome : ForgeRegistries.BIOMES) {
            // Add Copper as a Biome Feature to all biomes registered in forge
            biome.addFeature(
                    GenerationStage.Decoration.UNDERGROUND_ORES,
                    Feature.ORE.withConfiguration(
                            new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockList.COPPER_ORE.getDefaultState(), 6))
                            .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 5, 64))));
        }
    }

}
