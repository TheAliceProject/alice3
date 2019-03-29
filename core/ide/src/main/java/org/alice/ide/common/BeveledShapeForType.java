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
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class BeveledShapeForType extends BeveledShape {
	private static List<Class<?>> s_roundTypes = new LinkedList<Class<?>>();

	public static void addRoundType( Class<?> cls ) {
		s_roundTypes.add( cls );
	}

	public static void removeRoundType( Class<?> cls ) {
		s_roundTypes.remove( cls );
	}

	private float m_x;
	private float m_yTop;
	private float m_yBottom;

	public BeveledShapeForType( Shape base, GeneralPath highlightForRaised, GeneralPath neutralForRaised, GeneralPath shadowForRaised, GeneralPath highlightForSunken, GeneralPath neutralForSunken,
			GeneralPath shadowForSunken, float x, float yTop, float yBottom ) {
		super( base, highlightForRaised, neutralForRaised, shadowForRaised, highlightForSunken, neutralForSunken, shadowForSunken );
		m_x = x;
		m_yTop = yTop;
		m_yBottom = yBottom;
	}

	public BeveledShapeForType( Shape base, GeneralPath highlightForRaised, GeneralPath neutralForRaised, GeneralPath shadowForRaised, float x, float yTop, float yBottom ) {
		super( base, highlightForRaised, neutralForRaised, shadowForRaised );
		m_x = x;
		m_yTop = yTop;
		m_yBottom = yBottom;
	}

	private static Area union( Shape a, Shape b ) {
		Area rv;
		if( a != null ) {
			rv = new Area( a );
			if( b != null ) {
				rv.add( new Area( b ) );
			}
		} else {
			if( b != null ) {
				rv = new Area( b );
			} else {
				rv = null;
			}
		}
		return rv;
	}

	private void addRectShadow( GeneralPath shadowPath, float x0, float y0, float x1, float y1 ) {
		assert shadowPath != null;
		shadowPath.moveTo( x0, y1 );
		shadowPath.lineTo( x1, y1 );
		shadowPath.lineTo( x1, y0 );
	}

	private void addRectHighlight( GeneralPath highlightPath, float x0, float y0, float x1, float y1 ) {
		assert highlightPath != null;
		highlightPath.moveTo( x1, y0 );
		highlightPath.lineTo( x0, y0 );
		highlightPath.lineTo( x0, m_yTop );
		highlightPath.moveTo( x0, m_yBottom );
		highlightPath.lineTo( x0, y1 );
	}

	//	float x;
	//	float y;
	//	float a;
	//	float b;
	//	x=xB; y=y0;	            base.moveTo(x,y);     high.moveTo(x,y);
	//	x=xA; y=y0;	            base.lineTo(x,y);     high.lineTo(x,y);
	//	x=x0; y=yA;	a=x0; b=y0; base.quadTo(a,b,x,y); high.quadTo(a,b,x,y);
	//	x=x0; y=yB;	            base.lineTo(x,y);     high.lineTo(x,y);     neut.moveTo(x,y);
	//	x=xA; y=y1;	a=x0; b=y1; base.quadTo(a,b,x,y);                       neut.quadTo(a,b,x,y); shad.moveTo(x,y);
	//	x=xB; y=y1;	            base.lineTo(x,y);                                                 shad.lineTo(x,y);
	//	x=x1; y=yB;	a=x1; b=y1; base.quadTo(a,b,x,y);                                             shad.quadTo(a,b,x,y);
	//	x=x1; y=yA;	            base.lineTo(x,y);                           neut.moveTo(x,y);     shad.lineTo(x,y);
	//	x=xB; y=y0;	a=x1; b=y0; base.quadTo(a,b,x,y);                       neut.quadTo(a,b,x,y);

	public void union( Rectangle2D.Float rect ) {
		float x0 = rect.x;
		float x1 = rect.x + rect.width;
		float y0 = rect.y;
		float y1 = rect.y + rect.height;

		m_base = union( m_base, rect );
		addRectShadow( m_shadowForRaised, x0, y0, x1, y1 );
		addRectHighlight( m_highlightForRaised, x0, y0, x1, y1 );

		if( m_highlightForSunken != m_shadowForRaised ) {
			addRectShadow( m_highlightForSunken, x0, y0, x1, y1 );
		}
		if( m_shadowForSunken != m_highlightForRaised ) {
			addRectHighlight( m_shadowForSunken, x0, y0, x1, y1 );
		}
	}

	private void addRoundRectShadow( GeneralPath shad, float x0, float y0, float xA, float yA, float xB, float yB, float x1, float y1 ) {
		float x;
		float y;
		float a;
		float b;
		x = xA;
		y = y1;
		a = x0;
		b = y1;
		shad.moveTo( x, y );
		x = xB;
		y = y1;
		shad.lineTo( x, y );
		x = x1;
		y = yB;
		a = x1;
		b = y1;
		shad.quadTo( a, b, x, y );
		x = x1;
		y = yA;
		shad.lineTo( x, y );
	}

	private void addRoundRectHighlight( GeneralPath high, float x0, float y0, float xA, float yA, float xB, float yB, float x1, float y1 ) {
		assert high != null;
		float x;
		float y;
		float a;
		float b;
		x = xB;
		y = y0;
		high.moveTo( x, y );
		x = xA;
		y = y0;
		high.lineTo( x, y );
		x = x0;
		y = yA;
		a = x0;
		b = y0;
		high.quadTo( a, b, x, y );
		high.lineTo( x0, m_yTop );
		high.moveTo( x0, m_yBottom );
		x = x0;
		y = yB;
		high.lineTo( x, y );
	}

	public void union( RoundRectangle2D.Float roundRect ) {
		float x0 = roundRect.x;
		float xA = roundRect.x + roundRect.arcwidth;
		float xB = ( roundRect.x + roundRect.width ) - roundRect.arcwidth;
		float x1 = roundRect.x + roundRect.width;
		float y0 = roundRect.y;
		float yA = roundRect.y + roundRect.archeight;
		float yB = ( roundRect.y + roundRect.height ) - roundRect.archeight;
		float y1 = roundRect.y + roundRect.height;

		m_base = union( m_base, roundRect );
		addRoundRectShadow( m_shadowForRaised, x0, y0, xA, yA, xB, yB, x1, y1 );
		addRoundRectHighlight( m_highlightForRaised, x0, y0, xA, yA, xB, yB, x1, y1 );

		if( m_highlightForSunken != m_shadowForRaised ) {
			addRoundRectShadow( m_highlightForSunken, x0, y0, xA, yA, xB, yB, x1, y1 );
		}
		if( m_shadowForSunken != m_highlightForRaised ) {
			addRoundRectHighlight( m_shadowForSunken, x0, y0, xA, yA, xB, yB, x1, y1 );
		}
	}

	//todo: make constructor
	public static BeveledShapeForType createBeveledShapeFor( AbstractType<?, ?, ?> type, float x0, float y0, float width, float height ) {
		assert type != null;
		float x1 = x0 + width;
		float y1 = y0 + height;
		BeveledShapeForType rv;
		if( type == JavaType.VOID_TYPE ) {
			GeneralPath basePath = new GeneralPath();
			GeneralPath shadowPath = new GeneralPath();
			GeneralPath highlightPath = new GeneralPath();
			rv = new BeveledShapeForType( basePath, shadowPath, null, highlightPath, x1, y0, y0 );
		} else if( type.isAssignableTo( String.class ) ) {

			float yDelta = height * 0.2f;
			float yA = y0 + yDelta;
			float yB = y1 - yDelta;

			float xDelta = width * 0.2f;
			float xA = x0 + xDelta;
			float xB = x1 - xDelta;

			GeneralPath basePath = new GeneralPath();
			basePath.moveTo( x1, y0 );
			basePath.curveTo( x0, y0, xB, yB, x0, yB );
			basePath.lineTo( x0, y1 );
			basePath.curveTo( x1, y1, xA, yA, x1, yA );

			GeneralPath shadowPath = new GeneralPath();
			shadowPath.moveTo( x0, y1 );
			shadowPath.curveTo( x1, y1, xA, yA, x1, yA );

			GeneralPath highlightPath = new GeneralPath();
			highlightPath.moveTo( x1, y0 );
			highlightPath.curveTo( x0, y0, xB, yB, x0, yB );
			highlightPath.lineTo( x0, y1 );

			rv = new BeveledShapeForType( basePath, highlightPath, null, shadowPath, x1, y0, yA );
			//		} else if( type.isAssignableTo( Class.class ) ) {
			//			java.awt.geom.GeneralPath basePath = new java.awt.geom.GeneralPath();
			//			basePath.moveTo( x0, y0 );
			//			basePath.lineTo( x0, y1 );
			//			basePath.lineTo( x1, y1 );
			//			basePath.lineTo( x1, y0 );
			//			java.awt.geom.GeneralPath shadowPath = new java.awt.geom.GeneralPath();
			//			shadowPath.moveTo( x0, y1 );
			//			shadowPath.lineTo( x1, y1 );
			//			shadowPath.lineTo( x1, y0 );
			//
			//			java.awt.geom.GeneralPath highlightPath = new java.awt.geom.GeneralPath();
			//			highlightPath.moveTo( x0, y1 );
			//			highlightPath.lineTo( x0, y0 );
			//			highlightPath.lineTo( x1, y0 );
			//			rv = new BeveledShapeForType( basePath, shadowPath, null, highlightPath, 0, 0, 0 );
			//todo
		} else if( type.isAssignableTo( Number.class ) /* || type.isAssignableTo( edu.cmu.cs.dennisc.boundedvalue.Portion.class ) || type.isAssignableTo( edu.cmu.cs.dennisc.math.Angle.class ) */) {
			//todo: Integer
			//			if( Integer.class.isAssignableFrom( type ) ) {
			//			} else {
			//			}
			float yDelta = height * 0.333f;
			float yA = y0 + yDelta;
			float yB = y1 - yDelta;

			float xA = x0 + ( ( yB - yA ) * 0.5f );

			GeneralPath basePath = new GeneralPath();
			basePath.moveTo( x1, y0 );
			basePath.lineTo( x0, y0 );
			basePath.lineTo( x0, yA );
			basePath.lineTo( x1, yA );
			basePath.lineTo( x1, yB );
			basePath.lineTo( x0, yB );
			basePath.lineTo( x0, y1 );
			basePath.lineTo( x1, y1 );

			//			java.awt.geom.GeneralPath shadowRaisedPath = new java.awt.geom.GeneralPath();
			//			shadowRaisedPath.moveTo( x0, yA );
			//			shadowRaisedPath.lineTo( x1, yA );
			//			shadowRaisedPath.lineTo( x1, yB );
			//			shadowRaisedPath.lineTo( xA, yB );
			//
			//			shadowRaisedPath.moveTo( x0, y1 );
			//			shadowRaisedPath.lineTo( x1, y1 );
			//
			//			java.awt.geom.GeneralPath highlightRaisedPath = new java.awt.geom.GeneralPath();
			//			highlightRaisedPath.moveTo( x1, y0 );
			//			highlightRaisedPath.lineTo( x0, y0 );
			//			highlightRaisedPath.lineTo( x0, yA );
			//
			//			highlightRaisedPath.moveTo( xA, yB );
			//			highlightRaisedPath.lineTo( x0, yB );
			//			highlightRaisedPath.lineTo( x0, y1 );

			GeneralPath shadowRaisedPath = new GeneralPath();
			shadowRaisedPath.moveTo( x0, yA );
			shadowRaisedPath.lineTo( x1, yA );

			shadowRaisedPath.moveTo( x0, y1 );
			shadowRaisedPath.lineTo( x1, y1 );

			GeneralPath neutralRaisedPath = new GeneralPath();
			neutralRaisedPath.moveTo( x1, yA );
			neutralRaisedPath.lineTo( x1, yB );
			neutralRaisedPath.lineTo( xA, yB );

			GeneralPath highlightRaisedPath = new GeneralPath();
			highlightRaisedPath.moveTo( x1, y0 );
			highlightRaisedPath.lineTo( x0, y0 );
			highlightRaisedPath.lineTo( x0, yA );

			highlightRaisedPath.moveTo( xA, yB );
			highlightRaisedPath.lineTo( x0, yB );
			highlightRaisedPath.lineTo( x0, y1 );

			GeneralPath shadowSunkenPath = new GeneralPath();
			shadowSunkenPath.moveTo( x1, y0 );
			shadowSunkenPath.lineTo( x0, y0 );
			shadowSunkenPath.lineTo( x0, yA );
			shadowSunkenPath.moveTo( x1, yA );
			shadowSunkenPath.lineTo( x1, yB );
			shadowSunkenPath.lineTo( x0, yB );
			shadowSunkenPath.lineTo( x0, y1 );

			GeneralPath highlightSunkenPath = new GeneralPath();
			highlightSunkenPath.moveTo( x0, yA );
			highlightSunkenPath.lineTo( x1, yA );
			highlightSunkenPath.moveTo( x0, y1 );
			highlightSunkenPath.lineTo( x1, y1 );
			rv = new BeveledShapeForType( basePath, highlightRaisedPath, neutralRaisedPath, shadowRaisedPath, highlightSunkenPath, null, shadowSunkenPath, x1, y0, y1 );
		} else if( type.isAssignableTo( Boolean.class ) || type.isAssignableTo( Boolean.TYPE ) ) {
			float xA = ( x0 + x1 ) * 0.7f;
			float yA = ( y0 + y1 ) * 0.5f;
			GeneralPath basePath = new GeneralPath();
			basePath.moveTo( x1, y0 );
			basePath.lineTo( x0, y0 );
			basePath.quadTo( xA, yA, x0, y1 );
			basePath.lineTo( x1, y1 );

			GeneralPath shadowPath = new GeneralPath();
			shadowPath.moveTo( x0, y1 );
			shadowPath.lineTo( x1, y1 );

			GeneralPath highlightPath = new GeneralPath();
			highlightPath.moveTo( x1, y0 );
			highlightPath.lineTo( x0, y0 );
			highlightPath.quadTo( xA, yA, x0, y1 );

			rv = new BeveledShapeForType( basePath, highlightPath, null, shadowPath, x1, y0, y1 );
		} else {
			rv = null;
			for( Class<?> cls : s_roundTypes ) {
				if( type.isAssignableTo( cls ) ) {
					//					java.awt.Shape base = new java.awt.geom.CubicCurve2D.Float( x1, y0, x0, y0, x0, y1, x1, y1 );

					GeneralPath basePath = new GeneralPath();
					basePath.moveTo( x1, y0 );
					basePath.curveTo( x0, y0, x0, y1, x1, y1 );

					GeneralPath highlighPath = new GeneralPath();
					highlighPath.moveTo( x1, y0 );
					highlighPath.curveTo( x0, y0, x0, y1, x1, y1 );

					GeneralPath shadowPath = new GeneralPath();

					rv = new BeveledShapeForType( basePath, highlighPath, null, shadowPath, x1, y0, y1 );
				}
			}
			if( rv != null ) {
				//pass
			} else {
				//java.awt.Shape base = new java.awt.geom.Rectangle2D.Float( x0, y0, width, height );
				GeneralPath basePath = new GeneralPath();
				basePath.moveTo( x1, y0 );
				basePath.lineTo( x0, y0 );
				basePath.lineTo( x0, y1 );
				basePath.lineTo( x1, y1 );

				GeneralPath highlighPath = new GeneralPath();
				highlighPath.moveTo( x1, y0 );
				highlighPath.lineTo( x0, y0 );
				highlighPath.lineTo( x0, y1 );

				GeneralPath shadowPath = new GeneralPath();
				shadowPath.moveTo( x0, y1 );
				shadowPath.lineTo( x1, y1 );

				rv = new BeveledShapeForType( basePath, highlighPath, null, shadowPath, x1, y0, y1 );
			}
		}
		return rv;
	}

	//todo: make constructor
	public static BeveledShapeForType createBeveledShapeFor( AbstractType<?, ?, ?> type, Rectangle2D.Float rect, float width, float height ) {
		float x0 = rect.x - width;
		float y0 = rect.y + ( ( rect.height - height ) * 0.5f );

		BeveledShapeForType beveledShapeForType = createBeveledShapeFor( type, x0, y0, width, height );
		beveledShapeForType.union( rect );

		return beveledShapeForType;
	}

	//todo: make constructor
	public static BeveledShapeForType createBeveledShapeFor( AbstractType<?, ?, ?> type, RoundRectangle2D.Float roundRect, float width, float height ) {
		float x0 = roundRect.x - width;
		float y0 = roundRect.y + ( ( roundRect.height - height ) * 0.5f );

		BeveledShapeForType beveledShapeForType = createBeveledShapeFor( type, x0, y0, width, height );
		beveledShapeForType.union( roundRect );

		return beveledShapeForType;
	}
}
