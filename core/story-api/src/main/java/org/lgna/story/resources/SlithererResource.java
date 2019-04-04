/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.lgna.story.resources;

import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.ResourceTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.SSlitherer;
import org.lgna.story.implementation.SlithererImp;

@ResourceTemplate(modelClass = SSlitherer.class) public interface SlithererResource extends JointedModelResource {
  public static final JointId ROOT = new JointId(null, SlithererResource.class);
  public static final JointId SPINE_BASE = new JointId(ROOT, SlithererResource.class);
  public static final JointId SPINE_MIDDLE = new JointId(SPINE_BASE, SlithererResource.class);
  public static final JointId SPINE_UPPER = new JointId(SPINE_MIDDLE, SlithererResource.class);
  public static final JointId NECK = new JointId(SPINE_UPPER, SlithererResource.class);
  public static final JointId HEAD = new JointId(NECK, SlithererResource.class);
  public static final JointId MOUTH = new JointId(HEAD, SlithererResource.class);
  //  public static final org.lgna.story.resources.JointId LOWER_LIP = new org.lgna.story.resources.JointId( MOUTH, SlithererResource.class );
  public static final JointId LEFT_EYE = new JointId(HEAD, SlithererResource.class);
  public static final JointId RIGHT_EYE = new JointId(HEAD, SlithererResource.class);
  public static final JointId LEFT_EYELID = new JointId(HEAD, SlithererResource.class);
  public static final JointId RIGHT_EYELID = new JointId(HEAD, SlithererResource.class);

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static final JointId TAIL_0 = new JointId(ROOT, SlithererResource.class);
  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static JointId[] DEFAULT_TAIL = {TAIL_0};

  @FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN) public static JointId[] JOINT_ID_ROOTS = {ROOT};

  public JointId[] getTailArray();

  public SlithererImp createImplementation(SSlitherer abstraction);
}
