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
package edu.cmu.cs.dennisc.glyph;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.math.Point2f;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.text.Bidi;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class GlyphVector {
  public GlyphVector(String text, Font font, float xFactor, float yFactor) {
    this.text = text;
    this.font = font;
    this.xFactor = xFactor;
    this.yFactor = yFactor;
  }

  private void markShapesDirty() {
    this.glyphVector = null;
    this.facesShape = null;
    this.outlinesShape = null;
    this.outlineLines = null;
    this.faceContours = null;

    this.bounds = null;
  }

  private java.awt.font.GlyphVector getGlyphVector() {
    if (glyphVector == null) {
      // This handles uniform text but will still do unexpected things when mixing LTR and RTL texts
      // TODO Better handle mixed LTR and RTL texts here (and perhaps backport any fix to Alice 2)
      if (Bidi.requiresBidi(text.toCharArray(), 0, text.length())
          && new Bidi(text, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).isRightToLeft()) {
        glyphVector = font.layoutGlyphVector(s_frc, text.toCharArray(), 0, text.length(), Font.LAYOUT_RIGHT_TO_LEFT);
      } else {
        glyphVector = font.createGlyphVector(s_frc, text);
      }
    }
    return glyphVector;
  }


  public Shape getFacesShape() {
    if (this.facesShape == null) {
      java.awt.font.GlyphVector glyphVector = getGlyphVector();
      this.facesShape = glyphVector.getOutline();
    }
    return this.facesShape;
  }

  public Shape getOutlinesShape() {
    if (this.outlinesShape == null) {
      Shape facesShape = getFacesShape();
      this.outlinesShape = s_stroke.createStrokedShape(facesShape);
    }
    return this.outlinesShape;
  }

  //  private static void reverse( java.util.List<edu.cmu.cs.dennisc.math.Point2d> v ) {
  //    int n = v.size();
  //    for( int i=0; i<n/2; i++ ) {
  //      int j = n-i-1;
  //      edu.cmu.cs.dennisc.math.Point2d temp = v.elementAt( i );
  //      v.setElementAt( v.elementAt( j ), i );
  //      v.setElementAt( temp, j );
  //    }
  //  }

  public List<List<Point2f>> acquireFaceContours() {
    //todo
    //    try {
    //      this.faceContoursLock.wait();
    //    } catch( InterruptedException ie ) {
    //      //todo?
    //    }
    if (this.faceContours == null) {
      this.faceContours = iterate(getFacesShape(), this.xFactor, this.yFactor);
    }
    return this.faceContours;
  }

  public void releaseFaceContours() {
    //todo
    //    this.faceContoursLock.notify();
  }

  public List<List<Point2f>> acquireOutlineLines() {
    //todo
    //    try {
    //      this.outlineLinesLock.wait();
    //    } catch( InterruptedException ie ) {
    //      //todo?
    //    }
    if (this.outlineLines == null) {
      this.outlineLines = iterate(getOutlinesShape(), this.xFactor, this.yFactor);
    }
    return this.outlineLines;
  }

  public void releaseOutlineLines() {
    //todo
    //    this.outlineLinesLock.notify();
  }

  public Rectangle2D.Float getBounds(Rectangle2D.Float rv) {
    if (this.bounds == null) {
      //todo: convert to use convex hulls instead of flatness?

      float xMin = Float.MAX_VALUE;
      float yMin = Float.MAX_VALUE;
      float xMax = -Float.MAX_VALUE;
      float yMax = -Float.MAX_VALUE;

      float[] segment = new float[6];
      PathIterator pi = getFacesShape().getPathIterator(null, GlyphVector.FLATNESS);
      while (!pi.isDone()) {
        switch (pi.currentSegment(segment)) {
        case PathIterator.SEG_MOVETO:
        case PathIterator.SEG_LINETO:
          float xCurr = segment[0] * this.xFactor;
          float yCurr = segment[1] * this.yFactor;
          xMin = Math.min(xMin, xCurr);
          xMax = Math.max(xMax, xCurr);
          yMin = Math.min(yMin, yCurr);
          yMax = Math.max(yMax, yCurr);
          break;
        case PathIterator.SEG_CLOSE:
          break;

        case PathIterator.SEG_QUADTO:
          throw new RuntimeException("SEG_QUADTO: should not occur when shape.getPathIterator is passed a flatness argument");
        case PathIterator.SEG_CUBICTO:
          throw new RuntimeException("SEG_CUBICTO: should not occur when shape.getPathIterator is passed a flatness argument");
        default:
          throw new RuntimeException("unhandled segment: should not occur");
        }
        pi.next();
      }
      if ((xMin != Float.MAX_VALUE) && (yMin != Float.MAX_VALUE) && (xMax != -Float.MAX_VALUE) && (yMax != -Float.MAX_VALUE)) {
        this.bounds = new Rectangle2D.Float(xMin, yMin, xMax - xMin, yMax - yMin);
      } else {
        this.bounds = new Rectangle2D.Float(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
      }
    }
    rv.setFrame(this.bounds);

    return rv;
  }

  public Rectangle2D.Float getBounds() {
    return getBounds(new Rectangle2D.Float());
  }

  public String getText() {
    return this.text;
  }

  public boolean setText(String text) {
    if (Objects.notEquals(this.text, text)) {
      this.text = text;
      markShapesDirty();
      return true;
    } else {
      return false;
    }
  }

  public Font getFont() {
    return this.font;
  }

  public boolean setFont(Font font) {
    if (Objects.notEquals(this.font, font)) {
      this.font = font;
      markShapesDirty();
      return true;
    } else {
      return false;
    }
  }

  private static final FontRenderContext s_frc = new FontRenderContext(null, false, true);
  private static final Stroke s_stroke = new BasicStroke(0);

  private String text;
  private Font font;
  private final float xFactor;
  private final float yFactor;

  public static final double FLATNESS = 0.01;

  private java.awt.font.GlyphVector glyphVector = null;
  private Shape facesShape = null;
  private Shape outlinesShape = null;

  private List<List<Point2f>> faceContours = null;
  //private Object faceContoursLock = new Object();
  private List<List<Point2f>> outlineLines = null;
  //private Object outlineLinesLock = new Object();

  private Rectangle2D.Float bounds;

  private static List<List<Point2f>> iterate(Shape shape, float xFactor, float yFactor) {
    PathIterator pi = shape.getPathIterator(null, FLATNESS);

    List<List<Point2f>> polylines = Lists.newLinkedList();
    List<Point2f> polyline = null;

    float[] segment = new float[6];
    while (!pi.isDone()) {
      switch (pi.currentSegment(segment)) {
      case PathIterator.SEG_MOVETO:
        polyline = Lists.newLinkedList();
        //note: no break
      case PathIterator.SEG_LINETO:
        assert polyline != null;
        polyline.add(new Point2f(segment[0] * xFactor, segment[1] * yFactor));
        break;
      case PathIterator.SEG_CLOSE:
        assert polyline != null;
        //        if( this.isReversed ) {
        //          reverse( polyline );
        //        }
        polylines.add(polyline);
        polyline = null;
        break;

      case PathIterator.SEG_QUADTO:
        throw new RuntimeException("SEG_QUADTO: should not occur when shape.getPathIterator is passed a flatness argument");
      case PathIterator.SEG_CUBICTO:
        throw new RuntimeException("SEG_CUBICTO: should not occur when shape.getPathIterator is passed a flatness argument");
      default:
        throw new RuntimeException("unhandled segment: should not occur");
      }
      pi.next();
    }
    return polylines;
  }
}
