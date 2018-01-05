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
import gnu.trove.map.hash.TByteObjectHashMap;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.math.Side;
import org.terasology.math.geom.Vector3i;
import org.terasology.naming.Name;
import org.terasology.registry.In;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockBuilderHelper;
import org.terasology.world.block.BlockComponent;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.block.family.BlockFamilyFactory;
import org.terasology.world.block.family.RegisterBlockFamilyFactory;
import org.terasology.world.block.items.OnBlockItemPlaced;
import org.terasology.world.block.loader.BlockFamilyDefinition;

import java.util.HashSet;
import java.util.Set;

@RegisterBlockFamilyFactory("DeadIslands:fallenTree")
public class FallenTreeFamilyFactory implements BlockFamilyFactory {

    @In
    private WorldProvider worldProvider;

    @In
    private BlockEntityRegistry blockEntityRegistry;

    public FallenTreeFamilyFactory() {
    }

    @Override
    public BlockFamily createBlockFamily(BlockFamilyDefinition definition, BlockBuilderHelper blockBuilder) {
        BlockUri blockUri = new BlockUri(definition.getUrn());
        TByteObjectMap<Block> blocks = new TByteObjectHashMap<>();
        Block block = blockBuilder.constructSimpleBlock(definition, "middle");
        block.setUri(new BlockUri(blockUri, new Name("middle")));
        blocks.put((byte) 0b10010, block);
        block = blockBuilder.constructSimpleBlock(definition, "left_end");
        block.setUri(new BlockUri(blockUri, new Name("left_end")));
        blocks.put((byte) 0b10000, block);
        block = blockBuilder.constructSimpleBlock(definition, "right_end");
        block.setUri(new BlockUri(blockUri, new Name("right_end")));
        blocks.put((byte) 0b10, block);
        block = blockBuilder.constructSimpleBlock(definition, "lone");
        block.setUri(new BlockUri(blockUri, new Name("lone")));
        blocks.put((byte) 0, block);
        BlockFamily family = new FallenTreeFamily(blockUri, definition.getCategories(), block, blocks);
        return family;
    }

    @Override
    public Set<String> getSectionNames() {
        Set<String> set = new HashSet<>();
        set.add("middle");
        set.add("left_end");
        set.add("right_end");
        set.add("lone");
        return set;
    }

    // Copied from Sample module, RomanColumnFamilyFactory, because it's after midnight already and my laptop is like 1 GB in swap currently
    @ReceiveEvent()
    public void onPlaceBlock(OnBlockItemPlaced event, EntityRef entity) {
        BlockComponent blockComponent = event.getPlacedBlock().getComponent(BlockComponent.class);
        if (blockComponent == null) {
            return;
        }

        Vector3i targetBlock = blockComponent.getPosition();
        processUpdateForBlockLocation(targetBlock);
    }

    private void processUpdateForBlockLocation(Vector3i blockLocation) {
        for (Side side : Side.values()) {
            Vector3i neighborLocation = new Vector3i(blockLocation);
            neighborLocation.add(side.getVector3i());
            if (worldProvider.isBlockRelevant(neighborLocation)) {
                Block neighborBlock = worldProvider.getBlock(neighborLocation);
                final BlockFamily blockFamily = neighborBlock.getBlockFamily();
                if (blockFamily instanceof FallenTreeFamily) {
                    FallenTreeFamily neighboursFamily = (FallenTreeFamily) blockFamily;
                    Block neighborBlockAfterUpdate = neighboursFamily.getBlockForNeighborUpdate(worldProvider, blockEntityRegistry, neighborLocation, neighborBlock);
                    if (neighborBlock != neighborBlockAfterUpdate) {
                        worldProvider.setBlock(neighborLocation, neighborBlockAfterUpdate);
                    }
                }
            }
        }
    }
}
