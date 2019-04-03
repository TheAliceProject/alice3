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

package test.ik.croquet;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.lgna.croquet.ImmutableCascadeFillIn;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.story.resources.JointId;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class JointIdFillIn extends ImmutableCascadeFillIn<JointId, Void> {
  private static final Map<JointId, JointIdFillIn> map = Maps.newHashMap();

  public static synchronized JointIdFillIn getInstance(JointId jointId) {
    JointIdFillIn rv = map.get(jointId);
    if (rv != null) {
      //pass
    } else {
      rv = new JointIdFillIn(jointId);
      map.put(jointId, rv);
    }
    return rv;
  }

  private final JointId jointId;

  private JointIdFillIn(JointId jointId) {
    super(UUID.fromString("1250c3c2-3545-442c-9f5a-d4191d5642ee"));
    this.jointId = jointId;
  }

  private static int getDepth(JointId jointId) {
    if (jointId != null) {
      return getDepth(jointId.getParent()) + 1;
    } else {
      return -1;
    }
  }

  @Override
  protected JComponent createMenuItemIconProxy(ItemNode<? super JointId, Void> node) {
    StringBuilder sb = new StringBuilder();
    final int N = getDepth(jointId);
    for (int i = 0; i < N; i++) {
      sb.append("- ");
    }
    sb.append(jointId);
    return new JLabel(sb.toString());
  }

  @Override
  public JointId createValue(ItemNode<? super JointId, Void> node) {
    return this.jointId;
  }

  @Override
  public JointId getTransientValue(ItemNode<? super JointId, Void> node) {
    return this.jointId;
  }
}
