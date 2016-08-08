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
package edu.cmu.cs.dennisc.java.awt;

/**
 * @author Dennis Cosgrove
 */
public class BeveledRoundRectangle extends BeveledShape {
	public BeveledRoundRectangle() {
	}

	public BeveledRoundRectangle( java.awt.geom.RoundRectangle2D.Float roundRect ) {
		initialize( roundRect );
	}

	public BeveledRoundRectangle( float x0, float y0, float width, float height, float arcwidth, float archeight ) {
		initialize( x0, y0, width, height, arcwidth, archeight );
	}

	private void initialize( float x0, float y0, float xA, float yA, float xB, float yB, float x1, float y1 ) {
		java.awt.geom.GeneralPath base = new java.awt.geom.GeneralPath();
		java.awt.geom.GeneralPath high = new java.awt.geom.GeneralPath();
		java.awt.geom.GeneralPath neut = new java.awt.geom.GeneralPath();
		java.awt.geom.GeneralPath shad = new java.awt.geom.GeneralPath();

		//		float x;
		//		float y;
		//		x=xB; y=y0;	base.moveTo(x,y); high.moveTo(x,y);
		//		x=xA; y=y0;	base.lineTo(x,y); high.lineTo(x,y);
		//		x=x0; y=yA;	base.lineTo(x,y); high.lineTo(x,y);
		//		x=x0; y=yB;	base.lineTo(x,y); high.lineTo(x,y); neut.moveTo(x,y);
		//		x=xA; y=y1;	base.lineTo(x,y);                   neut.lineTo(x,y); shad.moveTo(x,y);
		//		x=xB; y=y1;	base.lineTo(x,y);                                     shad.lineTo(x,y);
		//		x=x1; y=yB;	base.lineTo(x,y);                                     shad.lineTo(x,y);
		//		x=x1; y=yA;	base.lineTo(x,y);                   neut.moveTo(x,y); shad.lineTo(x,y);
		//		x=xB; y=y0;	base.lineTo(x,y);                   neut.lineTo(x,y);

		float x;
		float y;
		float a;
		float b;
		x = xB;
		y = y0;
		base.moveTo( x, y );
		high.moveTo( x, y );
		x = xA;
		y = y0;
		base.lineTo( x, y );
		high.lineTo( x, y );
		x = x0;
		y = yA;
		a = x0;
		b = y0;
		base.quadTo( a, b, x, y );
		high.quadTo( a, b, x, y );
		x = x0;
		y = yB;
		base.lineTo( x, y );
		high.lineTo( x, y );
		neut.moveTo( x, y );
		x = xA;
		y = y1;
		a = x0;
		b = y1;
		base.quadTo( a, b, x, y );
		neut.quadTo( a, b, x, y );
		shad.moveTo( x, y );
		x = xB;
		y = y1;
		base.lineTo( x, y );
		shad.lineTo( x, y );
		x = x1;
		y = yB;
		a = x1;
		b = y1;
		base.quadTo( a, b, x, y );
		shad.quadTo( a, b, x, y );
		x = x1;
		y = yA;
		base.lineTo( x, y );
		neut.moveTo( x, y );
		shad.lineTo( x, y );
		x = xB;
		y = y0;
		a = x1;
		b = y0;
		base.quadTo( a, b, x, y );
		neut.quadTo( a, b, x, y );

		base.closePath();

		initialize( base, high, neut, shad );

		//		java.awt.geom.GeneralPath highlightPath = new java.awt.geom.GeneralPath();
		//		java.awt.geom.PathIterator pathIterator = roundRect.getPathIterator( null );
		//		float[] coords = new float[ 6 ];
		//		while( pathIterator.isDone() == false ) {
		//			switch( pathIterator.currentSegment( coords ) ) {
		//			case java.awt.geom.PathIterator.SEG_MOVETO:
		//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "SEG_MOVETO", coords );
		//				highlightPath.moveTo( coords[ 0 ], coords[ 1 ] );
		//				break;
		//			case java.awt.geom.PathIterator.SEG_LINETO:
		//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "SEG_LINETO", coords );
		//				highlightPath.lineTo( coords[ 0 ], coords[ 1 ] );
		//				break;
		//			case java.awt.geom.PathIterator.SEG_QUADTO:
		//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "SEG_QUADTO", coords );
		//				highlightPath.quadTo( coords[ 0 ], coords[ 1 ], coords[ 2 ], coords[ 3 ] );
		//				break;
		//			case java.awt.geom.PathIterator.SEG_CUBICTO:
		//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "SEG_CUBICTO", coords );
		//				highlightPath.curveTo( coords[ 0 ], coords[ 1 ], coords[ 2 ], coords[ 3 ], coords[ 4 ], coords[ 5 ] );
		//				break;
		//			case java.awt.geom.PathIterator.SEG_CLOSE:
		//				highlightPath.closePath();
		//				break;
		//			}
		//			pathIterator.next();
		//		}
		//		initialize( roundRect, highlightPath, null, null );
	}

	public void initialize( float x, float y, float width, float height, float arcwidth, float archeight ) {
		float x0 = x;
		float xA = x + arcwidth;
		float xB = ( x + width ) - arcwidth;
		float x1 = x + width;
		float y0 = y;
		float yA = y + archeight;
		float yB = ( y + height ) - archeight;
		float y1 = y + height;
		initialize( x0, y0, xA, yA, xB, yB, x1, y1 );
	}

	public void initialize( java.awt.geom.RoundRectangle2D.Float roundRect ) {
		initialize( roundRect.x, roundRect.y, roundRect.width, roundRect.height, roundRect.arcwidth, roundRect.archeight );
	}
}
