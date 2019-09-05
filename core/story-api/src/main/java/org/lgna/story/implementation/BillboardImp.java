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

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.Dimension3;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.QuadArray;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.TexturedVisual;
import edu.cmu.cs.dennisc.scenegraph.Vertex;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.scale.Resizer;
import org.lgna.story.Paint;
import org.lgna.story.SBillboard;

/**
 * @author Dennis Cosgrove
 */
public class BillboardImp extends VisualScaleModelImp {

  private final Vertex[] frontVertices = new Vertex[] {
      Vertex.createXYZIJKUV(+0.5, 1.0, 0.0, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f),
      Vertex.createXYZIJKUV(+0.5, 0.0, 0.0, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f),
      Vertex.createXYZIJKUV(-0.5, 0.0, 0.0, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f),
      Vertex.createXYZIJKUV(-0.5, 1.0, 0.0, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f)};

  private final Vertex[] backVertices = new Vertex[] {
      Vertex.createXYZIJKUV(-0.5, 1.0, 0.0, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f),
      Vertex.createXYZIJKUV(-0.5, 0.0, 0.0, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f),
      Vertex.createXYZIJKUV(+0.5, 0.0, 0.0, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f),
      Vertex.createXYZIJKUV(+0.5, 1.0, 0.0, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f)};

  private class Face extends TexturedVisual {
    Face(Vertex[] vertices) {
      putInstance(this);
      putInstance(this.getAppearance());
      QuadArray sgGeometry = new QuadArray();
      putInstance(sgGeometry);
      sgGeometry.vertices.setValue(vertices);
      geometries.setValue(new Geometry[] {sgGeometry});
    }
  }

  public BillboardImp(SBillboard abstraction) {
    this.abstraction = abstraction;
    for (Visual sgVisual : this.sgVisuals) {
      sgVisual.setParent(this.getSgComposite());
    }
  }

  @Override
  public SBillboard getAbstraction() {
    return this.abstraction;
  }

  @Override
  public void setSize(Dimension3 size) {
    this.setScale(getScaleForSize(size));
  }

  @Override
  public Resizer[] getResizers() {
    return new Resizer[] {Resizer.XY_PLANE, Resizer.X_AXIS, Resizer.Y_AXIS};
  }

  @Override
  public double getValueForResizer(Resizer resizer) {
    if (resizer == Resizer.XY_PLANE) {
      return this.getScale().x;
    } else if (resizer == Resizer.X_AXIS) {
      return this.getScale().x;
    } else if (resizer == Resizer.Y_AXIS) {
      return this.getScale().y;
    } else {
      assert false : resizer;
      return Double.NaN;
    }
  }

  @Override
  public void setValueForResizer(Resizer resizer, double value) {
    if (value > 0.0) {
      double zScale = this.getScale().z;
      if (resizer == Resizer.XY_PLANE) {
        double scaleChange = value / this.getScale().x;
        this.setScale(new Dimension3(value, this.getScale().y * scaleChange, zScale));
      } else if (resizer == Resizer.X_AXIS) {
        this.setScale(new Dimension3(value, this.getScale().y, zScale));
      } else if (resizer == Resizer.Y_AXIS) {
        this.setScale(new Dimension3(this.getScale().x, value, zScale));
      } else {
        assert false : resizer;
      }
    } else {
      Logger.severe(this, value);
    }
  }

  public void setFrontPaint(Paint frontPaint) {
    TexturedPaintUtilities.setPaint(this.sgFrontFace, frontPaint);
  }

  public void setBackPaint(Paint backPaint) {
    TexturedPaintUtilities.setPaint(this.sgBackFace, backPaint);
  }

  @Override
  public Visual[] getSgVisuals() {
    return this.sgVisuals;
  }

  @Override
  protected TexturedAppearance[] getSgPaintAppearances() {
    return this.sgPaintAppearances;
  }

  @Override
  protected SimpleAppearance[] getSgOpacityAppearances() {
    return this.sgOpacityAppearances;
  }

  private final SBillboard abstraction;

  private final Face sgFrontFace = new Face(frontVertices);
  private final Face sgBackFace = new Face(backVertices);
  private final Visual[] sgVisuals = {this.sgFrontFace, this.sgBackFace};
  private final TexturedAppearance[] sgPaintAppearances = {this.sgFrontFace.getAppearance()};
  private final TexturedAppearance[] sgOpacityAppearances = {this.sgFrontFace.getAppearance(), this.sgBackFace.getAppearance()};

  public final PaintProperty backPaint = new PaintProperty(BillboardImp.this) {
    @Override
    protected void internalSetValue(Paint value) {
      TexturedPaintUtilities.setPaint(BillboardImp.this.sgBackFace, value);
    }
  };
}
