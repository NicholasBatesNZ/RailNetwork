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
package org.terasology.railnetwork;

import org.terasology.math.geom.BaseVector2i;
import org.terasology.world.generation.*;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generator.plugin.RegisterPlugin;

import java.util.Random;

@RegisterPlugin
@Produces(RailNetworkFacet.class)
@Requires(@Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(sides = 32)))
public class RailNetworkProvider implements FacetProviderPlugin {

    @Override
    public void setSeed(long seed) { }

    @Override
    public void process(GeneratingRegion region) {
        Border3D border = region.getBorderForFacet(RailNetworkFacet.class).extendBy(0, 4, 16);
        RailNetworkFacet facet = new RailNetworkFacet(region.getRegion(), border);
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);

        for (BaseVector2i position : surfaceHeightFacet.getWorldRegion().contents()) {
            int surfaceHeight = (int) surfaceHeightFacet.getWorld(position);
            Random rand = new Random();

            if (facet.getWorldRegion().encompasses(position.getX(), surfaceHeight, position.getY())
                    && rand.nextInt(10000) >= 9999) {
                facet.setWorld(position.getX(), surfaceHeight, position.getY(), new RailNetwork());
            }
        }

        region.setRegionFacet(RailNetworkFacet.class, facet);
    }
}
