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
package org.alice.ide.icons;

/**
 * @author Dennis Cosgrove
 */
public class CheckIcon extends org.lgna.croquet.icon.AbstractIcon {
	private final java.awt.Shape shape;
	private final java.awt.Stroke innerStroke;
	private final java.awt.Stroke outerStroke;

	public CheckIcon( java.awt.Dimension size ) {
		super( size );
		int unit = size.width;

		double xA = 0.2;
		double xC = 0.8;
		double xB = xA + ( ( xC - xA ) * 0.3 );

		double yA = 0.45;
		double yB = xC;
		double yC = xA;

		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( xA * unit, yA * unit );
		path.lineTo( xB * unit, yB * unit );
		path.lineTo( xC * unit, yC * unit );
		shape = path;
		innerStroke = new java.awt.BasicStroke( unit * 0.2f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
		outerStroke = new java.awt.BasicStroke( unit * 0.25f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
	}

	protected java.awt.Paint getInnerPaint( java.awt.Component c ) {
		return new java.awt.Color( 0, 127, 0 );
	}

	protected java.awt.Paint getOuterPaint( java.awt.Component c ) {
		return java.awt.Color.WHITE;
	}

	@Override
	protected void paintIcon( java.awt.Component c, java.awt.Graphics2D g2 ) {
		java.awt.Stroke prevStroke = g2.getStroke();
		g2.setStroke( outerStroke );
		g2.setPaint( this.getOuterPaint( c ) );
		g2.draw( shape );
		g2.setStroke( innerStroke );
		g2.setPaint( this.getInnerPaint( c ) );
		g2.draw( shape );
		g2.setStroke( prevStroke );
	}
}
