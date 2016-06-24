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

/**
 * @author Dennis Cosgrove
 */
public class GlyphVector {
	public GlyphVector( String text, java.awt.Font font, float xFactor, float yFactor ) {
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
		if( this.glyphVector == null ) {
			//this.glyphVector = this.font.layoutGlyphVector( s_frc, this.text.toCharArray(), 0, this.text.length(), java.awt.Font.LAYOUT_LEFT_TO_RIGHT );
			this.glyphVector = this.font.createGlyphVector( s_frc, this.text );
		}
		return this.glyphVector;
	}

	public java.awt.Shape getFacesShape() {
		if( this.facesShape == null ) {
			java.awt.font.GlyphVector glyphVector = getGlyphVector();
			this.facesShape = glyphVector.getOutline();
		}
		return this.facesShape;
	}

	public java.awt.Shape getOutlinesShape() {
		if( this.outlinesShape == null ) {
			java.awt.Shape facesShape = getFacesShape();
			this.outlinesShape = s_stroke.createStrokedShape( facesShape );
		}
		return this.outlinesShape;
	}

	//	private static void reverse( java.util.List<edu.cmu.cs.dennisc.math.Point2d> v ) {
	//		int n = v.size();
	//		for( int i=0; i<n/2; i++ ) {
	//			int j = n-i-1;
	//			edu.cmu.cs.dennisc.math.Point2d temp = v.elementAt( i );
	//			v.setElementAt( v.elementAt( j ), i );
	//			v.setElementAt( temp, j );
	//		}
	//	}

	public java.util.List<java.util.List<edu.cmu.cs.dennisc.math.Point2f>> acquireFaceContours() {
		//todo
		//		try {
		//			this.faceContoursLock.wait();
		//		} catch( InterruptedException ie ) {
		//			//todo?
		//		}
		if( this.faceContours == null ) {
			this.faceContours = iterate( getFacesShape(), this.xFactor, this.yFactor );
		}
		return this.faceContours;
	}

	public void releaseFaceContours() {
		//todo
		//		this.faceContoursLock.notify();
	}

	public java.util.List<java.util.List<edu.cmu.cs.dennisc.math.Point2f>> acquireOutlineLines() {
		//todo
		//		try {
		//			this.outlineLinesLock.wait();
		//		} catch( InterruptedException ie ) {
		//			//todo?
		//		}
		if( this.outlineLines == null ) {
			this.outlineLines = iterate( getOutlinesShape(), this.xFactor, this.yFactor );
		}
		return this.outlineLines;
	}

	public void releaseOutlineLines() {
		//todo
		//		this.outlineLinesLock.notify();
	}

	public java.awt.geom.Rectangle2D.Float getBounds( java.awt.geom.Rectangle2D.Float rv ) {
		if( this.bounds == null ) {
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
					float xCurr = segment[ 0 ] * this.xFactor;
					float yCurr = segment[ 1 ] * this.yFactor;
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
				this.bounds = new java.awt.geom.Rectangle2D.Float( xMin, yMin, xMax - xMin, yMax - yMin );
			} else {
				this.bounds = new java.awt.geom.Rectangle2D.Float( Float.NaN, Float.NaN, Float.NaN, Float.NaN );
			}
		}
		rv.setFrame( this.bounds );

		return rv;
	}

	public java.awt.geom.Rectangle2D.Float getBounds() {
		return getBounds( new java.awt.geom.Rectangle2D.Float() );
	}

	public String getText() {
		return this.text;
	}

	public boolean setText( String text ) {
		if( edu.cmu.cs.dennisc.java.util.Objects.notEquals( this.text, text ) ) {
			this.text = text;
			markShapesDirty();
			return true;
		} else {
			return false;
		}
	}

	public java.awt.Font getFont() {
		return this.font;
	}

	public boolean setFont( java.awt.Font font ) {
		if( edu.cmu.cs.dennisc.java.util.Objects.notEquals( this.font, font ) ) {
			this.font = font;
			markShapesDirty();
			return true;
		} else {
			return false;
		}
	}

	private static java.awt.font.FontRenderContext s_frc = new java.awt.font.FontRenderContext( null, false, true );
	private static java.awt.Stroke s_stroke = new java.awt.BasicStroke( 0 );

	private String text;
	private java.awt.Font font;
	private final float xFactor;
	private final float yFactor;

	public static final double FLATNESS = 0.01;

	private java.awt.font.GlyphVector glyphVector = null;
	private java.awt.Shape facesShape = null;
	private java.awt.Shape outlinesShape = null;

	private java.util.List<java.util.List<edu.cmu.cs.dennisc.math.Point2f>> faceContours = null;
	//private Object faceContoursLock = new Object();
	private java.util.List<java.util.List<edu.cmu.cs.dennisc.math.Point2f>> outlineLines = null;
	//private Object outlineLinesLock = new Object();

	private java.awt.geom.Rectangle2D.Float bounds;

	private static java.util.List<java.util.List<edu.cmu.cs.dennisc.math.Point2f>> iterate( java.awt.Shape shape, float xFactor, float yFactor ) {
		java.awt.geom.PathIterator pi = shape.getPathIterator( null, FLATNESS );

		java.util.List<java.util.List<edu.cmu.cs.dennisc.math.Point2f>> polylines = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		java.util.List<edu.cmu.cs.dennisc.math.Point2f> polyline = null;

		float[] segment = new float[ 6 ];
		while( !pi.isDone() ) {
			switch( pi.currentSegment( segment ) ) {
			case java.awt.geom.PathIterator.SEG_MOVETO:
				polyline = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				//note: no break
			case java.awt.geom.PathIterator.SEG_LINETO:
				assert polyline != null;
				polyline.add( new edu.cmu.cs.dennisc.math.Point2f( segment[ 0 ] * xFactor, segment[ 1 ] * yFactor ) );
				break;
			case java.awt.geom.PathIterator.SEG_CLOSE:
				assert polyline != null;
				//				if( this.isReversed ) {
				//					reverse( polyline );
				//				}
				polylines.add( polyline );
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
}
