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

import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.JointedModelPose;
import org.lgna.story.SJointedModel;
import org.lgna.story.implementation.BasicJointedModelImp;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.PropResource;

public enum LeatherBookResource implements PropResource {
  BLANK, GREEN, AQUA, LIME, YELLOW, RUST, VIOLET, BLUE, PERIWINKLE, MOSS, BROWN, TRIG, CALCULUS, GEOMETRY, LINEAR_ALGEBRA, ALGEBRA, PROBABILITY, STATISTICS, CALCULUS3_D, LEATHER_BOOK_TALL_BLANK, LEATHER_BOOK_TALL_GREEN, LEATHER_BOOK_TALL_AQUA, LEATHER_BOOK_TALL_LIME, LEATHER_BOOK_TALL_YELLOW, LEATHER_BOOK_TALL_RUST, LEATHER_BOOK_TALL_VIOLET, LEATHER_BOOK_TALL_BLUE, LEATHER_BOOK_TALL_PERIWINKLE, LEATHER_BOOK_TALL_MOSS, LEATHER_BOOK_TALL_BROWN, LEATHER_BOOK_TALL_TRIG, LEATHER_BOOK_TALL_CALCULUS, LEATHER_BOOK_TALL_GEOMETRY, LEATHER_BOOK_TALL_LINEAR_ALGEBRA, LEATHER_BOOK_TALL_ALGEBRA, LEATHER_BOOK_TALL_PROBABILITY, LEATHER_BOOK_TALL_STATISTICS, LEATHER_BOOK_TALL_CALCULUS3_D;

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId ROOT = new JointId(null, LeatherBookResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId BACK_COVER = new JointId(ROOT, LeatherBookResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId FRONT_COVER = new JointId(ROOT, LeatherBookResource.class);

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId[] JOINT_ID_ROOTS = {ROOT};

  public static final JointedModelPose OPEN_POSE = new JointedModelPose(new JointIdTransformationPair(FRONT_COVER, new Orientation(0.0, -0.717083913125823, 0.0, 0.6969868445933232), new Position(-0.022763729095458984, -1.0658140798173486E-16, -0.04561522603034973)), new JointIdTransformationPair(BACK_COVER, new Orientation(0.0, 0.7234047982217257, 0.0, 0.6904241434870194), new Position(-0.023308169096708298, -1.776356733521132E-16, 0.04543914273381233)));

  private final ImplementationAndVisualType resourceType;

  LeatherBookResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  LeatherBookResource(ImplementationAndVisualType resourceType) {
    this.resourceType = resourceType;
  }

  @Override
  public JointId[] getRootJointIds() {
    return LeatherBookResource.JOINT_ID_ROOTS;
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
