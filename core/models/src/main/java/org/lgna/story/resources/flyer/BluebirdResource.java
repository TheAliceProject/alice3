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

package org.lgna.story.resources.flyer;

import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.JointedModelPose;
import org.lgna.story.SFlyer;
import org.lgna.story.implementation.FlyerImp;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.FlyerResource;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

public enum BluebirdResource implements FlyerResource {
  DEFAULT;

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_TOE = new JointId(LEFT_FOOT, BluebirdResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_TOE = new JointId(RIGHT_FOOT, BluebirdResource.class);

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointedModelPose SPREAD_WINGS_POSE = new JointedModelPose(new JointIdTransformationPair(LEFT_WING_WRIST, new Orientation(-0.06357764977232984, 0.16086790948730237, -0.0146460973385253, 0.9848171860702688), new Position(-5.218048202503346E-17, 0.0, -0.0982094332575798)), new JointIdTransformationPair(RIGHT_WING_SHOULDER, new Orientation(-0.07957606406212243, -0.6294775090520338, 0.3720211156734822, 0.677514579266812), new Position(0.0666263997554779, 0.0114509342238307, 0.014014306478202343)),
                                                                                                                                          new JointIdTransformationPair(RIGHT_WING_ELBOW, new Orientation(-0.12715618690940483, 0.21347454184396453, -0.06278849348138876, 0.966601018622217), new Position(-9.769962563761818E-17, 3.552713599391162E-17, -0.06674163043498993)), new JointIdTransformationPair(LEFT_WING_ELBOW, new Orientation(-0.12715118488755633, -0.21347548494270613, 0.06278794100385973, 0.966601504227395), new Position(-1.0658140798173486E-16, -3.552713599391162E-17, -0.06674181669950485)),
                                                                                                                                          new JointIdTransformationPair(RIGHT_WING_WRIST, new Orientation(-0.06357727284782241, -0.16086623723882798, 0.014645752559851876, 0.9848174886880362), new Position(-2.620126221648339E-16, 1.0658140798173486E-16, -0.09821022301912308)), new JointIdTransformationPair(LEFT_WING_SHOULDER, new Orientation(-0.0795791035829649, 0.629478646262877, -0.3720181006716689, 0.6775148211992351), new Position(-0.06662635505199432, 0.011451060883700848, 0.014014254324138165)));

  @Override
  public JointedModelPose getSpreadWingsPose() {
    return BluebirdResource.SPREAD_WINGS_POSE;
  }

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointedModelPose FOLD_WINGS_POSE = new JointedModelPose(new JointIdTransformationPair(LEFT_WING_WRIST, new Orientation(-0.31486744785555604, 0.9036627126256906, -0.012428352976375404, 0.28999608296182483), new Position(-5.218048202503346E-17, 0.0, -0.0982094332575798)), new JointIdTransformationPair(RIGHT_WING_SHOULDER, new Orientation(0.36758445801383055, 0.8583398725778442, -0.3100673472069508, 0.1788646682991726), new Position(0.0666263997554779, 0.0114509342238307, 0.014014306478202343)),
                                                                                                                                        new JointIdTransformationPair(RIGHT_WING_ELBOW, new Orientation(-0.050132424880408345, 0.9817329248561044, -0.10614447171626046, 0.14973495033947373), new Position(-9.769962563761818E-17, 3.552713599391162E-17, -0.06674163043498993)), new JointIdTransformationPair(LEFT_WING_ELBOW, new Orientation(-0.0501282620267501, -0.9817337306280213, 0.10614228419803694, 0.14973261167357155), new Position(-1.0658140798173486E-16, -3.552713599391162E-17, -0.06674181669950485)),
                                                                                                                                        new JointIdTransformationPair(RIGHT_WING_WRIST, new Orientation(-0.3148697843849382, -0.9036623158466526, 0.012429180472900278, 0.2899947469745262), new Position(-2.620126221648339E-16, 1.0658140798173486E-16, -0.09821022301912308)), new JointIdTransformationPair(LEFT_WING_SHOULDER, new Orientation(0.3675849887738264, -0.8583401664786814, 0.31006643303858084, 0.17886375189016573), new Position(-0.06662635505199432, 0.011451060883700848, 0.014014254324138165)));

  @Override
  public JointedModelPose getFoldWingsPose() {
    return BluebirdResource.FOLD_WINGS_POSE;
  }

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId[] NECK_ARRAY = {NECK_0, NECK_1};

  @Override
  public JointId[] getNeckArray() {
    return BluebirdResource.NECK_ARRAY;
  }

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId[] TAIL_ARRAY = {TAIL_0, TAIL_1, TAIL_2};

  @Override
  public JointId[] getTailArray() {
    return BluebirdResource.TAIL_ARRAY;
  }

  private final ImplementationAndVisualType resourceType;

  BluebirdResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  BluebirdResource(ImplementationAndVisualType resourceType) {
    this.resourceType = resourceType;
  }

  public JointId[] getRootJointIds() {
    return FlyerResource.JOINT_ID_ROOTS;
  }

  @Override
  public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
    return this.resourceType.getFactory(this);
  }

  @Override
  public FlyerImp createImplementation(SFlyer abstraction) {
    return new FlyerImp(abstraction, this.resourceType.getFactory(this));
  }
}
