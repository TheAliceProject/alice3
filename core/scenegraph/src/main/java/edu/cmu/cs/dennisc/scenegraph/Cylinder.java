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

package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.property.BooleanProperty;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import org.alice.math.immutable.AxisAlignedBox;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Vector3;

/**
 * @author Dennis Cosgrove
 */
public class Cylinder extends Shape {
  public static enum OriginAlignment {
    TOP, CENTER, BOTTOM
  }

  public static enum BottomToTopAxis {
    POSITIVE_X(+1, 0, 0), POSITIVE_Y(0, +1, 0), POSITIVE_Z(0, 0, +1), NEGATIVE_X(-1, 0, 0), NEGATIVE_Y(0, -1, 0), NEGATIVE_Z(0, 0, -1);

    BottomToTopAxis(double x, double y, double z) {
      axis = new Vector3(x, y, z);
    }

    public Vector3 getVector() {
      return axis;
    }

    private final Vector3 axis;
  }

  public double getActualTopRadius() {
    if (Double.isNaN(topRadius.getValue())) {
      return bottomRadius.getValue();
    } else {
      return topRadius.getValue();
    }
  }

  private double getMaxRadius() {
    if (Double.isNaN(topRadius.getValue())) {
      return bottomRadius.getValue();
    } else {
      return Math.max(bottomRadius.getValue(), topRadius.getValue());
    }
  }

  private double getTop() {
    OriginAlignment originAlignment = this.originAlignment.getValue();
    if (originAlignment == OriginAlignment.BOTTOM) {
      return length.getValue();
    } else if (originAlignment == OriginAlignment.CENTER) {
      return length.getValue() * 0.5;
    } else if (originAlignment == OriginAlignment.TOP) {
      return 0;
    } else {
      throw new RuntimeException();
    }
  }

  private double getBottom() {
    OriginAlignment originAlignment = this.originAlignment.getValue();
    if (originAlignment == OriginAlignment.BOTTOM) {
      return 0;
    } else if (originAlignment == OriginAlignment.CENTER) {
      return -length.getValue() * 0.5;
    } else if (originAlignment == OriginAlignment.TOP) {
      return -length.getValue();
    } else {
      throw new RuntimeException();
    }
  }

  private double getCenter() {
    OriginAlignment originAlignment = this.originAlignment.getValue();
    if (originAlignment == OriginAlignment.BOTTOM) {
      return length.getValue() * 0.5;
    } else if (originAlignment == OriginAlignment.CENTER) {
      return 0;
    } else if (originAlignment == OriginAlignment.TOP) {
      return -length.getValue() * 0.5;
    } else {
      throw new RuntimeException();
    }
  }

  public Point3 getCenterOfTop() {
    return getOffsetPoint(getTop());
  }

  public Point3 getCenterOfBottom() {
    return getOffsetPoint(getBottom());
  }

  private Point3 getOffsetPoint(double offset) {
    return switch (bottomToTopAxis.getValue()) {
      case POSITIVE_X -> new Point3(offset, 0, 0);
      case POSITIVE_Y -> new Point3(0, offset, 0);
      case POSITIVE_Z -> new Point3(0, 0, offset);
      case NEGATIVE_X -> new Point3(-offset, 0, 0);
      case NEGATIVE_Y -> new Point3(0, -offset, 0);
      case NEGATIVE_Z -> new Point3(0, 0, -offset);
    };
  }

  @Override
  protected AxisAlignedBox updateBoundingBox() {
    double top = getTop();
    double bottom = getBottom();
    double maxRadius = getMaxRadius();
    BottomToTopAxis bottomToTopAxis = this.bottomToTopAxis.getValue();
    switch (bottomToTopAxis) {
      case POSITIVE_X -> {
        return new AxisAlignedBox(new Point3(bottom, -maxRadius, -maxRadius),
            new Point3(top, +maxRadius, +maxRadius));
      }
      case POSITIVE_Y -> {
        return new AxisAlignedBox(new Point3(-maxRadius, bottom, -maxRadius),
            new Point3(+maxRadius, top, +maxRadius));
      }
      case POSITIVE_Z -> {
        return new AxisAlignedBox(new Point3(-maxRadius, -maxRadius, bottom),
            new Point3(+maxRadius, +maxRadius, top));
      }
      case NEGATIVE_X -> {
        return new AxisAlignedBox(new Point3(top, -maxRadius, -maxRadius),
            new Point3(bottom, +maxRadius, +maxRadius));
      }
      case NEGATIVE_Y -> {
        return new AxisAlignedBox(new Point3(-maxRadius, top, -maxRadius),
            new Point3(+maxRadius, bottom, +maxRadius));
      }
      case NEGATIVE_Z -> {
        return new AxisAlignedBox(new Point3(-maxRadius, -maxRadius, top),
            new Point3(+maxRadius, +maxRadius, bottom));
      }
      default -> throw new RuntimeException();
    }
  }

  public final BoundDoubleProperty length = new BoundDoubleProperty(this, 1.0);
  public final BoundDoubleProperty bottomRadius = new BoundDoubleProperty(this, 1.0);
  public final BoundDoubleProperty topRadius = new BoundDoubleProperty(this, 1.0);
  //todo: change default to CENTER?
  public final InstanceProperty<OriginAlignment> originAlignment = new InstanceProperty<OriginAlignment>(this, OriginAlignment.BOTTOM) {
    @Override
    public void setValue(OriginAlignment value) {
      if (Objects.notEquals(value, this.getValue())) {
        Cylinder.this.markBoundsDirty();
        super.setValue(value);
        Cylinder.this.fireBoundChanged();
      }
    }

  };

  //todo: change default to POSITIVE_Z? NEGATIVE_Z?
  public final InstanceProperty<BottomToTopAxis> bottomToTopAxis = new InstanceProperty<BottomToTopAxis>(this, BottomToTopAxis.POSITIVE_Y) {
    @Override
    public void setValue(BottomToTopAxis value) {
      if (Objects.notEquals(value, this.getValue())) {
        Cylinder.this.markBoundsDirty();
        super.setValue(value);
        Cylinder.this.fireBoundChanged();
      }
    }

  };
  public final BooleanProperty hasBottomCap = new BooleanProperty(this, true);
  public final BooleanProperty hasTopCap = new BooleanProperty(this, true);
}
