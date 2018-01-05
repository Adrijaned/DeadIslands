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
import org.terasology.naming.Name;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockBuilderHelper;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.block.family.BlockFamilyFactory;
import org.terasology.world.block.family.RegisterBlockFamilyFactory;
import org.terasology.world.block.loader.BlockFamilyDefinition;

@RegisterBlockFamilyFactory("DeadIslands:fallenTree")
public class FallenTreeFamilyFactory implements BlockFamilyFactory {


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

}
