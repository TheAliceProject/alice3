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

package org.lgna.story.resources.biped;

import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.SBiped;
import org.lgna.story.implementation.BipedImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

public enum UtahraptorResource implements BipedResource {
  DEFAULT;

  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId JAW_TRIP = new JointId(MOUTH, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_INDEX_FINGER_TIP = new JointId(LEFT_INDEX_FINGER_KNUCKLE, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_MIDDLE_FINGER_TIP = new JointId(LEFT_MIDDLE_FINGER_KNUCKLE, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_PINKY_FINGER_TIP = new JointId(LEFT_PINKY_FINGER_KNUCKLE, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_INDEX_FINGER_TIP = new JointId(RIGHT_INDEX_FINGER_KNUCKLE, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_MIDDLE_FINGER_TIP = new JointId(RIGHT_MIDDLE_FINGER_KNUCKLE, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_PINKY_FINGER_TIP = new JointId(RIGHT_PINKY_FINGER_KNUCKLE, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_TOES = new JointId(LEFT_FOOT, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_TOE_TIP = new JointId(LEFT_TOES, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_CLAW = new JointId(LEFT_FOOT, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME, methodNameHint = "getTail") public static final JointId TAIL_0 = new JointId(PELVIS_LOWER_BODY, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_1 = new JointId(TAIL_0, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_2 = new JointId(TAIL_1, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_3 = new JointId(TAIL_2, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_4 = new JointId(TAIL_3, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_5 = new JointId(TAIL_4, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_TOES = new JointId(RIGHT_FOOT, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_TOE_TIP = new JointId(RIGHT_TOES, UtahraptorResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_CLAW = new JointId(RIGHT_FOOT, UtahraptorResource.class);

  public static final JointId[] TAIL_ARRAY = {TAIL_0, TAIL_1, TAIL_2, TAIL_3, TAIL_4, TAIL_5};

  private final ImplementationAndVisualType resourceType;

  UtahraptorResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  UtahraptorResource(ImplementationAndVisualType resourceType) {
    this.resourceType = resourceType;
  }

  @Override
  public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
    return this.resourceType.getFactory(this);
  }

  @Override
  public BipedImp createImplementation(SBiped abstraction) {
    return new BipedImp(abstraction, this.resourceType.getFactory(this));
  }
}
