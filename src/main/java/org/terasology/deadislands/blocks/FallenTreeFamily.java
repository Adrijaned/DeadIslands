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
import org.terasology.math.Side;
import org.terasology.math.geom.Vector3i;
import org.terasology.naming.Name;
import org.terasology.registry.In;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockBuilderHelper;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.AbstractBlockFamily;
import org.terasology.world.block.family.BlockSections;
import org.terasology.world.block.family.RegisterBlockFamily;
import org.terasology.world.block.family.UpdatesWithNeighboursFamily;
import org.terasology.world.block.loader.BlockFamilyDefinition;
import org.terasology.world.block.shapes.BlockShape;


@RegisterBlockFamily("DeadIslands:fallenTree")
@BlockSections({"middle", "left_end", "right_end", "lone"})
public class FallenTreeFamily extends AbstractBlockFamily implements UpdatesWithNeighboursFamily {

    @In
    WorldProvider worldProvider;

    private TByteObjectMap<Block> blocks;
    BlockUri blockUri;

    public FallenTreeFamily(BlockFamilyDefinition definition, BlockShape shape, BlockBuilderHelper blockBuilder) {
        super(definition, shape, blockBuilder);
    }

    public FallenTreeFamily(BlockFamilyDefinition definition, BlockBuilderHelper blockBuilder) {
        super(definition, blockBuilder);
        blocks = new TByteObjectHashMap<>();
        blockUri = new BlockUri(definition.getUrn());
        addBlocks(definition, blockBuilder);
        this.setBlockUri(blockUri);
        this.setCategory(definition.getCategories());
    }

    private void addBlocks(BlockFamilyDefinition definition, BlockBuilderHelper blockBuilder) {
        Block block = makeBlock(definition, blockBuilder, "middle");
        blocks.put((byte) 0b10010, block);
        block = makeBlock(definition, blockBuilder, "left_end");
        blocks.put((byte) 0b10000, block);
        block = makeBlock(definition, blockBuilder, "right_end");
        blocks.put((byte) 0b10, block);
        block = makeBlock(definition, blockBuilder, "lone");
        blocks.put((byte) 0, block);
    }

    private Block makeBlock(BlockFamilyDefinition definition, BlockBuilderHelper blockBuilder, String section) {
        Block block = blockBuilder.constructSimpleBlock(definition, section);
        block.setUri(new BlockUri(blockUri, new Name(section)));
        block.setBlockFamily(this);
        return block;
    }


    private Block getBlockForLocation(Vector3i location) {
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

    @Override
    public Block getBlockForNeighborUpdate(Vector3i location, Block oldBlock) {
        return getBlockForLocation(location);
    }

    @Override
    public Block getBlockForPlacement(Vector3i location, Side attachmentSide, Side direction) {
        return getBlockForLocation(location);
    }

    @Override
    public Block getArchetypeBlock() {
        return blocks.get((byte) 0);
    }

    @Override
    public Block getBlockFor(BlockUri blockUri) {
        for (Block block : blocks.valueCollection()) {
            if (block.getURI().equals(blockUri)) {
                return block;
            }
        }

        return null;
    }

    @Override
    public Iterable<Block> getBlocks() {
        return blocks.valueCollection();
    }

    @Override
    public BlockUri getURI() {
        return blockUri;
    }

    @Override
    public String getDisplayName() {
        return "Fallen Tree";
    }
}
