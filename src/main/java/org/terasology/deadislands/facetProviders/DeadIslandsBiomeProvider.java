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

import org.terasology.core.world.generator.facets.BiomeFacet;
import org.terasology.deadislands.DeadIslandsBiome;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.world.generation.*;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(BiomeFacet.class)
@Requires({
        @Facet(SurfaceHeightFacet.class),
        @Facet(SeaLevelFacet.class)
})
public class DeadIslandsBiomeProvider implements FacetProvider {

    @Override
    public void setSeed(long seed) {
    }

    @Override
    public void process(GeneratingRegion region) {
        int seaLevel = region.getRegionFacet(SeaLevelFacet.class).getSeaLevel();
        SurfaceHeightFacet heightFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        Border3D border = region.getBorderForFacet(BiomeFacet.class);
        BiomeFacet biomeFacet = new BiomeFacet(region.getRegion(), border);
        for (BaseVector2i coordinates : biomeFacet.getRelativeRegion().contents()) {
            if (heightFacet.get(coordinates) <= seaLevel){
                biomeFacet.set(coordinates, DeadIslandsBiome.OCEAN);
            } else if (heightFacet.get(coordinates) <= seaLevel + 1) {
                biomeFacet.set(coordinates, DeadIslandsBiome.BEACH);
            } else {
                biomeFacet.set(coordinates, DeadIslandsBiome.ISLAND);
            }
        }
        region.setRegionFacet(BiomeFacet.class, biomeFacet);
    }
}
