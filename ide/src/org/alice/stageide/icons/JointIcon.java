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
package org.alice.stageide.icons;

/**
 * @author Dennis Cosgrove
 */
public class JointIcon extends ShapeIcon {

	private static final java.awt.Stroke BONE_STROKE = new java.awt.BasicStroke( 3.0f );
	private static final java.awt.Stroke JOINT_OUTLINE_STROKE = new java.awt.BasicStroke( 1.0f );
	private static final int JOINT_WIDTH = 6;
	private static final int JOINT_HEIGHT = 6;

	public JointIcon( java.awt.Dimension size ) {
		super( size );
	}
	private void drawJoint( java.awt.Graphics2D g2, int x, int y, java.awt.Paint fillPaint, java.awt.Paint outlinePaint ) {
		if( fillPaint != null ) {
			g2.setPaint( fillPaint );
			g2.fillOval( x - 4, y - 4, JOINT_WIDTH, JOINT_HEIGHT );
		}
		if( outlinePaint != null ) {
			g2.setPaint( outlinePaint );
			g2.drawOval( x - 4, y - 4, JOINT_WIDTH, JOINT_HEIGHT );
		}

	}
	@Override
	protected void paintIcon( java.awt.Graphics2D g2, int width, int height, java.awt.Paint fillPaint, java.awt.Paint drawPaint ) {
		final int INSET_X = 6;
		final int INSET_Y = 6;
		final int JOINT_A_X = INSET_X;
		final int JOINT_A_Y = INSET_Y;
		final int JOINT_B_X = 3 * width / 4;
		final int JOINT_B_Y = 2 * width / 5;
		final int JOINT_C_X = width / 2;
		final int JOINT_C_Y = height - INSET_Y;

		java.awt.Stroke prevStroke = g2.getStroke();
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		try {
			g2.setStroke( BONE_STROKE );
			g2.drawLine( JOINT_A_X, JOINT_A_Y, JOINT_B_X, JOINT_B_Y );
			g2.drawLine( JOINT_B_X, JOINT_B_Y, JOINT_C_X, JOINT_C_Y );
			g2.setStroke( JOINT_OUTLINE_STROKE );
			this.drawJoint( g2, JOINT_B_X, JOINT_B_Y, java.awt.Color.RED, java.awt.Color.BLACK );
			this.drawJoint( g2, JOINT_A_X, JOINT_A_Y, java.awt.Color.LIGHT_GRAY, java.awt.Color.BLACK );
			this.drawJoint( g2, JOINT_C_X, JOINT_C_Y, java.awt.Color.LIGHT_GRAY, java.awt.Color.BLACK );
		} finally {
			g2.setStroke( prevStroke );
		}
	}
}
