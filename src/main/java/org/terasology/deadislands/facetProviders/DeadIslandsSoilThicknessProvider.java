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

import org.terasology.deadislands.facets.DeadIslandsSoilThicknessFacet;
import org.terasology.deadislands.facets.DeadIslandsTreeFacet;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Vector2f;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.utilities.procedural.WhiteNoise;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;

@Produces(DeadIslandsSoilThicknessFacet.class)
public class DeadIslandsSoilThicknessProvider implements FacetProvider{
    Noise noise;
    @Override
    public void setSeed(long seed) {
        noise = new SubSampledNoise(new WhiteNoise(seed + 74534), new Vector2f(0.1f, 0.1f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {
        Border3D border = region.getBorderForFacet(DeadIslandsTreeFacet.class);
        DeadIslandsSoilThicknessFacet facet = new DeadIslandsSoilThicknessFacet(region.getRegion(), border);
        for (BaseVector2i coordinates : facet.getWorldRegion().contents()){
            float tempNoise = noise.noise(coordinates.x(), coordinates.y()) * 10;
            tempNoise *= tempNoise > 0 ? 1 : -1; // AKA Math.abs()
            facet.setWorld(coordinates, tempNoise + 2);
        }
        region.setRegionFacet(DeadIslandsSoilThicknessFacet.class, facet);
    }
}
