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
package org.terasology.deadislands;

import org.terasology.core.world.generator.facetProviders.SeaLevelProvider;
import org.terasology.deadislands.facetProviders.DeadIslandsBiomeProvider;
import org.terasology.deadislands.facetProviders.DeadIslandsSoilThicknessProvider;
import org.terasology.deadislands.facetProviders.DeadIslandsSurfaceProvider;
import org.terasology.deadislands.facetProviders.DeadIslandsTreeProvider;
import org.terasology.deadislands.rasterizers.DeadIslandsBiomeRasterizer;
import org.terasology.deadislands.rasterizers.DeadIslandsTreeRasterizer;
import org.terasology.deadislands.rasterizers.DeadIslandsWorldRasterizer;
import org.terasology.engine.SimpleUri;
import org.terasology.registry.In;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;
import org.terasology.world.generator.plugin.WorldGeneratorPluginLibrary;

@RegisterWorldGenerator(id = "deadislands", displayName = "Dead Islands", description = "Dead islands in an endless sea.")
public class DeadIslandsWorldGenerator extends BaseFacetedWorldGenerator {
    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    public DeadIslandsWorldGenerator(SimpleUri uri) {
        super(uri);
    }

    @Override
    protected WorldBuilder createWorld() {
        return new WorldBuilder(worldGeneratorPluginLibrary)
                .setSeaLevel(4)
                .addProvider(new SeaLevelProvider(4))
                .addProvider(new DeadIslandsSurfaceProvider())
                .addProvider(new DeadIslandsTreeProvider())
                .addProvider(new DeadIslandsBiomeProvider())
                .addProvider(new DeadIslandsSoilThicknessProvider())
                .addRasterizer(new DeadIslandsWorldRasterizer())
                .addRasterizer(new DeadIslandsBiomeRasterizer())
                .addRasterizer(new DeadIslandsTreeRasterizer())
                .addPlugins();
    }
}
