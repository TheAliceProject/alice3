/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.stageide.sceneeditor.interact.croquet;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation;
import edu.cmu.cs.dennisc.java.lang.DoubleUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import org.lgna.croquet.Group;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.UserField;
import org.lgna.story.SThing;
import org.lgna.story.implementation.EntityImp;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractSetLocalTransformationActionOperation extends AbstractFieldBasedManipulationActionOperation {

  public AbstractSetLocalTransformationActionOperation(Group group, UUID individualId, boolean isDoRequired, Animator animator, UserField field, String editPresentationKey) {
    super(group, individualId, isDoRequired, animator, field, editPresentationKey);
  }

  protected abstract AbstractTransformable getSGTransformable();

  protected abstract AffineMatrix4x4 getPrevLocalTransformation();

  protected abstract AffineMatrix4x4 getNextLocalTransformation();

  protected void setLocalTransformation(AffineMatrix4x4 lt) {
    AbstractTransformable sgTransformable = getSGTransformable();
    if (this.getAnimator() != null) {
      PointOfViewAnimation povAnimation = new PointOfViewAnimation(sgTransformable, AsSeenBy.PARENT, null, lt);
      povAnimation.setDuration(0.5);
      this.getAnimator().invokeLater(povAnimation, null);
    } else {
      sgTransformable.setLocalTransformation(lt);
    }
  }

  private static final NumberFormat MILLI_FORMAT = new DecimalFormat("0.000");

  private static void appendPosition(StringBuilder sb, AffineMatrix4x4 m) {
    sb.append("(");
    sb.append(DoubleUtilities.format(m.translation.x, MILLI_FORMAT));
    sb.append(",");
    sb.append(DoubleUtilities.format(m.translation.y, MILLI_FORMAT));
    sb.append(",");
    sb.append(DoubleUtilities.format(m.translation.z, MILLI_FORMAT));
    sb.append(")");
  }

  private static void appendOrientation(StringBuilder sb, AffineMatrix4x4 m) {
    UnitQuaternion q = m.orientation.createUnitQuaternion();
    sb.append("(");
    sb.append(DoubleUtilities.format(q.x, MILLI_FORMAT));
    sb.append(",");
    sb.append(DoubleUtilities.format(q.y, MILLI_FORMAT));
    sb.append(",");
    sb.append(DoubleUtilities.format(q.z, MILLI_FORMAT));
    sb.append(",");
    sb.append(DoubleUtilities.format(q.w, MILLI_FORMAT));
    sb.append(")");
  }

  @Override
  protected void perform(UserActivity activity) {
    final AffineMatrix4x4 prevLT = this.getPrevLocalTransformation();
    final AffineMatrix4x4 nextLT = this.getNextLocalTransformation();

    assert prevLT != null;
    assert nextLT != null;
    assert !prevLT.isNaN();
    assert !nextLT.isNaN();
    activity.commitAndInvokeDo(new AbstractEdit(activity) {
      @Override
      protected void doOrRedoInternal(boolean isDo) {
        if (!isDo || isDoRequired()) {
          setLocalTransformation(nextLT);
        }
      }

      @Override
      protected void undoInternal() {
        setLocalTransformation(prevLT);
      }

      @Override
      protected void appendDescription(StringBuilder rv, DescriptionStyle descriptionStyle) {
        String name = getEditPresentationKey();
        rv.append(name);
        if (descriptionStyle.isDetailed()) {
          SThing thing = EntityImp.getAbstractionFromSgElement(AbstractSetLocalTransformationActionOperation.this.getSGTransformable());
          rv.append(" ");
          rv.append(thing);
          if (name.contains("Move")) {
            rv.append(" ");
            appendPosition(rv, prevLT);
            rv.append(" -> ");
            appendPosition(rv, nextLT);
          }
          if (name.contains("Rotate")) {
            rv.append(" ");
            appendOrientation(rv, prevLT);
            rv.append(" -> ");
            appendOrientation(rv, nextLT);
          }
        }
      }
    });
  }
}
