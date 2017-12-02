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
import org.terasology.math.Region3i;
import org.terasology.math.Side;
import org.terasology.math.SideBitFlag;
import org.terasology.math.geom.BaseVector3i;
import org.terasology.math.geom.Vector3i;
import org.terasology.minecarts.blocks.RailsUpdateFamily;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizerPlugin;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generator.plugin.RegisterPlugin;

import java.util.Map;

@RegisterPlugin
public class RailNetworkRasterizer implements WorldRasterizerPlugin {

    private RailsUpdateFamily railFamily;
    private Block brick, air;

    @Override
    public void initialize() {
        railFamily = (RailsUpdateFamily) CoreRegistry.get(BlockManager.class).getBlockFamily("Rails:rails");
        brick = CoreRegistry.get(BlockManager.class).getBlock("Core:Brick");
        air = CoreRegistry.get(BlockManager.class).getBlock("Rails:rails");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {

        RailNetworkFacet railNetworkFacet = chunkRegion.getFacet(RailNetworkFacet.class);

        for (Map.Entry<BaseVector3i, RailNetwork> entry : railNetworkFacet.getWorldEntries().entrySet()) {
            Vector3i position = new Vector3i(entry.getKey()).addY(2);

            position.addX(-16);
            for (int i = 0; i < 32; i++) {
                chunk.setBlock(ChunkMath.calcBlockPos(position.addX(1)), railFamily.getBlockByConnection(SideBitFlag.getSides(Side.LEFT, Side.RIGHT)));
                chunk.setBlock(ChunkMath.calcBlockPos(position.addY(1)), air);
                chunk.setBlock(ChunkMath.calcBlockPos(position.addY(1)), brick);
                position.addY(-2);
            }
        }
    }
}
