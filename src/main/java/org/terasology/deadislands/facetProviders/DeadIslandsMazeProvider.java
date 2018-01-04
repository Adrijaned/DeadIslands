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
package org.terasology.deadislands.facetProviders;

import org.terasology.deadislands.facets.DeadIslandsMazeFacet;
import org.terasology.deadislands.support.DeadIslandsMazeTile;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.WhiteNoise;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProviderPlugin;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generator.plugin.RegisterPlugin;

@RegisterPlugin
@Produces(DeadIslandsMazeFacet.class)
@Requires({
        @Facet(SeaLevelFacet.class),
        @Facet(SurfaceHeightFacet.class)
})
public class DeadIslandsMazeProvider implements FacetProviderPlugin {
    private Noise noise;
    private long seed;
    @Override
    public void setSeed(long seed) {
        noise = new WhiteNoise(seed);
        this.seed = seed;
    }

    @Override
    public void process(GeneratingRegion region) {
        Border3D border = region.getBorderForFacet(DeadIslandsMazeFacet.class);
        DeadIslandsMazeFacet facet = new DeadIslandsMazeFacet(region.getRegion(), border);
        int seaLevel = region.getRegionFacet(SeaLevelFacet.class).getSeaLevel();
        SurfaceHeightFacet heightFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        for (BaseVector2i coordinates : facet.getWorldRegion().contents()) {
            if (heightFacet.getWorld(coordinates) > seaLevel){
                facet.setWorld(coordinates, DeadIslandsMazeTile.OBSTRUCTED);
            } else if (noise.noise(coordinates.x(), coordinates.y()) > 0.9999) {
                facet.setWorld(coordinates, DeadIslandsMazeTile.DESIGNATED);
            } else {
                facet.setWorld(coordinates, DeadIslandsMazeTile.AVAILABLE);
            }
        }
        facet.seed = seed;
        region.setRegionFacet(DeadIslandsMazeFacet.class, facet);
    }
}
