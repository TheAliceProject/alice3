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
package edu.cmu.cs.dennisc.javax.swing.icons;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractArrowIcon implements javax.swing.Icon {
	protected static enum Heading {
		EAST() {
			@Override
			public java.awt.geom.GeneralPath addPoints( java.awt.geom.GeneralPath rv, float x0, float xC, float x1, float y0, float yC, float y1 ) {
				rv.moveTo( x0, y0 );
				rv.lineTo( x1, yC );
				rv.lineTo( x0, y1 );
				return rv;
			}
		},
		SOUTH() {
			@Override
			public java.awt.geom.GeneralPath addPoints( java.awt.geom.GeneralPath rv, float x0, float xC, float x1, float y0, float yC, float y1 ) {
				rv.moveTo( x0, y0 );
				rv.lineTo( x1, y0 );
				rv.lineTo( xC, y1 );
				return rv;
			}
		};
		protected abstract java.awt.geom.GeneralPath addPoints( java.awt.geom.GeneralPath rv, float x0, float xC, float x1, float y0, float yC, float y1 );
	}

	private int size;

	public AbstractArrowIcon( int size ) {
		this.size = size;
	}

	@Override
	public int getIconWidth() {
		return this.size;
	}

	@Override
	public int getIconHeight() {
		return this.size;
	}

	protected java.awt.geom.GeneralPath createPath( int x, int y, Heading heading ) {
		float x0 = x;
		float x1 = ( x + this.size ) - 1;
		float xC = ( x0 + x1 ) * 0.5f;
		float y0 = y;
		float y1 = ( y + this.size ) - 1;
		float yC = ( y0 + y1 ) * 0.5f;
		java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
		heading.addPoints( rv, x0, xC, x1, y0, yC, y1 );
		rv.closePath();
		return rv;
	}
}
