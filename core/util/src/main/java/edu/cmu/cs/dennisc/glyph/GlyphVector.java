/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package edu.cmu.cs.dennisc.glyph;

/**
 * @author Dennis Cosgrove
 */
public class GlyphVector {
	private String m_text;
	private java.awt.Font m_font;
	private float m_xFactor;
	private float m_yFactor;

	public static final double FLATNESS = 0.01;

	private static java.awt.font.FontRenderContext s_frc = new java.awt.font.FontRenderContext( null, false, true );
	private static java.awt.Stroke s_stroke = new java.awt.BasicStroke( 0 );

	private java.awt.font.GlyphVector m_glyphVector = null;
	private java.awt.Shape m_facesShape = null;
	private java.awt.Shape m_outlinesShape = null;

	private java.util.Vector<java.util.Vector<edu.cmu.cs.dennisc.math.Point2f>> m_faceContours = null;
	//private Object m_faceContoursLock = new Object();
	private java.util.Vector<java.util.Vector<edu.cmu.cs.dennisc.math.Point2f>> m_outlineLines = null;
	//private Object m_outlineLinesLock = new Object();

	private java.awt.geom.Rectangle2D.Float m_bounds;

	public GlyphVector( String text, java.awt.Font font, float xFactor, float yFactor ) {
		m_text = text;
		m_font = font;
		m_xFactor = xFactor;
		m_yFactor = yFactor;
	}

	private void markShapesDirty() {
		m_glyphVector = null;
		m_facesShape = null;
		m_outlinesShape = null;
		m_outlineLines = null;
		m_faceContours = null;

		m_bounds = null;
	}

	private java.awt.font.GlyphVector getGlyphVector() {
		if( m_glyphVector == null ) {
			//m_glyphVector = m_font.layoutGlyphVector( s_frc, m_text.toCharArray(), 0, m_text.length(), java.awt.Font.LAYOUT_LEFT_TO_RIGHT );
			m_glyphVector = m_font.createGlyphVector( s_frc, m_text );
		}
		return m_glyphVector;
	}

	public java.awt.Shape getFacesShape() {
		if( m_facesShape == null ) {
			java.awt.font.GlyphVector glyphVector = getGlyphVector();
			m_facesShape = glyphVector.getOutline();
		}
		return m_facesShape;
	}

	public java.awt.Shape getOutlinesShape() {
		if( m_outlinesShape == null ) {
			java.awt.Shape facesShape = getFacesShape();
			m_outlinesShape = s_stroke.createStrokedShape( facesShape );
		}
		return m_outlinesShape;
	}

	//	private static void reverse( java.util.Vector<edu.cmu.cs.dennisc.math.Point2d> v ) {
	//		int n = v.size();
	//		for( int i=0; i<n/2; i++ ) {
	//			int j = n-i-1;
	//			edu.cmu.cs.dennisc.math.Point2d temp = v.elementAt( i );
	//			v.setElementAt( v.elementAt( j ), i );
	//			v.setElementAt( temp, j );
	//		}
	//	}
	private java.util.Vector<java.util.Vector<edu.cmu.cs.dennisc.math.Point2f>> iterate( java.awt.Shape shape ) {
		java.awt.geom.PathIterator pi = shape.getPathIterator( null, FLATNESS );

		java.util.Vector<java.util.Vector<edu.cmu.cs.dennisc.math.Point2f>> polylines = new java.util.Vector<java.util.Vector<edu.cmu.cs.dennisc.math.Point2f>>();
		java.util.Vector<edu.cmu.cs.dennisc.math.Point2f> polyline = null;

		float[] segment = new float[ 6 ];
		while( !pi.isDone() ) {
			switch( pi.currentSegment( segment ) ) {
			case java.awt.geom.PathIterator.SEG_MOVETO:
				polyline = new java.util.Vector<edu.cmu.cs.dennisc.math.Point2f>();
				//note: no break
			case java.awt.geom.PathIterator.SEG_LINETO:
				assert polyline != null;
				polyline.addElement( new edu.cmu.cs.dennisc.math.Point2f( segment[ 0 ] * m_xFactor, segment[ 1 ] * m_yFactor ) );
				break;
			case java.awt.geom.PathIterator.SEG_CLOSE:
				assert polyline != null;
				//				if( m_isReversed ) {
				//					reverse( polyline );
				//				}
				polylines.addElement( polyline );
				polyline = null;
				break;

			case java.awt.geom.PathIterator.SEG_QUADTO:
				throw new RuntimeException( "SEG_QUADTO: should not occur when shape.getPathIterator is passed a flatness argument" );
			case java.awt.geom.PathIterator.SEG_CUBICTO:
				throw new RuntimeException( "SEG_CUBICTO: should not occur when shape.getPathIterator is passed a flatness argument" );
			default:
				throw new RuntimeException( "unhandled segment: should not occur" );
			}
			pi.next();
		}
		return polylines;
	}

	public java.util.Vector<java.util.Vector<edu.cmu.cs.dennisc.math.Point2f>> acquireFaceContours() {
		//todo
		//		try {
		//			m_faceContoursLock.wait();
		//		} catch( InterruptedException ie ) {
		//			//todo?
		//		}
		if( m_faceContours == null ) {
			m_faceContours = iterate( getFacesShape() );
		}
		return m_faceContours;
	}

	public void releaseFaceContours() {
		//todo
		//		m_faceContoursLock.notify();
	}

	public java.util.Vector<java.util.Vector<edu.cmu.cs.dennisc.math.Point2f>> acquireOutlineLines() {
		//todo
		//		try {
		//			m_outlineLinesLock.wait();
		//		} catch( InterruptedException ie ) {
		//			//todo?
		//		}
		if( m_outlineLines == null ) {
			m_outlineLines = iterate( getOutlinesShape() );
		}
		return m_outlineLines;
	}

	public void releaseOutlineLines() {
		//todo
		//		m_outlineLinesLock.notify();
	}

	public java.awt.geom.Rectangle2D.Float getBounds( java.awt.geom.Rectangle2D.Float rv ) {
		if( m_bounds == null ) {
			//todo: convert to use convex hulls instead of flatness?

			float xMin = Float.MAX_VALUE;
			float yMin = Float.MAX_VALUE;
			float xMax = -Float.MAX_VALUE;
			float yMax = -Float.MAX_VALUE;

			float[] segment = new float[ 6 ];
			java.awt.geom.PathIterator pi = getFacesShape().getPathIterator( null, edu.cmu.cs.dennisc.glyph.GlyphVector.FLATNESS );
			while( !pi.isDone() ) {
				switch( pi.currentSegment( segment ) ) {
				case java.awt.geom.PathIterator.SEG_MOVETO:
				case java.awt.geom.PathIterator.SEG_LINETO:
					float xCurr = segment[ 0 ] * m_xFactor;
					float yCurr = segment[ 1 ] * m_yFactor;
					xMin = Math.min( xMin, xCurr );
					xMax = Math.max( xMax, xCurr );
					yMin = Math.min( yMin, yCurr );
					yMax = Math.max( yMax, yCurr );
					break;
				case java.awt.geom.PathIterator.SEG_CLOSE:
					break;

				case java.awt.geom.PathIterator.SEG_QUADTO:
					throw new RuntimeException( "SEG_QUADTO: should not occur when shape.getPathIterator is passed a flatness argument" );
				case java.awt.geom.PathIterator.SEG_CUBICTO:
					throw new RuntimeException( "SEG_CUBICTO: should not occur when shape.getPathIterator is passed a flatness argument" );
				default:
					throw new RuntimeException( "unhandled segment: should not occur" );
				}
				pi.next();
			}
			if( ( xMin != Float.MAX_VALUE ) && ( yMin != Float.MAX_VALUE ) && ( xMax != -Float.MAX_VALUE ) && ( yMax != -Float.MAX_VALUE ) ) {
				m_bounds = new java.awt.geom.Rectangle2D.Float( xMin, yMin, xMax - xMin, yMax - yMin );
			} else {
				m_bounds = new java.awt.geom.Rectangle2D.Float( Float.NaN, Float.NaN, Float.NaN, Float.NaN );
			}
		}
		rv.setFrame( m_bounds );

		return rv;
	}

	public java.awt.geom.Rectangle2D.Float getBounds() {
		return getBounds( new java.awt.geom.Rectangle2D.Float() );
	}

	public String getText() {
		return m_text;
	}

	public boolean setText( String text ) {
		if( edu.cmu.cs.dennisc.java.util.Objects.notEquals( m_text, text ) ) {
			m_text = text;
			markShapesDirty();
			return true;
		} else {
			return false;
		}
	}

	public java.awt.Font getFont() {
		return m_font;
	}

	public boolean setFont( java.awt.Font font ) {
		if( edu.cmu.cs.dennisc.java.util.Objects.notEquals( m_font, font ) ) {
			m_font = font;
			markShapesDirty();
			return true;
		} else {
			return false;
		}
	}
}
