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
package org.terasology.deadislands.blocks;

import gnu.trove.map.TByteObjectMap;
import org.terasology.math.Side;
import org.terasology.math.geom.Vector3i;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.UpdatesWithNeighboursFamily;

import java.util.List;

public class FallenTreeFamily extends UpdatesWithNeighboursFamily {
    private final TByteObjectMap<Block> blocks;

    public FallenTreeFamily(BlockUri blockUri, List<String> categories, Block archetypeBlock, TByteObjectMap<Block> blocks) {
        super(null, blockUri, categories, archetypeBlock, blocks, (byte) 0b10010);
        this.blocks = blocks;
    }

    @Override
    public Block getBlockForPlacement(WorldProvider worldProvider, BlockEntityRegistry blockEntityRegistry, Vector3i location, Side attachmentSide, Side direction) {
        return getBlockForLocation(worldProvider, location);
    }

    @Override
    public Block getBlockForNeighborUpdate(WorldProvider worldProvider, BlockEntityRegistry blockEntityRegistry, Vector3i location, Block oldBlock) {
        return getBlockForLocation(worldProvider, location);
    }

    private Block getBlockForLocation(WorldProvider worldProvider, Vector3i location) {
        Vector3i temp = new Vector3i(location);
        temp.add(Side.LEFT.getVector3i());
        boolean left = worldProvider.isBlockRelevant(temp) && worldProvider.getBlock(temp).getBlockFamily() instanceof FallenTreeFamily;
        temp = new Vector3i(location);
        temp.add(Side.RIGHT.getVector3i());
        boolean right = worldProvider.isBlockRelevant(temp) && worldProvider.getBlock(temp).getBlockFamily() instanceof FallenTreeFamily;
        if (!(left || right)) {
            return blocks.get((byte) 0);
        } else if (left && right) {
            return blocks.get((byte) 0b10010);
        } else if (right) {
            return blocks.get((byte) 0b10000);
        } else {
            return blocks.get((byte) 0b10);
        }
    }
}
