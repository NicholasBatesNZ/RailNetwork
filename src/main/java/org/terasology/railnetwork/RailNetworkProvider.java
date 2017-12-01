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

import org.terasology.math.TeraMath;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2f;
import org.terasology.utilities.procedural.BrownianNoise;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.PerlinNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProviderPlugin;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Updates;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generator.plugin.RegisterPlugin;

@RegisterPlugin
@Updates(@Facet(SurfaceHeightFacet.class))
public class RailNetworkProvider implements FacetProviderPlugin {

    private Noise networkNoise;

    @Override
    public void setSeed(long seed) {
        networkNoise = new SubSampledNoise(new BrownianNoise(new PerlinNoise(seed +3), 4), new Vector2f(0.001f, 0.001f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {

        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        float networkLength = 40;
        Rect2i processRegion = surfaceHeightFacet.getWorldRegion();

        for (BaseVector2i position : processRegion.contents()) {
            float additiveNetworkLength = networkNoise.noise(position.x(), position.y()) * networkLength;
            additiveNetworkLength = TeraMath.clamp(additiveNetworkLength, -networkLength, 0);

            surfaceHeightFacet.setWorld(position, surfaceHeightFacet.getWorld(position) + additiveNetworkLength);
        }
    }
}
