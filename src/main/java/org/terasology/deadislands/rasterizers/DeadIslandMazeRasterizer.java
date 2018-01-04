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

import org.terasology.deadislands.facets.DeadIslandsMazeFacet;
import org.terasology.deadislands.support.DeadIslandsMaze;
import org.terasology.deadislands.support.DeadIslandsMazeTile;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.WorldRasterizerPlugin;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generator.plugin.RegisterPlugin;

/**
 * Mazes are chunk centered - they use a custom class for their layout
 * and handling that would be too complicated for me.
 */

@RegisterPlugin
@Requires({
        @Facet(DeadIslandsMazeFacet.class),
        @Facet(SeaLevelFacet.class)
})
public class DeadIslandMazeRasterizer implements WorldRasterizerPlugin {
    private Block platform, downerWall, upperWall;

    @Override
    public void initialize() {
        platform = CoreRegistry.get(BlockManager.class).getBlock("Core:Plank");
        downerWall = CoreRegistry.get(BlockManager.class).getBlock("Core:Brick");
        upperWall = CoreRegistry.get(BlockManager.class).getBlock("Core:GreenLeaf");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        int seaLevel = chunkRegion.getFacet(SeaLevelFacet.class).getSeaLevel();
        if (chunk.getChunkWorldOffsetY() > seaLevel || chunk.getChunkWorldOffsetY() < seaLevel - chunk.getChunkSizeY()){
            return;
        }
        DeadIslandsMazeFacet mazeFacet = chunkRegion.getFacet(DeadIslandsMazeFacet.class);
        boolean isDesignated = false;
        for (BaseVector2i pos : mazeFacet.getRelativeRegion().contents()) {
            if (mazeFacet.get(pos) == DeadIslandsMazeTile.OBSTRUCTED) {
                return;
            }
            isDesignated = isDesignated || mazeFacet.get(pos) == DeadIslandsMazeTile.DESIGNATED;
        }
        if (!isDesignated) {
            return;
        }
        DeadIslandsMaze maze = new DeadIslandsMaze(mazeFacet.seed - chunk.getChunkWorldOffsetZ() + (long) (chunk.getChunkWorldOffsetX() * 1.1));
        int mazeSize = DeadIslandsMaze.mazeSize;
        for (BaseVector2i pos : Rect2i.createFromMinAndSize(0, 0, mazeSize, mazeSize).contents()) {
            chunk.setBlock(pos.x(), seaLevel, pos.y(), platform);
            if (maze.isWall(pos)) {
                chunk.setBlock(pos.x(), seaLevel + 1, pos.y(), downerWall);
                chunk.setBlock(pos.x(), seaLevel + 2, pos.y(), upperWall);
            }
        }

    }
}
