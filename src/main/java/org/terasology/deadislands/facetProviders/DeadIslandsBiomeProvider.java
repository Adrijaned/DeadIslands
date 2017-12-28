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


import org.terasology.deadislands.DeadIslandsBiome;
import org.terasology.deadislands.facets.DeadIslandsBiomeFacet;
import org.terasology.deadislands.facets.DeadIslandsSoilThicknessFacet;
import org.terasology.math.geom.Vector3i;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Requires({
        @Facet(SurfaceHeightFacet.class),
        @Facet(DeadIslandsSoilThicknessFacet.class),
        @Facet(SeaLevelFacet.class)
})
@Produces(DeadIslandsBiomeFacet.class)
public class DeadIslandsBiomeProvider implements FacetProvider {
    @Override
    public void process(GeneratingRegion region) {
        int seaLevel = region.getRegionFacet(SeaLevelFacet.class).getSeaLevel();
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        DeadIslandsSoilThicknessFacet soilThicknessFacet = region.getRegionFacet(DeadIslandsSoilThicknessFacet.class);
        Border3D border = region.getBorderForFacet(DeadIslandsBiomeFacet.class);
        DeadIslandsBiomeFacet biomeFacet = new DeadIslandsBiomeFacet(region.getRegion(), border);
        for (Vector3i coordinates : biomeFacet.getWorldRegion()){
            if (coordinates.y >= seaLevel + 10 && coordinates.y >= surfaceHeightFacet.getWorld(coordinates.x, coordinates.z) + 10) {
                biomeFacet.setWorld(coordinates, DeadIslandsBiome.SKY);
                continue;
            }
            if (coordinates.y < surfaceHeightFacet.getWorld(coordinates.x, coordinates.z) - soilThicknessFacet.getWorld(coordinates.x, coordinates.z)) {
                biomeFacet.setWorld(coordinates, DeadIslandsBiome.UNDERGROUND);
                continue;
            }
            if (surfaceHeightFacet.getWorld(coordinates.x, coordinates.z) < seaLevel) {
                biomeFacet.setWorld(coordinates, DeadIslandsBiome.OCEAN);
                continue;
            }
            if (surfaceHeightFacet.getWorld(coordinates.x, coordinates.z) > seaLevel + 1) {
                biomeFacet.setWorld(coordinates, DeadIslandsBiome.ISLAND);
                continue;
            }
            biomeFacet.setWorld(coordinates, DeadIslandsBiome.BEACH);
        }
        region.setRegionFacet(DeadIslandsBiomeFacet.class, biomeFacet);
    }
}
