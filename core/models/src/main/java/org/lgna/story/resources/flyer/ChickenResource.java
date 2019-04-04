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

public enum ChickenResource implements FlyerResource {
  MEAN_CHICKEN, DEFAULT;

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_TOE = new JointId(LEFT_FOOT, ChickenResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_TOE = new JointId(RIGHT_FOOT, ChickenResource.class);

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointedModelPose SPREAD_WINGS_POSE = new JointedModelPose(new JointIdTransformationPair(LEFT_WING_WRIST, new Orientation(-0.09003842593834224, 0.24769788501735335, 0.07614139380042827, 0.9616347163879868), new Position(-1.776356799695581E-17, 3.552713599391162E-17, -0.06171688809990883)), new JointIdTransformationPair(RIGHT_WING_ELBOW, new Orientation(0.2977194949254283, -0.10044202066550513, -0.056085701157270296, 0.9476966270656847), new Position(8.881783998477905E-18, 3.552713599391162E-17, -0.06717544794082642)),
                                                                                                                                          new JointIdTransformationPair(LEFT_WING_ELBOW, new Orientation(0.29771608500207875, 0.10044494243899584, 0.05608648065369891, 0.9476973424872226), new Position(-8.881783998477905E-18, -7.105427198782324E-17, -0.06717535108327866)), new JointIdTransformationPair(RIGHT_WING_WRIST, new Orientation(-0.09003162379296056, -0.24770267350444955, -0.0761417854230108, 0.9616340888136958), new Position(3.3228530604140216E-13, -7.105427198782324E-17, -0.06171758845448494)),
                                                                                                                                          new JointIdTransformationPair(LEFT_WING_SHOULDER, new Orientation(-0.5516930755138812, 0.7509373445525394, -0.18630302169306795, 0.3114787939723155), new Position(-0.05509381368756294, 0.012012673541903496, -0.01334095187485218)));

  @Override
  public JointedModelPose getSpreadWingsPose() {
    return ChickenResource.SPREAD_WINGS_POSE;
  }

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointedModelPose FOLD_WINGS_POSE = new JointedModelPose(new JointIdTransformationPair(LEFT_WING_WRIST, new Orientation(-0.09879622220942053, 0.36518907801110745, 0.06437184480753615, 0.923435167932551), new Position(-1.776356799695581E-17, 3.552713599391162E-17, -0.06171688809990883)), new JointIdTransformationPair(RIGHT_WING_ELBOW, new Orientation(0.2589987664218201, -0.3928822991381986, -0.1505900828719614, 0.8694169108986058), new Position(8.881783998477905E-18, 3.552713599391162E-17, -0.06717544794082642)),
                                                                                                                                        new JointIdTransformationPair(LEFT_WING_ELBOW, new Orientation(0.2817625605291381, 0.27764684608113316, 0.11131226997297258, 0.9116642292525946), new Position(-8.881783998477905E-18, -7.105427198782324E-17, -0.06717535108327866)), new JointIdTransformationPair(RIGHT_WING_WRIST, new Orientation(-0.09491708333549209, -0.3113904510018253, -0.0699572090904218, 0.9429383453928694), new Position(3.3228530604140216E-13, -7.105427198782324E-17, -0.06171758845448494)),
                                                                                                                                        new JointIdTransformationPair(LEFT_WING_SHOULDER, new Orientation(-0.5026503410028454, 0.8144717938501003, -0.12658502718684977, 0.26068095942056707), new Position(-0.05509381368756294, 0.012012673541903496, -0.01334095187485218)));

  @Override
  public JointedModelPose getFoldWingsPose() {
    return ChickenResource.FOLD_WINGS_POSE;
  }

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId[] NECK_ARRAY = {NECK_0, NECK_1};

  @Override
  public JointId[] getNeckArray() {
    return ChickenResource.NECK_ARRAY;
  }

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId[] TAIL_ARRAY = {TAIL_0, TAIL_1, TAIL_2};

  @Override
  public JointId[] getTailArray() {
    return ChickenResource.TAIL_ARRAY;
  }

  private final ImplementationAndVisualType resourceType;

  ChickenResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  ChickenResource(ImplementationAndVisualType resourceType) {
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
