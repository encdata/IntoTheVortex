package com.intothevortex.dimension;

import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.Holder;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.Blocks;

public final class VoidChunkGenerator extends ChunkGenerator {
    public static final MapCodec<VoidChunkGenerator> CODEC = MapCodec.unit(() -> new VoidChunkGenerator(null));

    public VoidChunkGenerator(BiomeSource biomeSource) { super(biomeSource); }

    @Override protected MapCodec<? extends ChunkGenerator> codec() { return CODEC; }
    @Override public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk) {}
    @Override public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess chunk) {}
    @Override public void spawnOriginalMobs(WorldGenRegion region) {}
    @Override public void createStructures(net.minecraft.core.RegistryAccess registryAccess, net.minecraft.world.level.chunk.ChunkGeneratorStructureState structureState, StructureManager structureManager, ChunkAccess chunk, net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager templateManager, net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension) {}
    @Override public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) { return CompletableFuture.completedFuture(chunk); }
    @Override public int getSeaLevel() { return -64; }
    @Override public int getMinY() { return -64; }
    @Override public int getGenDepth() { return 384; }
    @Override public int getBaseHeight(int x, int z, Heightmap.Types heightmap, LevelHeightAccessor level, RandomState randomState) { return level.getMinY(); }
    @Override public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) { return new NoiseColumn(level.getMinY(), java.util.stream.IntStream.range(0, level.getHeight()).mapToObj(value -> Blocks.AIR.defaultBlockState()).toArray(net.minecraft.world.level.block.state.BlockState[]::new)); }
    @Override public void addDebugScreenInfo(List<String> lines, RandomState randomState, net.minecraft.core.BlockPos pos) {}
}
