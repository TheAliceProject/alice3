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
package org.alice.interact.handle;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis;
import edu.cmu.cs.dennisc.scenegraph.scale.Resizer;
import edu.cmu.cs.dennisc.scenegraph.util.Arrow;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.condition.MovementDescription;
import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.AxisAlignedBox;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Vector3;

/**
 * @author David Culyba
 */
public class LinearScaleHandle extends LinearDragHandle {
  public static LinearScaleHandle createFromResizer(Resizer resizer) {
    return switch (resizer) {
    case UNIFORM -> new LinearScaleHandle(new MovementDescription(MovementDirection.RESIZE, MovementType.STOOD_UP), Color4f.PINK, false, resizer);
    case X_AXIS -> new LinearScaleHandle(new MovementDescription(MovementDirection.RIGHT, MovementType.LOCAL), Color4f.MAGENTA, true, resizer);
    case Y_AXIS -> new LinearScaleHandle(new MovementDescription(MovementDirection.UP, MovementType.LOCAL), Color4f.YELLOW, true, resizer);
    case Z_AXIS -> new LinearScaleHandle(new MovementDescription(MovementDirection.FORWARD, MovementType.LOCAL), Color4f.CYAN, true, resizer);
    case XY_PLANE -> new LinearScaleHandle(new MovementDescription(MovementDirection.UP_RIGHT, MovementType.LOCAL), Color4f.PINK, false, resizer);
    case XZ_PLANE -> new LinearScaleHandle(new MovementDescription(MovementDirection.RIGHT_FORWARD, MovementType.LOCAL), Color4f.PINK, false, resizer);
    case YZ_PLANE -> new LinearScaleHandle(new MovementDescription(MovementDirection.UP_FORWARD, MovementType.LOCAL), Color4f.PINK, false, resizer);
    default -> null;
    };
  }

  public LinearScaleHandle(MovementDescription dragDescription, Color4f baseColor, boolean applyAlongAxis, Resizer resizer) {
    super(dragDescription, baseColor);
    this.applyAlongAxis = applyAlongAxis;
    this.resizer = resizer;
    this.initializeAppearance();
  }

  public LinearScaleHandle(LinearScaleHandle handle) {
    super(handle);
    this.applyAlongAxis = handle.applyAlongAxis;
    this.resizer = handle.resizer;
    this.initializeAppearance();
  }

  @Override
  public LinearScaleHandle clone() {
    return new LinearScaleHandle(this);
  }

  public boolean applyAlongAxis() {
    return this.applyAlongAxis;
  }

  @Override
  protected void createShape() {
    createShape(1.0d);
  }

  private void createShape(double scale) {
    this.arrow = new Arrow(.05 * scale, 0.1 * scale, 0.15 * scale, 0.15 * scale, BottomToTopAxis.POSITIVE_Y, this.sgFrontFacingAppearance, true);
    this.arrow.setParent(this);
  }

  public Resizer getResizer() {
    return this.resizer;
  }

  protected Vector3 getUniformResizeOffset() {
    AxisAlignedBox bbox = getManipulatedObjectBox();
    if (bbox != null) {
      Point3 handleOffset = bbox.maximum().withZ(0);
      handleOffset = handleOffset.withX(-1 * handleOffset.x());
      if (!handleOffset.isZero()) {
        return handleOffset.asVector();
      }
    }
    return new Vector3(1, 1, 0);
  }

  protected Vector3 getUniformResizeDirection() {
    return getUniformResizeOffset().normalized();
  }

  @Override
  public void positionRelativeToObject() {
    if (this.dragDescription.direction == MovementDirection.RESIZE) {
      this.dragAxis = this.getUniformResizeDirection();
    }
    AffineMatrix4x4 objectTransformation = this.getTransformationForAxis(this.dragAxis);
    if (objectTransformation.isNaN()) {
      objectTransformation = AffineMatrix4x4.IDENTITY;
    }
    this.setTransformation(objectTransformation, this.getReferenceFrame());
    this.setTranslationOnly(this.dragAxis.times(this.getHandleLength()), this.getReferenceFrame());
  }

  @Override
  protected void setScale(double scale) {
    if (this.arrow != null) {
      this.arrow.setParent(null);
    }
    this.createShape(scale);
  }

  @Override
  public void setVisualsShowing(boolean showing) {
    super.setVisualsShowing(showing);
    this.arrow.setVisualShowing(showing);
  }

  private final Resizer resizer;
  private final boolean applyAlongAxis;
  private Arrow arrow;
}
