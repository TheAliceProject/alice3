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

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Dimension3;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Torus;
import edu.cmu.cs.dennisc.scenegraph.scale.Resizer;
import org.lgna.story.STorus;
import org.lgna.story.implementation.eventhandling.CylinderHull;
import org.lgna.story.implementation.eventhandling.VerticalPrismCollisionHull;

/**
 * @author Dennis Cosgrove
 */
public class TorusImp extends ShapeImp {

  private static final double MINIMUM_VALUE = 0.01;

  public TorusImp(STorus abstraction) {
    this.abstraction = abstraction;
    this.sgTorus.majorRadius.setValue(0.375);
    this.sgTorus.minorRadius.setValue(0.125);
    this.getSgVisuals()[0].geometries.setValue(new Geometry[] {this.sgTorus});
  }

  @Override
  public STorus getAbstraction() {
    return this.abstraction;
  }

  @Override
  protected Geometry getGeometry() {
    return sgTorus;
  }

  @Override
  protected InstanceProperty[] getScaleProperties() {
    return new InstanceProperty[] {this.sgTorus.majorRadius, this.sgTorus.minorRadius};
  }

  @Override
  public Resizer[] getResizers() {
    return new Resizer[] {Resizer.XZ_PLANE, Resizer.Y_AXIS, Resizer.UNIFORM};
  }

  @Override
  public double getValueForResizer(Resizer resizer) {
    switch (resizer) {
      case XZ_PLANE -> {
        return sgTorus.majorRadius.getValue();
      }
      case Y_AXIS -> {
        return sgTorus.minorRadius.getValue();
      }
      case UNIFORM -> {
        return outerRadius.getValue();
      }
      default -> throw new IllegalStateException("Unexpected value: " + resizer);
    }
  }

  @Override
  public void setValueForResizer(Resizer resizer, double value) {
    double newOuter;
    double newInner;
    switch (resizer) {
      case XZ_PLANE -> {
        double change = value - sgTorus.majorRadius.getValue();
        newOuter = outerRadius.getValue() + change;
        newInner = innerRadius.getValue() + change;

      }
      case Y_AXIS -> {
        double change = value - sgTorus.minorRadius.getValue();
        newOuter = outerRadius.getValue() + change;
        newInner = innerRadius.getValue() - change;
      }
      case UNIFORM -> {
        double factor = value / outerRadius.getValue();
        newInner = factor * innerRadius.getValue();
        newOuter = value;
      }
      default -> throw new IllegalStateException("Unexpected value: " + resizer);
    }
    innerRadius.setValue(newInner);
    outerRadius.setValue(newOuter);
  }

  @Override
  public void setSize(Dimension3 size) {
    double newOuter = size.x * .5;
    outerRadius.setValue(newOuter);
    innerRadius.setValue(newOuter - size.y);
  }

  @Override
  public VerticalPrismCollisionHull getCollisionHull() {
    AxisAlignedBox aabb = getDynamicAxisAlignedMinimumBoundingBox(AsSeenBy.SCENE);
    return new CylinderHull(aabb.getCenterOfBottomFace(), aabb.getHeight(), outerRadius.getValue());
  }

  private final STorus abstraction;
  private final Torus sgTorus = new Torus();
  public final DoubleProperty innerRadius = new DoubleProperty(TorusImp.this) {

    @Override
    public Double getValue() {
      return sgTorus.majorRadius.getValue() - sgTorus.minorRadius.getValue();
    }

    @Override
    protected void handleSetValue(Double newInner) {
      double oldOuter = sgTorus.majorRadius.getValue() + sgTorus.minorRadius.getValue();
      updateRadii(newInner, Math.max(oldOuter, newInner + MINIMUM_VALUE));
      if (oldOuter < newInner + MINIMUM_VALUE) {
        // Value changed in updateRadii, so notify listeners
        outerRadius.fireChanged(oldOuter, newInner + MINIMUM_VALUE);
      }
    }
  };
  public final DoubleProperty outerRadius = new DoubleProperty(TorusImp.this) {

    @Override
    public Double getValue() {
      return sgTorus.majorRadius.getValue() + sgTorus.minorRadius.getValue();
    }

    @Override
    protected void handleSetValue(Double newOuter) {
      double oldInner = sgTorus.majorRadius.getValue() - sgTorus.minorRadius.getValue();
      updateRadii(Math.min(oldInner, newOuter - MINIMUM_VALUE), newOuter);
      if (newOuter - MINIMUM_VALUE < oldInner) {
        // Value changed in updateRadii, so notify listeners
        innerRadius.fireChanged(oldInner, newOuter - MINIMUM_VALUE);
      }
    }
  };

  private void updateRadii(double inner, double outer) {
    double minorRadius = (outer - inner) * 0.5;
    sgTorus.minorRadius.setValue(minorRadius);
    sgTorus.majorRadius.setValue(outer - minorRadius);
  }
}
