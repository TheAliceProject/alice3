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
package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.animation.Style;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Dimension3;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.Matrix3x3;
import edu.cmu.cs.dennisc.math.ScaleUtilities;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.math.animation.Vector3Animation;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author Dennis Cosgrove
 */
public abstract class VisualScaleModelImp extends ModelImp {
  @Override
  protected InstanceProperty[] getScaleProperties() {
    return new InstanceProperty[] {this.getSgVisuals()[0].scale};
  }

  protected void setSgVisualsScale(Matrix3x3 m) {
    for (Visual sgVisual : this.getSgVisuals()) {
      sgVisual.scale.setValue(new Matrix3x3(m));
    }
  }

  protected Matrix3x3 getSgVisualsScale() {
    return this.getSgVisuals()[0].scale.getValue();
  }

  protected void applyScale(Vector3 axis, boolean isScootDesired) {
    if (isScootDesired) {
      AffineMatrix4x4 m = this.getSgComposite().localTransformation.getValue();
      m.translation.multiply(axis);
      this.getSgComposite().localTransformation.setValue(m);
    }
    for (Visual sgVisual : this.getSgVisuals()) {
      Matrix3x3 scale = sgVisual.scale.getValue();
      ScaleUtilities.applyScale(scale, axis);
      sgVisual.scale.setValue(scale);
    }
  }

  @Override
  public void animateSetScale(Dimension3 scale, double duration, Style style) {
    class ScaleAnimation extends Vector3Animation {
      private final Vector3 vPrev = new Vector3(1, 1, 1);
      private final Vector3 vBuffer = new Vector3();

      private final VisualScaleModelImp subject;
      private final VisualScaleModelImp[] scoots;

      public ScaleAnimation(double duration, Style style, Vector3 axis, VisualScaleModelImp subject, VisualScaleModelImp[] scoots) {
        super(duration, style, new Vector3(1, 1, 1), axis);
        this.subject = subject;
        this.scoots = scoots;
      }

      @Override
      protected void updateValue(Vector3 v) {
        Vector3.setReturnValueToDivision(this.vBuffer, v, this.vPrev);
        this.subject.applyScale(this.vBuffer, false);
        for (VisualScaleModelImp model : this.scoots) {
          model.applyScale(this.vBuffer, true);
        }
        this.vPrev.set(v);
      }
    }

    double actualDuration = adjustDurationIfNecessary(duration);
    VisualScaleModelImp[] scoots = {};
    //    edu.cmu.cs.dennisc.math.Vector3 newScaleVec = new edu.cmu.cs.dennisc.math.Vector3(scale);
    Dimension3 currentScale = this.getScale();
    Vector3 appliedScaleVec = new Vector3(scale.x / currentScale.x, scale.y / currentScale.y, scale.z / currentScale.z);
    if (Double.isNaN(appliedScaleVec.x) || Double.isInfinite(appliedScaleVec.x)) {
      appliedScaleVec.x = 1;
    }
    if (Double.isNaN(appliedScaleVec.y) || Double.isInfinite(appliedScaleVec.y)) {
      appliedScaleVec.y = 1;
    }
    if (Double.isNaN(appliedScaleVec.z) || Double.isInfinite(appliedScaleVec.z)) {
      appliedScaleVec.z = 1;
    }
    if (EpsilonUtilities.isWithinReasonableEpsilon(actualDuration, RIGHT_NOW)) {
      this.applyScale(appliedScaleVec, false);
      for (VisualScaleModelImp model : scoots) {
        model.applyScale(appliedScaleVec, true);
      }
    } else {
      this.perform(new ScaleAnimation(actualDuration, style, appliedScaleVec, this, scoots));
    }
  }

  @Override
  public Dimension3 getScale() {
    Matrix3x3 scale = this.getSgVisualsScale();
    return new Dimension3(scale.right.x, scale.up.y, scale.backward.z);
  }

  @Override
  public void setScale(Dimension3 scale) {
    Matrix3x3 m = Matrix3x3.createIdentity();
    m.right.x = scale.x;
    m.up.y = scale.y;
    m.backward.z = scale.z;
    this.setSgVisualsScale(m);
  }
}
