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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public class EmptyStatementListAfforance extends org.alice.ide.common.StatementLikeSubstance {
//	private static java.awt.Color TOP_COLOR = new java.awt.Color( 0, 0, 0, 63 );
//	private static java.awt.Color BOTTOM_COLOR = new java.awt.Color( 127, 127, 127, 63 );

	public EmptyStatementListAfforance() {
		super( edu.cmu.cs.dennisc.alice.ast.Statement.class, javax.swing.BoxLayout.LINE_AXIS );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 16, 8, 48 ) );
		this.addComponent( new edu.cmu.cs.dennisc.croquet.Label( "drop statement here", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.EXTRA_LIGHT ) );
	}
	@Override
	protected boolean isKnurlDesired() {
		return false;
	}
//	@Override
//	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
//		return java.awt.Color.RED;
//	}
	//	@Override
	//	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
	//		return new java.awt.GradientPaint( 0, y, TOP_COLOR, 0, y+height, BOTTOM_COLOR );
	//	}
	//	@Override
	//	protected edu.cmu.cs.dennisc.awt.BevelState getBevelState() {
	//		return edu.cmu.cs.dennisc.awt.BevelState.SUNKEN;
	//	}
	//	@Override
	//	protected void paintOutline( java.awt.Graphics2D g2, java.awt.geom.RoundRectangle2D.Float rr ) {
	//		edu.cmu.cs.dennisc.awt.BeveledRoundRectangle brr = new edu.cmu.cs.dennisc.awt.BeveledRoundRectangle( rr );
	//		brr.draw( g2, edu.cmu.cs.dennisc.awt.BevelState.SUNKEN, Float.NaN, Float.NaN, 1.0f );
	//	}
	private static final java.awt.Stroke DASHED_STROKE = new java.awt.BasicStroke( 1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0, new float[] {9.0f, 3.0f}, 0 );
	@Override
	protected void paintOutline( java.awt.Graphics2D g2, java.awt.geom.RoundRectangle2D.Float rr ) {
		g2.setColor( java.awt.Color.GRAY );
		g2.setStroke( DASHED_STROKE );
		g2.draw( rr );
	}
}
