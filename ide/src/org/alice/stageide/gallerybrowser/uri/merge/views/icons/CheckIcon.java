/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.gallerybrowser.uri.merge.views.icons;

/**
 * @author Dennis Cosgrove
 */
public class CheckIcon extends org.alice.stageide.icons.PlusIcon {
	private final java.awt.Shape checkShape;
	private final java.awt.Stroke innerStroke;
	private final java.awt.Stroke outerStroke;

	public CheckIcon( java.awt.Dimension size ) {
		super( size );

		int unit = size.height - PAD - PAD;

		double xA = 0.3;
		double xC = 0.7;
		double xB = xA + ( ( xC - xA ) * 0.3 );

		double yA = 0.45;
		double yB = xC;
		double yC = xA;

		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( xA * unit, yA * unit );
		path.lineTo( xB * unit, yB * unit );
		path.lineTo( xC * unit, yC * unit );
		this.checkShape = path;

		this.innerStroke = new java.awt.BasicStroke( unit * 0.1f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
		this.outerStroke = new java.awt.BasicStroke( unit * 0.15f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
	}

	@Override
	protected java.awt.Paint getPlusPaint( javax.swing.ButtonModel buttonModel ) {
		return null;
	}

	@Override
	protected void paintIcon( java.awt.Component c, java.awt.Graphics2D g2, int width, int height, java.awt.Paint fillPaint, java.awt.Paint drawPaint, javax.swing.ButtonModel buttonModel ) {
		super.paintIcon( c, g2, width, height, fillPaint, drawPaint, buttonModel );
		java.awt.Stroke prevStroke = g2.getStroke();
		g2.setStroke( this.outerStroke );
		g2.setPaint( java.awt.Color.BLACK );
		g2.draw( this.checkShape );
		g2.setStroke( this.innerStroke );
		g2.setPaint( java.awt.Color.DARK_GRAY );
		g2.draw( this.checkShape );
		g2.setStroke( prevStroke );
	}
}
