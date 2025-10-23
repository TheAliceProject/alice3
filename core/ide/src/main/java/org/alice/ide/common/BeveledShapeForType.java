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
package org.alice.ide.common;

import edu.cmu.cs.dennisc.java.awt.BeveledShape;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaType;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class BeveledShapeForType extends BeveledShape {
  private static final List<Class<?>> s_roundTypes = new LinkedList<>();

  public static void addRoundType(Class<?> cls) {
    s_roundTypes.add(cls);
  }

  private final float m_yTop;
  private final float m_yBottom;

  public BeveledShapeForType(Shape base, GeneralPath highlightForRaised, GeneralPath neutralForRaised, GeneralPath shadowForRaised, GeneralPath highlightForSunken, GeneralPath neutralForSunken, GeneralPath shadowForSunken, float yTop, float yBottom) {
    super(base, highlightForRaised, neutralForRaised, shadowForRaised, highlightForSunken, neutralForSunken, shadowForSunken);
    m_yTop = yTop;
    m_yBottom = yBottom;
  }

  public BeveledShapeForType(Shape base, GeneralPath highlightForRaised, GeneralPath neutralForRaised, GeneralPath shadowForRaised, float yTop, float yBottom) {
    super(base, highlightForRaised, neutralForRaised, shadowForRaised);
    m_yTop = yTop;
    m_yBottom = yBottom;
  }

  private static Area union(Shape a, Shape b) {
    Area rv;
    if (a != null) {
      rv = new Area(a);
      if (b != null) {
        rv.add(new Area(b));
      }
    } else {
      if (b != null) {
        rv = new Area(b);
      } else {
        rv = null;
      }
    }
    return rv;
  }

  private void addRoundRectShadow(GeneralPath shad, float xA, float yA, float xB, float yB, float x1, float y1) {
    shad.moveTo(xA, y1);
    shad.lineTo(xB, y1);
    shad.quadTo(x1, y1, x1, yB);
    shad.lineTo(x1, yA);
  }

 private void addRoundRectHighlight(GeneralPath high, float x0, float y0, float xA, float yA, float xB, float yB) {
      assert high != null;
      high.moveTo(xB, y0);
      high.lineTo(xA, y0);
      high.quadTo(x0, y0, x0, yA);
      high.lineTo(x0, m_yTop);
      high.moveTo(x0, m_yBottom);
      high.lineTo(x0, yB);
    }

  public void union(RoundRectangle2D.Float roundRect) {
    float xA = roundRect.x + roundRect.arcwidth;
    float xB = (roundRect.x + roundRect.width) - roundRect.arcwidth;
    float x1 = roundRect.x + roundRect.width;
    float yA = roundRect.y + roundRect.archeight;
    float yB = (roundRect.y + roundRect.height) - roundRect.archeight;
    float y1 = roundRect.y + roundRect.height;

    m_base = union(m_base, roundRect);
    addRoundRectShadow(m_shadowForRaised, xA, yA, xB, yB, x1, y1);
    addRoundRectHighlight(m_highlightForRaised, xA, yA, xB, yB, x1, y1);

    if (m_highlightForSunken != m_shadowForRaised) {
      addRoundRectShadow(m_highlightForSunken, xA, yA, xB, yB, x1, y1);
    }
    if (m_shadowForSunken != m_highlightForRaised) {
      addRoundRectHighlight(m_shadowForSunken, xA, yA, xB, yB, x1, y1);
    }
  }

  //todo: make constructor
  public static BeveledShapeForType createBeveledShapeFor(AbstractType<?, ?, ?> type, float x0, float y0, float width, float height) {
    assert type != null;
    float x1 = x0 + width;
    float y1 = y0 + height;
    BeveledShapeForType rv;
    if (type == JavaType.VOID_TYPE) {
      GeneralPath basePath = new GeneralPath();
      GeneralPath shadowPath = new GeneralPath();
      GeneralPath highlightPath = new GeneralPath();
      rv = new BeveledShapeForType(basePath, shadowPath, null, highlightPath, y0, y0);
    } else if (type.isAssignableTo(String.class)) {
      rv = getBeveledShapeForString(x0, y0, width, height, y1, x1);
    } else if (type.isAssignableTo(Number.class) /* || type.isAssignableTo( edu.cmu.cs.dennisc.boundedvalue.Portion.class ) || type.isAssignableTo( edu.cmu.cs.dennisc.math.Angle.class ) */) {
      rv = getBeveledShapeForNumber(x0, y0, height, y1, x1);
    } else if (type.isAssignableTo(Boolean.class) || type.isAssignableTo(Boolean.TYPE)) {
      rv = getBeveledShapeForBoolean(x0, y0, x1, y1);
    } else {
      rv = null;
      for (Class<?> cls : s_roundTypes) {
        if (type.isAssignableTo(cls)) {
          GeneralPath basePath = new GeneralPath();
          basePath.moveTo(x1, y0);
          basePath.curveTo(x0, y0, x0, y1, x1, y1);

          GeneralPath highlighPath = new GeneralPath();
          highlighPath.moveTo(x1, y0);
          highlighPath.curveTo(x0, y0, x0, y1, x1, y1);

          GeneralPath shadowPath = new GeneralPath();

          rv = new BeveledShapeForType(basePath, highlighPath, null, shadowPath, y0, y1);
        }
      }
      if (rv == null) {
        rv = getDefaultBeveledShape(x0, y0, x1, y1);
      }
    }
    return rv;
  }

  private static BeveledShapeForType getBeveledShapeForString(float x0, float y0, float width, float height, float y1, float x1) {
    BeveledShapeForType rv;
    float yDelta = height * 0.2f;
    float yA = y0 + yDelta;
    float yB = y1 - yDelta;

    float xDelta = width * 0.2f;
    float xA = x0 + xDelta;
    float xB = x1 - xDelta;

    GeneralPath basePath = new GeneralPath();
    basePath.moveTo(x1, y0);
    basePath.curveTo(x0, y0, xB, yB, x0, yB);
    basePath.lineTo(x0, y1);
    basePath.curveTo(x1, y1, xA, yA, x1, yA);

    GeneralPath shadowPath = new GeneralPath();
    shadowPath.moveTo(x0, y1);
    shadowPath.curveTo(x1, y1, xA, yA, x1, yA);

    GeneralPath highlightPath = new GeneralPath();
    highlightPath.moveTo(x1, y0);
    highlightPath.curveTo(x0, y0, xB, yB, x0, yB);
    highlightPath.lineTo(x0, y1);

    rv = new BeveledShapeForType(basePath, highlightPath, null, shadowPath, y0, yA);
    return rv;
  }

  private static BeveledShapeForType getBeveledShapeForNumber(float x0, float y0, float height, float y1, float x1) {
    BeveledShapeForType rv;
    //todo: Integer
    //      if( Integer.class.isAssignableFrom( type ) ) {
    //      } else {
    //      }
    float yDelta = height * 0.333f;
    float yA = y0 + yDelta;
    float yB = y1 - yDelta;

    float xA = x0 + ((yB - yA) * 0.5f);

    GeneralPath basePath = new GeneralPath();
    basePath.moveTo(x1, y0);
    basePath.lineTo(x0, y0);
    basePath.lineTo(x0, yA);
    basePath.lineTo(x1, yA);
    basePath.lineTo(x1, yB);
    basePath.lineTo(x0, yB);
    basePath.lineTo(x0, y1);
    basePath.lineTo(x1, y1);

    GeneralPath shadowRaisedPath = new GeneralPath();
    shadowRaisedPath.moveTo(x0, yA);
    shadowRaisedPath.lineTo(x1, yA);

    shadowRaisedPath.moveTo(x0, y1);
    shadowRaisedPath.lineTo(x1, y1);

    GeneralPath neutralRaisedPath = new GeneralPath();
    neutralRaisedPath.moveTo(x1, yA);
    neutralRaisedPath.lineTo(x1, yB);
    neutralRaisedPath.lineTo(xA, yB);

    GeneralPath highlightRaisedPath = new GeneralPath();
    highlightRaisedPath.moveTo(x1, y0);
    highlightRaisedPath.lineTo(x0, y0);
    highlightRaisedPath.lineTo(x0, yA);

    highlightRaisedPath.moveTo(xA, yB);
    highlightRaisedPath.lineTo(x0, yB);
    highlightRaisedPath.lineTo(x0, y1);

    GeneralPath shadowSunkenPath = new GeneralPath();
    shadowSunkenPath.moveTo(x1, y0);
    shadowSunkenPath.lineTo(x0, y0);
    shadowSunkenPath.lineTo(x0, yA);
    shadowSunkenPath.moveTo(x1, yA);
    shadowSunkenPath.lineTo(x1, yB);
    shadowSunkenPath.lineTo(x0, yB);
    shadowSunkenPath.lineTo(x0, y1);

    GeneralPath highlightSunkenPath = new GeneralPath();
    highlightSunkenPath.moveTo(x0, yA);
    highlightSunkenPath.lineTo(x1, yA);
    highlightSunkenPath.moveTo(x0, y1);
    highlightSunkenPath.lineTo(x1, y1);
    rv = new BeveledShapeForType(basePath, highlightRaisedPath, neutralRaisedPath, shadowRaisedPath, highlightSunkenPath, null, shadowSunkenPath, y0, y1);
    return rv;
  }

  private static BeveledShapeForType getBeveledShapeForBoolean(float x0, float y0, float x1, float y1) {
    BeveledShapeForType rv;
    float xA = (x0 + x1) * 0.7f;
    float yA = (y0 + y1) * 0.5f;
    GeneralPath basePath = new GeneralPath();
    basePath.moveTo(x1, y0);
    basePath.lineTo(x0, y0);
    basePath.quadTo(xA, yA, x0, y1);
    basePath.lineTo(x1, y1);

    GeneralPath shadowPath = new GeneralPath();
    shadowPath.moveTo(x0, y1);
    shadowPath.lineTo(x1, y1);

    GeneralPath highlightPath = new GeneralPath();
    highlightPath.moveTo(x1, y0);
    highlightPath.lineTo(x0, y0);
    highlightPath.quadTo(xA, yA, x0, y1);

    rv = new BeveledShapeForType(basePath, highlightPath, null, shadowPath, y0, y1);
    return rv;
  }

  private static BeveledShapeForType getDefaultBeveledShape(float x0, float y0, float x1, float y1) {
    BeveledShapeForType rv;
    //java.awt.Shape base = new java.awt.geom.Rectangle2D.Float( x0, y0, width, height );
    GeneralPath basePath = new GeneralPath();
    basePath.moveTo(x1, y0);
    basePath.lineTo(x0, y0);
    basePath.lineTo(x0, y1);
    basePath.lineTo(x1, y1);

    GeneralPath highlighPath = new GeneralPath();
    highlighPath.moveTo(x1, y0);
    highlighPath.lineTo(x0, y0);
    highlighPath.lineTo(x0, y1);

    GeneralPath shadowPath = new GeneralPath();
    shadowPath.moveTo(x0, y1);
    shadowPath.lineTo(x1, y1);

    rv = new BeveledShapeForType(basePath, highlighPath, null, shadowPath, y0, y1);
    return rv;
  }

  //todo: make constructor
  public static BeveledShapeForType createBeveledShapeFor(AbstractType<?, ?, ?> type, RoundRectangle2D.Float roundRect, float width, float height) {
    float x0 = roundRect.x - width;
    float y0 = roundRect.y + ((roundRect.height - height) * 0.5f);

    BeveledShapeForType beveledShapeForType = createBeveledShapeFor(type, x0, y0, width, height);
    beveledShapeForType.union(roundRect);

    return beveledShapeForType;
  }
}
