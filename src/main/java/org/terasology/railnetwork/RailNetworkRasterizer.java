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

import org.terasology.math.ChunkMath;
import org.terasology.math.Side;
import org.terasology.math.SideBitFlag;
import org.terasology.math.geom.Vector3i;
import org.terasology.minecarts.blocks.RailsUpdateFamily;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.segmentedpaths.blocks.PathFamily;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizerPlugin;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generator.plugin.RegisterPlugin;

@RegisterPlugin
public class RailNetworkRasterizer implements WorldRasterizerPlugin {

    private RailsUpdateFamily railFamily;

    @Override
    public void initialize() {
        railFamily = (RailsUpdateFamily) CoreRegistry.get(BlockManager.class).getBlockFamily("Rails:rails");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {

        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        SeaLevelFacet seaLevelFacet = chunkRegion.getFacet(SeaLevelFacet.class);
        int seaLevel = seaLevelFacet.getSeaLevel();

        for (Vector3i position : chunkRegion.getRegion()) {
            float surfaceHeight = surfaceHeightFacet.getWorld(position.x, position.z);

            if (position.y > seaLevel && position.y == surfaceHeight + 1) {
                chunk.setBlock(ChunkMath.calcBlockPos(position), railFamily.getBlockByConnection(SideBitFlag.getSides(Side.LEFT, Side.RIGHT)));
                break;
            }
        }
    }
}
