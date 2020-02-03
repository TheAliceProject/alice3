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

public enum BedDoubleResource implements PropResource {
  BASIC_GRAY_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  BASIC_BLUE_STRIPES_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  BASIC_LIGHT_BLUE_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  BASIC_BLUE_SQUARES_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  BASIC_WHITE_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  BASIC_OFF_WHITE_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  BASIC_PINK_FLOWERS_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  BASIC_YELLOW_FLOWERS_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  ART_NOUVEAU_BROWN_CIRCLES_ORANGE_FRAME(ImplementationAndVisualType.SIMS2),
  ART_NOUVEAU_BROWN_CIRCLES_RED_FRAME(ImplementationAndVisualType.SIMS2),
  ART_NOUVEAU_LEOPARD_ORANGE_FRAME(ImplementationAndVisualType.SIMS2),
  ART_NOUVEAU_LEOPARD_RED_FRAME(ImplementationAndVisualType.SIMS2),
  ART_NOUVEAU_WHITE_ORANGE_FRAME(ImplementationAndVisualType.SIMS2),
  ART_NOUVEAU_WHITE_RED_FRAME(ImplementationAndVisualType.SIMS2),
  ART_NOUVEAU_OFF_WHITE_ORANGE_FRAME(ImplementationAndVisualType.SIMS2),
  ART_NOUVEAU_OFF_WHITE_RED_FRAME(ImplementationAndVisualType.SIMS2),
  COLONIAL_BROWN_CIRCLES_CHERRY_FRAME(ImplementationAndVisualType.SIMS2),
  COLONIAL_BROWN_CIRCLES_MAHOGANY_FRAME(ImplementationAndVisualType.SIMS2),
  COLONIAL_PURPLE_CIRCLES_CHERRY_FRAME(ImplementationAndVisualType.SIMS2),
  COLONIAL_PURPLE_CIRCLES_MAHOGANY_FRAME(ImplementationAndVisualType.SIMS2),
  COLONIAL_RED_CIRCLES_CHERRY_FRAME(ImplementationAndVisualType.SIMS2),
  COLONIAL_RED_CIRCLES_MAHOGANY_FRAME(ImplementationAndVisualType.SIMS2),
  COLONIAL_WHITE_CHERRY_FRAME(ImplementationAndVisualType.SIMS2),
  COLONIAL_WHITE_MAHOGANY_FRAME(ImplementationAndVisualType.SIMS2),
  COLONIAL_OFF_WHITE_CHERRY_FRAME(ImplementationAndVisualType.SIMS2),
  COLONIAL_OFF_WHITE_MAHOGANY_FRAME(ImplementationAndVisualType.SIMS2),
  DESIGNER_BLUE_STRIPES_GRAY_FRAME(ImplementationAndVisualType.SIMS2),
  DESIGNER_BLUE_STRIPES_PLANK_FRAME(ImplementationAndVisualType.SIMS2),
  DESIGNER_LEOPARD_GRAY_FRAME(ImplementationAndVisualType.SIMS2),
  DESIGNER_LEOPARD_PLANK_FRAME(ImplementationAndVisualType.SIMS2),
  DESIGNER_BLUE_SQUARES_GRAY_FRAME(ImplementationAndVisualType.SIMS2),
  DESIGNER_BLUE_SQUARES_PLANK_FRAME(ImplementationAndVisualType.SIMS2),
  DESIGNER_OFF_WHITE_GRAY_FRAME(ImplementationAndVisualType.SIMS2),
  DESIGNER_OFF_WHITE_PLANK_FRAME(ImplementationAndVisualType.SIMS2),
  LOFT_GRAY_PURPLE_FRAME(ImplementationAndVisualType.SIMS2),
  LOFT_GRAY_GREEN_FRAME(ImplementationAndVisualType.SIMS2),
  LOFT_GRAY_RED_FRAME(ImplementationAndVisualType.SIMS2),
  LOFT_GRAY_LIGHT_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  LOFT_STRIPES_PURPLE_FRAME(ImplementationAndVisualType.SIMS2),
  LOFT_STRIPES_GREEN_FRAME(ImplementationAndVisualType.SIMS2),
  LOFT_STRIPES_RED_FRAME(ImplementationAndVisualType.SIMS2),
  LOFT_STRIPES_LIGHT_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  ORIENTAL_TAN_CIRCLES_EBONY_FRAME(ImplementationAndVisualType.SIMS2),
  ORIENTAL_TAN_CIRCLES_LIGHT_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  ORIENTAL_BROWN_CIRCLES_EBONY_FRAME(ImplementationAndVisualType.SIMS2),
  ORIENTAL_BROWN_CIRCLES_LIGHT_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  ORIENTAL_PURPLE_CIRCLES_EBONY_FRAME(ImplementationAndVisualType.SIMS2),
  ORIENTAL_PURPLE_CIRCLES_LIGHT_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  ORIENTAL_BLUE_CIRCLES_EBONY_FRAME(ImplementationAndVisualType.SIMS2),
  ORIENTAL_BLUE_CIRCLES_LIGHT_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  ORIENTAL_RED_CIRCLES_EBONY_FRAME(ImplementationAndVisualType.SIMS2),
  ORIENTAL_RED_CIRCLES_LIGHT_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  MODERN_LEOPARD_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  MODERN_LEOPARD_ORANGE_FRAME(ImplementationAndVisualType.SIMS2),
  MODERN_LEOPARD_GREEN_FRAME(ImplementationAndVisualType.SIMS2),
  MODERN_STRIPES_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  MODERN_STRIPES_ORANGE_FRAME(ImplementationAndVisualType.SIMS2),
  MODERN_STRIPES_GREEN_FRAME(ImplementationAndVisualType.SIMS2),
  MODERN_WHITE_WOOD_FRAME(ImplementationAndVisualType.SIMS2),
  MODERN_WHITE_ORANGE_FRAME(ImplementationAndVisualType.SIMS2),
  MODERN_WHITE_GREEN_FRAME(ImplementationAndVisualType.SIMS2);

  private final ImplementationAndVisualType resourceType;

  BedDoubleResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  BedDoubleResource(ImplementationAndVisualType resourceType) {
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
