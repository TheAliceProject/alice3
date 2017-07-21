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
public class RectangleUtilities {
	public static java.awt.Rectangle createCenteredRectangle( java.awt.Rectangle bound, int width, int height ) {
		int x0 = bound.x;
		int x1 = ( bound.x + bound.width ) - 1;
		int xC = ( x0 + x1 ) / 2;

		int y0 = bound.y;
		int y1 = ( bound.y + bound.height ) - 1;
		int yC = ( y0 + y1 ) / 2;

		return new java.awt.Rectangle( xC - ( width / 2 ), yC - ( height / 2 ), width, height );
	}

	public static java.awt.Rectangle createCenteredRectangle( java.awt.Rectangle bound, java.awt.Dimension size ) {
		return createCenteredRectangle( bound, size.width, size.height );
	}

	public static java.awt.Rectangle grow( java.awt.Rectangle rv, int xPad, int yPad ) {
		rv.x -= xPad;
		rv.y -= yPad;
		rv.width += xPad + xPad;
		rv.height += yPad + yPad;
		return rv;
	}

	public static java.awt.Rectangle grow( java.awt.Rectangle rv, int pad ) {
		return grow( rv, pad, pad );
	}

	public static java.awt.Point getPoint( java.awt.Rectangle rect, int xConstraint, int yConstraint ) {
		java.awt.Point rv = new java.awt.Point();
		switch( xConstraint ) {
		case javax.swing.SwingConstants.LEADING:
			rv.x = rect.x;
			break;
		case javax.swing.SwingConstants.TRAILING:
			rv.x = rect.x + rect.width;
			break;
		case javax.swing.SwingConstants.CENTER:
			rv.x = rect.x + ( rect.width / 2 );
			break;
		default:
			assert false : xConstraint;
		}
		switch( yConstraint ) {
		case javax.swing.SwingConstants.LEADING:
			rv.y = rect.y;
			break;
		case javax.swing.SwingConstants.TRAILING:
			rv.y = rect.y + rect.height;
			break;
		case javax.swing.SwingConstants.CENTER:
			rv.y = rect.y + ( rect.height / 2 );
			break;
		default:
			assert false : xConstraint;
		}
		return rv;
	}

	public static java.awt.Rectangle inset( java.awt.Rectangle rv, java.awt.Insets insets ) {
		if( insets != null ) {
			if( rv != null ) {
				rv.x -= insets.left;
				rv.y -= insets.top;
				rv.width += insets.left + insets.right;
				rv.height += insets.top + insets.bottom;
			} else {
				//todo?
				//				throw new NullPointerException();
			}
		}
		return rv;
	}

	public static void setBounds( java.awt.Rectangle hole, int xA, int yA, int xB, int yB ) {
		hole.x = Math.min( xA, xB );
		hole.y = Math.min( yA, yB );
		hole.width = Math.abs( xB - xA );
		hole.height = Math.abs( yB - yA );
	}

	public static java.awt.Rectangle toAwtRectangle( edu.cmu.cs.dennisc.math.immutable.MRectangleI rectangle ) {
		return rectangle != null ? new java.awt.Rectangle( rectangle.x, rectangle.y, rectangle.width, rectangle.height ) : null;
	}

	public static edu.cmu.cs.dennisc.math.immutable.MRectangleI toMRectangleI( java.awt.Rectangle rectangle ) {
		return rectangle != null ? new edu.cmu.cs.dennisc.math.immutable.MRectangleI( rectangle.x, rectangle.y, rectangle.width, rectangle.height ) : null;
	}
}
