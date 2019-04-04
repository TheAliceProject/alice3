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
import org.lgna.story.SJointedModel;
import org.lgna.story.implementation.BasicJointedModelImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.PropResource;

public enum FlamesResource implements PropResource {
  DEFAULT;

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId ROOT = new JointId(null, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.PRIME_TIME) public static final JointId FLAME_BASE = new JointId(ROOT, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_ONE_0 = new JointId(FLAME_BASE, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_ONE_1 = new JointId(FLAME_ONE_0, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_ONE_2 = new JointId(FLAME_ONE_1, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_ONE_3 = new JointId(FLAME_ONE_2, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_ONE_4 = new JointId(FLAME_ONE_3, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_ONE_5 = new JointId(FLAME_ONE_4, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_ONE_6 = new JointId(FLAME_ONE_5, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_TWO_0 = new JointId(FLAME_BASE, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_TWO_1 = new JointId(FLAME_TWO_0, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_TWO_2 = new JointId(FLAME_TWO_1, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_TWO_3 = new JointId(FLAME_TWO_2, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_THREE_0 = new JointId(FLAME_BASE, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_THREE_1 = new JointId(FLAME_THREE_0, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_THREE_2 = new JointId(FLAME_THREE_1, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_THREE_3 = new JointId(FLAME_THREE_2, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_THREE_4 = new JointId(FLAME_THREE_3, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_THREE_5 = new JointId(FLAME_THREE_4, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_FOUR_0 = new JointId(FLAME_THREE_2, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_FOUR_1 = new JointId(FLAME_FOUR_0, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_FOUR_2 = new JointId(FLAME_FOUR_1, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_FOUR_3 = new JointId(FLAME_FOUR_2, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_FOUR_4 = new JointId(FLAME_FOUR_3, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_FOUR_5 = new JointId(FLAME_FOUR_4, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_FIVE_0 = new JointId(FLAME_THREE_2, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_FIVE_1 = new JointId(FLAME_FIVE_0, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_FIVE_2 = new JointId(FLAME_FIVE_1, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_SIX_0 = new JointId(FLAME_BASE, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_SIX_1 = new JointId(FLAME_SIX_0, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_SIX_2 = new JointId(FLAME_SIX_1, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_SIX_3 = new JointId(FLAME_SIX_2, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_SIX_4 = new JointId(FLAME_SIX_3, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_SIX_5 = new JointId(FLAME_SIX_4, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_SEVEN_0 = new JointId(FLAME_BASE, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_SEVEN_1 = new JointId(FLAME_SEVEN_0, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_SEVEN_2 = new JointId(FLAME_SEVEN_1, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_SEVEN_3 = new JointId(FLAME_SEVEN_2, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_SEVEN_4 = new JointId(FLAME_SEVEN_3, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_SEVEN_5 = new JointId(FLAME_SEVEN_4, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_EIGHT_0 = new JointId(FLAME_BASE, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_EIGHT_1 = new JointId(FLAME_EIGHT_0, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_EIGHT_2 = new JointId(FLAME_EIGHT_1, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_EIGHT_3 = new JointId(FLAME_EIGHT_2, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_EIGHT_4 = new JointId(FLAME_EIGHT_3, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_EIGHT_5 = new JointId(FLAME_EIGHT_4, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_EIGHT_6 = new JointId(FLAME_EIGHT_5, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_NINE_0 = new JointId(FLAME_BASE, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_NINE_1 = new JointId(FLAME_NINE_0, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_NINE_2 = new JointId(FLAME_NINE_1, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_NINE_3 = new JointId(FLAME_NINE_2, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_NINE_4 = new JointId(FLAME_NINE_3, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_TEN_0 = new JointId(FLAME_BASE, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_TEN_1 = new JointId(FLAME_TEN_0, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_TEN_2 = new JointId(FLAME_TEN_1, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_TEN_3 = new JointId(FLAME_TEN_2, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_TEN_4 = new JointId(FLAME_TEN_3, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_TEN_5 = new JointId(FLAME_TEN_4, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_ELEVEN_0 = new JointId(FLAME_BASE, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_ELEVEN_1 = new JointId(FLAME_ELEVEN_0, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_ELEVEN_2 = new JointId(FLAME_ELEVEN_1, FlamesResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId FLAME_ELEVEN_3 = new JointId(FLAME_ELEVEN_2, FlamesResource.class);

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId[] JOINT_ID_ROOTS = {ROOT};

  public static final JointId[] FLAME_FOUR_ARRAY = {FLAME_FOUR_0, FLAME_FOUR_1, FLAME_FOUR_2, FLAME_FOUR_3, FLAME_FOUR_4, FLAME_FOUR_5};

  public static final JointId[] FLAME_NINE_ARRAY = {FLAME_NINE_0, FLAME_NINE_1, FLAME_NINE_2, FLAME_NINE_3, FLAME_NINE_4};

  public static final JointId[] FLAME_TEN_ARRAY = {FLAME_TEN_0, FLAME_TEN_1, FLAME_TEN_2, FLAME_TEN_3, FLAME_TEN_4, FLAME_TEN_5};

  public static final JointId[] FLAME_ELEVEN_ARRAY = {FLAME_ELEVEN_0, FLAME_ELEVEN_1, FLAME_ELEVEN_2, FLAME_ELEVEN_3};

  public static final JointId[] FLAME_THREE_ARRAY = {FLAME_THREE_0, FLAME_THREE_1, FLAME_THREE_2, FLAME_THREE_3, FLAME_THREE_4, FLAME_THREE_5};

  public static final JointId[] FLAME_SEVEN_ARRAY = {FLAME_SEVEN_0, FLAME_SEVEN_1, FLAME_SEVEN_2, FLAME_SEVEN_3, FLAME_SEVEN_4, FLAME_SEVEN_5};

  public static final JointId[] FLAME_FIVE_ARRAY = {FLAME_FIVE_0, FLAME_FIVE_1, FLAME_FIVE_2};

  public static final JointId[] FLAME_TWO_ARRAY = {FLAME_TWO_0, FLAME_TWO_1, FLAME_TWO_2, FLAME_TWO_3};

  public static final JointId[] FLAME_ONE_ARRAY = {FLAME_ONE_0, FLAME_ONE_1, FLAME_ONE_2, FLAME_ONE_3, FLAME_ONE_4, FLAME_ONE_5, FLAME_ONE_6};

  public static final JointId[] FLAME_SIX_ARRAY = {FLAME_SIX_0, FLAME_SIX_1, FLAME_SIX_2, FLAME_SIX_3, FLAME_SIX_4, FLAME_SIX_5};

  public static final JointId[] FLAME_EIGHT_ARRAY = {FLAME_EIGHT_0, FLAME_EIGHT_1, FLAME_EIGHT_2, FLAME_EIGHT_3, FLAME_EIGHT_4, FLAME_EIGHT_5, FLAME_EIGHT_6};

  private final ImplementationAndVisualType resourceType;

  FlamesResource() {
    this(ImplementationAndVisualType.ALICE);
  }

  FlamesResource(ImplementationAndVisualType resourceType) {
    this.resourceType = resourceType;
  }

  @Override
  public JointId[] getRootJointIds() {
    return FlamesResource.JOINT_ID_ROOTS;
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
