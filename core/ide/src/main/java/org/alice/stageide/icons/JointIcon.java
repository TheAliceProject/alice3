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
package org.alice.stageide.icons;

/**
 * @author Dennis Cosgrove
 */
public class JointIcon extends ShapeIcon {
	private final java.awt.Stroke boneStroke;
	private final java.awt.Stroke jointOutlineStroke;
	private final float jointWidth;
	private final float jointHeight;

	public JointIcon( java.awt.Dimension size ) {
		super( size );
		int n = Math.min( size.width, size.height );
		boneStroke = new java.awt.BasicStroke( n / 12.0f );
		jointOutlineStroke = new java.awt.BasicStroke( 1.0f );
		jointHeight = n * 0.25f;
		jointWidth = jointHeight;
	}

	private void drawJoint( java.awt.Graphics2D g2, float x, float y, java.awt.Paint fillPaint, java.awt.Paint outlinePaint ) {
		java.awt.geom.Ellipse2D.Float ellipse = new java.awt.geom.Ellipse2D.Float( x - ( this.jointWidth * 0.5f ), y - ( this.jointHeight * 0.5f ), this.jointWidth, this.jointHeight );
		if( fillPaint != null ) {
			g2.setPaint( fillPaint );
			g2.fill( ellipse );
		}
		if( outlinePaint != null ) {
			g2.setPaint( outlinePaint );
			g2.draw( ellipse );
		}
	}

	@Override
	protected void paintIcon( java.awt.Component c, java.awt.Graphics2D g2, int width, int height, java.awt.Paint fillPaint, java.awt.Paint drawPaint ) {
		final float INSET_X = this.jointWidth;
		final float INSET_Y = this.jointHeight;
		final float JOINT_A_X = INSET_X;
		final float JOINT_A_Y = INSET_Y;
		final float JOINT_B_X = width * 0.75f;
		final float JOINT_B_Y = height * 0.4f;
		final float JOINT_C_X = width * 0.5f;
		final float JOINT_C_Y = height - INSET_Y;

		java.awt.Stroke prevStroke = g2.getStroke();
		try {
			g2.setStroke( boneStroke );
			drawLine( g2, JOINT_A_X, JOINT_A_Y, JOINT_B_X, JOINT_B_Y );
			drawLine( g2, JOINT_B_X, JOINT_B_Y, JOINT_C_X, JOINT_C_Y );
			g2.setStroke( jointOutlineStroke );
			this.drawJoint( g2, JOINT_B_X, JOINT_B_Y, java.awt.Color.RED, java.awt.Color.BLACK );
			this.drawJoint( g2, JOINT_A_X, JOINT_A_Y, java.awt.Color.LIGHT_GRAY, java.awt.Color.BLACK );
			this.drawJoint( g2, JOINT_C_X, JOINT_C_Y, java.awt.Color.LIGHT_GRAY, java.awt.Color.BLACK );
		} finally {
			g2.setStroke( prevStroke );
		}
	}
}
