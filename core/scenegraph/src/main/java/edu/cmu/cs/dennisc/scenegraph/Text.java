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

import edu.cmu.cs.dennisc.glyph.GlyphVector;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.StringProperty;
import org.alice.math.immutable.*;

import java.awt.Font;
import java.awt.geom.Rectangle2D;

/**
 * @author Dennis Cosgrove
 */
public class Text extends Geometry {
  private static final String DEFAULT_TEXT = "";
  private static final Font DEFAULT_FONT = new Font(null, Font.PLAIN, 12);

  public GlyphVector getGlyphVector() {
    return this.glyphVector;
  }

  protected void updateUnalignedBoundingBoxIfNecessary() {
    if (this.unalignedBoundingBox.isNaN()) {
      Rectangle2D.Float bounds = this.glyphVector.getBounds();
      this.unalignedBoundingBox = new AxisAlignedBox(
          new Point3(bounds.x, bounds.y, 0),
          new Point3(bounds.x + bounds.width, bounds.y + bounds.height, depth.getValue()));
    }

    if (this.unalignedBoundingBox.isNaN()) {
      Logger.todo(this);
      this.unalignedBoundingBox = AxisAlignedBox.Empty;
    }
  }

  //todo: cache result?
  public Vector3 getAlignmentOffset() {
    updateUnalignedBoundingBoxIfNecessary();
    return new Vector3(getXOffset(), getYOffset(), getZOffset());
  }

  private double getXOffset() {
    return switch (this.leftToRightAlignment.getValue()) {
      case ALIGN_CENTER_OF_LEFT_AND_RIGHT ->
          -(this.unalignedBoundingBox.getXMinimum() + (this.unalignedBoundingBox.getWidth() / 2));
      case ALIGN_LEFT -> -this.unalignedBoundingBox.getXMinimum();
      case ALIGN_RIGHT -> -this.unalignedBoundingBox.getXMaximum();
    };
  }

  private double getYOffset() {
    return switch (this.topToBottomAlignment.getValue()) {
      case ALIGN_CENTER_OF_TOP_AND_BOTTOM ->
          -(this.unalignedBoundingBox.getYMinimum() + (this.unalignedBoundingBox.getHeight() / 2));
      case ALIGN_TOP -> -this.unalignedBoundingBox.getYMaximum();
      case ALIGN_BOTTOM -> -this.unalignedBoundingBox.getYMinimum();
      case ALIGN_BASELINE -> 0;
      case ALIGN_CENTER_OF_TOP_AND_BASELINE -> -this.unalignedBoundingBox.getYMaximum() / 2;
    };
  }

  private double getZOffset() {
    return switch (this.frontToBackAlignment.getValue()) {
      case ALIGN_CENTER_OF_FRONT_AND_BACK -> -depth.getValue() / 2;
      case ALIGN_FRONT -> 0;
      case ALIGN_BACK -> -depth.getValue();
    };
  }

  @Override
  protected AxisAlignedBox updateBoundingBox() {
    updateUnalignedBoundingBoxIfNecessary();
    return unalignedBoundingBox.translate(getAlignmentOffset());
  }

  @Override
  public AffineMatrix4x4 getPlane() {
    throw new RuntimeException("TODO");
  }

  @Override
  public void transform(Matrix4x4 trans) {
    throw new RuntimeException("TODO");
  }

  public final StringProperty text = new StringProperty(this, DEFAULT_TEXT) {
    @Override
    public void setValue(String value) {
      markBoundsDirty();
      super.setValue(value);
      glyphVector.setText(value);
      unalignedBoundingBox = AxisAlignedBox.NaN;
      fireBoundChanged();
    }
  };
  public final InstanceProperty<Font> font = new InstanceProperty<Font>(this, DEFAULT_FONT) {
    @Override
    public void setValue(Font value) {
      markBoundsDirty();
      super.setValue(value);
      glyphVector.setFont(value);
      unalignedBoundingBox = AxisAlignedBox.NaN;
      fireBoundChanged();
    }
  };

  public final BoundDoubleProperty depth = new BoundDoubleProperty(this, 0.25) {
    @Override
    public void setValue(Double value) {
      super.setValue(value);
      unalignedBoundingBox = AxisAlignedBox.NaN;
    }
  };

  public final InstanceProperty<LeftToRightAlignment> leftToRightAlignment = new InstanceProperty<LeftToRightAlignment>(this, LeftToRightAlignment.ALIGN_CENTER_OF_LEFT_AND_RIGHT) {
    @Override
    public void setValue(LeftToRightAlignment value) {
      if (Objects.notEquals(value, this.getValue())) {
        markBoundsDirty();
        super.setValue(value);
        unalignedBoundingBox = AxisAlignedBox.NaN;
        fireBoundChanged();
      }
    }
  };
  public final InstanceProperty<TopToBottomAlignment> topToBottomAlignment = new InstanceProperty<TopToBottomAlignment>(this, TopToBottomAlignment.ALIGN_BASELINE) {
    @Override
    public void setValue(TopToBottomAlignment value) {
      if (Objects.notEquals(value, this.getValue())) {
        markBoundsDirty();
        super.setValue(value);
        unalignedBoundingBox = AxisAlignedBox.NaN;
        fireBoundChanged();
      }
    }
  };
  public final InstanceProperty<FrontToBackAlignment> frontToBackAlignment = new InstanceProperty<FrontToBackAlignment>(this, FrontToBackAlignment.ALIGN_CENTER_OF_FRONT_AND_BACK) {
    @Override
    public void setValue(FrontToBackAlignment value) {
      if (Objects.notEquals(value, this.getValue())) {
        markBoundsDirty();
        super.setValue(value);
        unalignedBoundingBox = AxisAlignedBox.NaN;
        fireBoundChanged();
      }
    }
  };

  private AxisAlignedBox unalignedBoundingBox = AxisAlignedBox.NaN;

  private final GlyphVector glyphVector = new GlyphVector(DEFAULT_TEXT, DEFAULT_FONT, -1, -1);
}
