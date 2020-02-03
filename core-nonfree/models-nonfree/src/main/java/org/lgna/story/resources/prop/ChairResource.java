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

public enum ChairResource implements PropResource {
  ART_NOUVEAU_DARK_WOOD(ImplementationAndVisualType.SIMS2),
  ART_NOUVEAU_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  ART_NOUVEAU_WOOD(ImplementationAndVisualType.SIMS2),
  CLUB_DARK_GREEN(ImplementationAndVisualType.SIMS2),
  CLUB_GREEN(ImplementationAndVisualType.SIMS2),
  CLUB_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  CLUB_DARK_WOOD(ImplementationAndVisualType.SIMS2),
  COLONIAL_BLUE_PATTERN(ImplementationAndVisualType.SIMS2),
  COLONIAL_DIAMONDS(ImplementationAndVisualType.SIMS2),
  COLONIAL_YELLOW(ImplementationAndVisualType.SIMS2),
  COLONIAL_GOLD_PATTERN(ImplementationAndVisualType.SIMS2),
  COLONIAL_BLUE(ImplementationAndVisualType.SIMS2),
  COLONIAL_RED_STRIPES(ImplementationAndVisualType.SIMS2),
  FANCY_COLONIAL_TAN(ImplementationAndVisualType.SIMS2),
  FANCY_COLONIAL_BLUE_PATTERN(ImplementationAndVisualType.SIMS2),
  FANCY_COLONIAL_BLUE(ImplementationAndVisualType.SIMS2),
  FANCY_COLONIAL_DIAMONDS(ImplementationAndVisualType.SIMS2),
  FANCY_COLONIAL_GOLD_PATTERN(ImplementationAndVisualType.SIMS2),
  FANCY_COLONIAL_YELLOW(ImplementationAndVisualType.SIMS2),
  FANCY_COLONIAL_RED_STRIPES(ImplementationAndVisualType.SIMS2),
  PARK_CHESTNUT(ImplementationAndVisualType.SIMS2),
  PARK_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  PARK_OAK(ImplementationAndVisualType.SIMS2),
  PARK_BLUE(ImplementationAndVisualType.SIMS2),
  PARK_GREEN(ImplementationAndVisualType.SIMS2),
  PARK_RED(ImplementationAndVisualType.SIMS2),
  PARK_WALNUT(ImplementationAndVisualType.SIMS2),
  DANISH_MODERN_BLUE(ImplementationAndVisualType.SIMS2),
  DANISH_MODERN_GREEN(ImplementationAndVisualType.SIMS2),
  DANISH_MODERN_PURPLE(ImplementationAndVisualType.SIMS2),
  DANISH_MODERN_RED(ImplementationAndVisualType.SIMS2),
  DANISH_MODERN_WHITE(ImplementationAndVisualType.SIMS2),
  LOFT_FORK_BLUE_BLACK(ImplementationAndVisualType.SIMS2),
  LOFT_FORK_BLUE_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  LOFT_FORK_GREEN_BLACK(ImplementationAndVisualType.SIMS2),
  LOFT_FORK_GREEN_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  LOFT_FORK_YELLOW_BLACK(ImplementationAndVisualType.SIMS2),
  LOFT_FORK_YELLOW_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  LOFT_FORK_RED_BLACK(ImplementationAndVisualType.SIMS2),
  LOFT_FORK_RED_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  LOFT_FORK_WHITE_BLACK(ImplementationAndVisualType.SIMS2),
  LOFT_FORK_WHITE_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  LOFT_OFFICE_BLUE_BLACK(ImplementationAndVisualType.SIMS2),
  LOFT_OFFICE_BLUE_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  LOFT_OFFICE_GREEN_BLACK(ImplementationAndVisualType.SIMS2),
  LOFT_OFFICE_GREEN_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  LOFT_OFFICE_YELLOW_BLACK(ImplementationAndVisualType.SIMS2),
  LOFT_OFFICE_YELLOW_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  LOFT_OFFICE_RED_BLACK(ImplementationAndVisualType.SIMS2),
  LOFT_OFFICE_RED_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  LOFT_OFFICE_WHITE_BLACK(ImplementationAndVisualType.SIMS2),
  LOFT_OFFICE_WHITE_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  MODERATE_BLUE_BLACK_BODY(ImplementationAndVisualType.SIMS2),
  MODERATE_BLUE_WOOD_BODY(ImplementationAndVisualType.SIMS2),
  MODERATE_YELLOW_STRIPES_BLACK_BODY(ImplementationAndVisualType.SIMS2),
  MODERATE_YELLOW_STRIPES_WOOD_BODY(ImplementationAndVisualType.SIMS2),
  MODERATE_GRAY_BLACK_BODY(ImplementationAndVisualType.SIMS2),
  MODERATE_GRAY_WOOD_BODY(ImplementationAndVisualType.SIMS2),
  MODERATE_RED_BLACK_BODY(ImplementationAndVisualType.SIMS2),
  MODERATE_RED_WOOD_BODY(ImplementationAndVisualType.SIMS2),
  MODERATE_TEAL_BLACK_BODY(ImplementationAndVisualType.SIMS2),
  MODERATE_TEAL_WOOD_BODY(ImplementationAndVisualType.SIMS2),
  MODERATE_YELLOW_BLACK_BODY(ImplementationAndVisualType.SIMS2),
  MODERATE_YELLOW_WOOD_BODY(ImplementationAndVisualType.SIMS2),
  MOROCCAN_YELLOW(ImplementationAndVisualType.SIMS2),
  MOROCCAN_BLUE_STRIPES(ImplementationAndVisualType.SIMS2),
  MOROCCAN_RED(ImplementationAndVisualType.SIMS2),
  MOROCCAN_WHITE(ImplementationAndVisualType.SIMS2),
  ORIENTAL_BLACK(ImplementationAndVisualType.SIMS2),
  ORIENTAL_LIGHT_WOOD(ImplementationAndVisualType.SIMS2),
  ORIENTAL_ORANGE_WOOD(ImplementationAndVisualType.SIMS2),
  ORIENTAL_RED(ImplementationAndVisualType.SIMS2),
  ORIENTAL_WOOD(ImplementationAndVisualType.SIMS2);

  private final ImplementationAndVisualType resourceType;

  ChairResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  ChairResource(ImplementationAndVisualType resourceType) {
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
