/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.deadislands.rasterizers;

import org.terasology.deadislands.facets.DeadIslandsBiomeFacet;
import org.terasology.deadislands.facets.DeadIslandsSoilThicknessFacet;
import org.terasology.deadislands.support.DeadIslandsBiome;
import org.terasology.math.ChunkMath;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Requires({
        @Facet(SeaLevelFacet.class),
        @Facet(SurfaceHeightFacet.class),
        @Facet(DeadIslandsSoilThicknessFacet.class),
        @Facet(DeadIslandsBiomeFacet.class)
})
public class DeadIslandsSurfaceRasterizer implements org.terasology.world.generation.WorldRasterizer {
    private Block dirtBlock, waterBlock, stoneBlock, sandBlock, gravelBlock;

    @Override
    public void initialize() {
        dirtBlock = CoreRegistry.get(BlockManager.class).getBlock("Core:Dirt");
        waterBlock = CoreRegistry.get(BlockManager.class).getBlock("Core:Water");
        stoneBlock = CoreRegistry.get(BlockManager.class).getBlock("Core:Stone");
        sandBlock = CoreRegistry.get(BlockManager.class).getBlock("Core:Sand");
        gravelBlock = CoreRegistry.get(BlockManager.class).getBlock("Core:Gravel");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        int seaLevel = chunkRegion.getFacet(SeaLevelFacet.class).getSeaLevel();
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        DeadIslandsSoilThicknessFacet soilThicknessFacet = chunkRegion.getFacet(DeadIslandsSoilThicknessFacet.class);
        DeadIslandsBiomeFacet biomeFacet = chunkRegion.getFacet(DeadIslandsBiomeFacet.class);
        for (Vector3i coordinates : chunkRegion.getRegion()) {
            float surfaceHeight = surfaceHeightFacet.getWorld(coordinates.x, coordinates.z);
            float soilThickness = soilThicknessFacet.getWorld(coordinates.x, coordinates.z);
            DeadIslandsBiome biome = biomeFacet.getWorld(coordinates);
            if (coordinates.y < surfaceHeight - soilThickness) {
                chunk.setBlock(ChunkMath.calcBlockPos(coordinates), stoneBlock);
            } else if (coordinates.y < surfaceHeight) {
                if (coordinates.y < surfaceHeight - (soilThickness / 2)){
                    chunk.setBlock(ChunkMath.calcBlockPos(coordinates), dirtBlock);
                } else if (biome == DeadIslandsBiome.BEACH){
                    chunk.setBlock(ChunkMath.calcBlockPos(coordinates), sandBlock);
                } else if (biome == DeadIslandsBiome.OCEAN && soilThickness > 6.5) {
                    chunk.setBlock(ChunkMath.calcBlockPos(coordinates), gravelBlock);
                } else {
                    chunk.setBlock(ChunkMath.calcBlockPos(coordinates), dirtBlock);
                }
            } else if (coordinates.y <= seaLevel) {
                chunk.setBlock(ChunkMath.calcBlockPos(coordinates), waterBlock);
            }
        }
    }
}
