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

package org.lgna.story.resources.slitherer;

import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.SSlitherer;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.implementation.SlithererImp;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.SlithererResource;

public enum LongNeckSnailResource implements SlithererResource {
  DEFAULT;

  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LOWER_LIP = new JointId(MOUTH, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_EAR = new JointId(HEAD, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId LEFT_EAR_TIP = new JointId(LEFT_EAR, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_EYE_0 = new JointId(HEAD, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_EYE_1 = new JointId(LEFT_EYE_0, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId LEFT_EYE_2 = new JointId(LEFT_EYE_1, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_EAR = new JointId(HEAD, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId RIGHT_EAR_TIP = new JointId(RIGHT_EAR, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_EYE_0 = new JointId(HEAD, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_EYE_1 = new JointId(RIGHT_EYE_0, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId RIGHT_EYE_2 = new JointId(RIGHT_EYE_1, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_1 = new JointId(TAIL_0, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_2 = new JointId(TAIL_1, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_3 = new JointId(TAIL_2, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_4 = new JointId(TAIL_3, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId SHELL_0 = new JointId(TAIL_1, LongNeckSnailResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId SHELL_1 = new JointId(SHELL_0, LongNeckSnailResource.class);

  public static final JointId[] SHELL_ARRAY = {SHELL_0, SHELL_1};

  public static final JointId[] LEFT_EYE_ARRAY = {LEFT_EYE_0, LEFT_EYE_1, LEFT_EYE_2};

  public static final JointId[] RIGHT_EYE_ARRAY = {RIGHT_EYE_0, RIGHT_EYE_1, RIGHT_EYE_2};

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId[] TAIL_ARRAY = {TAIL_0, TAIL_1, TAIL_2, TAIL_3, TAIL_4};

  @Override
  public JointId[] getTailArray() {
    return LongNeckSnailResource.TAIL_ARRAY;
  }

  private final ImplementationAndVisualType resourceType;

  LongNeckSnailResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  LongNeckSnailResource(ImplementationAndVisualType resourceType) {
    this.resourceType = resourceType;
  }

  public JointId[] getRootJointIds() {
    return SlithererResource.JOINT_ID_ROOTS;
  }

  @Override
  public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
    return this.resourceType.getFactory(this);
  }

  @Override
  public SlithererImp createImplementation(SSlitherer abstraction) {
    return new SlithererImp(abstraction, this.resourceType.getFactory(this));
  }
}
