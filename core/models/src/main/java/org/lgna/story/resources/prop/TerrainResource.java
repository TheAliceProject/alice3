/*
 * Alice 3 End User License Agreement
 *
 * Copyright (c) 2006-2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may "Alice" appear in their name, without prior written permission of Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must display the following acknowledgement: "This product includes software developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is contributed by Electronic Arts Inc. and may be used for personal, non-commercial, and academic use only. Redistributions of any program source code that utilizes The Sims 2 Assets must also retain the copyright notice, list of conditions and the disclaimer contained in The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.lgna.story.resources.prop;

import org.lgna.story.SJointedModel;
import org.lgna.story.implementation.BasicJointedModelImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.PropResource;

public enum TerrainResource implements PropResource {
  SQUARE_DESERT,
  SQUARE_BEACH,
  SQUARE_SNOW,
  SQUARE_GRASS,
  SQUARE_GRASS_DARK,
  SQUARE_DRY_GRASS,
  SQUARE_FOREST_FLOOR,
  SQUARE_FOREST_FLOOR_BROWN,
  SQUARE_FOREST_FLOOR_RED,
  SQUARE_JUNGLE,
  SQUARE_ROCKY_BROWN,
  SQUARE_ROCKY_SAND,
  SQUARE_SAVANA_GRASS,
  SQUARE_SAVANA_GRASS_DRY,
  SQUARE_MOON,
  SQUARE_MARS,
  SQUARE_MARS_LIGHT,
  SQUARE_MAGIC_GRASS,
  CRESCENT_DESERT,
  CRESCENT_BEACH,
  CRESCENT_SNOW,
  CRESCENT_GRASS,
  CRESCENT_DRY_GRASS,
  CRESCENT_FOREST_FLOOR,
  CRESCENT_FOREST_FLOOR_BROWN,
  CRESCENT_FOREST_FLOOR_RED,
  CRESCENT_JUNGLE,
  CRESCENT_ROCKY_BROWN,
  CRESCENT_ROCKY_SAND,
  CRESCENT_SAVANA_GRASS,
  CRESCENT_SAVANA_GRASS_DRY,
  CRESCENT_MOON,
  CRESCENT_MARS,
  CRESCENT_MARS_LIGHT,
  CRESCENT_MAGIC_GRASS,
  BLOB_DESERT,
  BLOB_BEACH,
  BLOB_SNOW,
  BLOB_GRASS,
  BLOB_DRY_GRASS,
  BLOB_FOREST_FLOOR,
  BLOB_FOREST_FLOOR_BROWN,
  BLOB_FOREST_FLOOR_RED,
  BLOB_JUNGLE,
  BLOB_ROCKY_BROWN,
  BLOB_ROCKY_SAND,
  BLOB_SAVANA_GRASS,
  BLOB_SAVANA_GRASS_DRY,
  BLOB_MOON,
  BLOB_MARS,
  BLOB_MARS_LIGHT,
  BLOB_MAGIC_GRASS,
  OVAL_DESERT,
  OVAL_BEACH,
  OVAL_SNOW,
  OVAL_GRASS,
  OVAL_DRY_GRASS,
  OVAL_FOREST_FLOOR,
  OVAL_FOREST_FLOOR_BROWN,
  OVAL_FOREST_FLOOR_RED,
  OVAL_JUNGLE,
  OVAL_ROCKY_BROWN,
  OVAL_ROCKY_SAND,
  OVAL_SAVANA_GRASS,
  OVAL_SAVANA_GRASS_DRY,
  OVAL_MOON,
  OVAL_MARS,
  OVAL_MARS_LIGHT,
  OVAL_MAGIC_GRASS,
  FLAT_DESERT,
  FLAT_BEACH,
  FLAT_SNOW,
  FLAT_GRASS,
  FLAT_DRY_GRASS,
  FLAT_FOREST_FLOOR,
  FLAT_FOREST_FLOOR_BROWN,
  FLAT_FOREST_FLOOR_RED,
  FLAT_JUNGLE,
  FLAT_OCEAN,
  FLAT_OCEAN_NIGHT,
  FLAT_ICE,
  FLAT_BRICK,
  FLAT_WOOD,
  FLAT_SWAMP,
  FLAT_SWAMP_POISON,
  FLAT_SWAMP_WATER,
  FLAT_ROCKY_BROWN,
  FLAT_ROCKY_SAND,
  FLAT_SAVANA_GRASS,
  FLAT_SAVANA_GRASS_DRY,
  FLAT_MOON,
  FLAT_MARS,
  FLAT_MARS_LIGHT,
  FLAT_MAGIC_GRASS,
  FLAT_OVAL_DESERT,
  FLAT_OVAL_BEACH,
  FLAT_OVAL_SNOW,
  FLAT_OVAL_GRASS,
  FLAT_OVAL_DRY_GRASS,
  FLAT_OVAL_FOREST_FLOOR,
  FLAT_OVAL_FOREST_FLOOR_BROWN,
  FLAT_OVAL_FOREST_FLOOR_RED,
  FLAT_OVAL_JUNGLE,
  FLAT_OVAL_OCEAN,
  FLAT_OVAL_OCEAN_NIGHT,
  FLAT_OVAL_ICE,
  FLAT_OVAL_BRICK,
  FLAT_OVAL_WOOD,
  FLAT_OVAL_SWAMP,
  FLAT_OVAL_SWAMP_POISON,
  FLAT_OVAL_SWAMP_WATER,
  FLAT_OVAL_ROCKY_BROWN,
  FLAT_OVAL_ROCKY_SAND,
  FLAT_OVAL_SAVANA_GRASS,
  FLAT_OVAL_SAVANA_GRASS_DRY,
  FLAT_OVAL_MOON,
  FLAT_OVAL_MARS,
  FLAT_OVAL_MARS_LIGHT,
  FLAT_OVAL_MAGIC_GRASS,
  FLAT_OVAL_UNDERWATER_SAND;

  private final ImplementationAndVisualType resourceType;

  TerrainResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  TerrainResource(ImplementationAndVisualType resourceType) {
    this.resourceType = resourceType;
  }

  @Override
  public JointId[] getRootJointIds() {
    return new JointId[0];
  }

  @Override
  public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
    return this.resourceType.getFactory(this);
  }

  @Override
  public BasicJointedModelImp createImplementation(SJointedModel abstraction) {
    return new BasicJointedModelImp(abstraction, this.resourceType.getFactory(this));
  }
}
