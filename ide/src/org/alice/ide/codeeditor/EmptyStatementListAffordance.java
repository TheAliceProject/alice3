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
public class EmptyStatementListAffordance extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JLabel > {
	private static final java.awt.Stroke SOLID_STROKE = new java.awt.BasicStroke( 4.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
	private static java.awt.Color HIGHLIGHT_COLOR = new java.awt.Color( 255, 255, 220 );
	private static java.awt.Color SHADOW_COLOR = new java.awt.Color( 63, 63, 63 );
	private static java.awt.Color TOP_COLOR = new java.awt.Color( 0, 0, 0, 91 );
	private static java.awt.Color BOTTOM_COLOR = new java.awt.Color( 127, 127, 127, 91 );

	private static final java.awt.Stroke DASHED_STROKE = new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 0, new float[] { 9.0f, 3.0f }, 0);

	private boolean isDrawingDesired = true;
	
	private edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty;
	public EmptyStatementListAffordance( edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty ) {
		this.statementListProperty = statementListProperty;
	}
	
	@Override
	protected javax.swing.JLabel createAwtComponent() {
		javax.swing.JLabel rv = new javax.swing.JLabel() {
			@Override
			public void paint(java.awt.Graphics g) {
				if( isDrawingDesired ) {
					super.paint(g);
				}
			}
			private java.awt.geom.GeneralPath createPath( java.awt.geom.RoundRectangle2D.Float rr, boolean isTopLeft ) {
				 java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
				 float halfSize = Math.min( rr.width/2, rr.height/2 );
				 if( isTopLeft ) {
					 rv.moveTo( rr.x, rr.y );
				 } else {
					 rv.moveTo( rr.x + rr.width, rr.y + rr.height );
				 }
				 rv.lineTo( rr.x + rr.width, rr.y );
				 rv.lineTo( rr.x + rr.width - halfSize, rr.y + halfSize );
				 rv.lineTo( rr.x + halfSize, rr.y + rr.height - halfSize );
				 rv.lineTo( rr.x, rr.y + rr.height );
				 rv.closePath();
				 return rv;
			}
			private java.awt.Shape createClip( java.awt.geom.RoundRectangle2D.Float rr, boolean isTopLeft ) {
				java.awt.geom.Area rv = new java.awt.geom.Area( rr );
				rv.subtract( new java.awt.geom.Area( this.createPath( rr, isTopLeft==false ) ) );
				return rv;
			}
			private boolean isMuted() {
				boolean isIf = ((edu.cmu.cs.dennisc.alice.ast.Node)statementListProperty.getOwner()).getParent() instanceof edu.cmu.cs.dennisc.alice.ast.BooleanExpressionBodyPair;
				boolean isElse = ((edu.cmu.cs.dennisc.alice.ast.Node)statementListProperty.getOwner()).getParent() instanceof edu.cmu.cs.dennisc.alice.ast.ConditionalStatement;
				return isIf || isElse;
			}
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				if( this.isMuted() ) {
					//pass
				} else {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					java.awt.Paint prevPaint = g2.getPaint();
					java.awt.Stroke prevStroke = g2.getStroke();
					java.awt.Shape prevClip = g2.getClip();
					g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
					
					int x = 0;
					int y = 0;
					int width = this.getWidth();
					int height = this.getHeight();
					java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( x, y, width-1, height-1, 8, 8 );
					g2.setPaint( new java.awt.GradientPaint( 0, 0, TOP_COLOR, 0, height, BOTTOM_COLOR ) );
					g2.fill( rr );
					
					g2.setStroke( SOLID_STROKE );
					g2.setClip( this.createClip( rr, true ) );
					g2.setPaint( SHADOW_COLOR );
					g2.draw( rr );
					
					g2.setClip( this.createClip( rr, false ) );
					g2.setPaint( HIGHLIGHT_COLOR );
					g2.draw( rr );

					g2.setClip( prevClip );
					g2.setStroke( prevStroke );
					g2.setPaint( prevPaint );

					g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_DEFAULT );
				}
				super.paintComponent( g );
			}
			@Override
			protected void paintBorder(java.awt.Graphics g) {
				super.paintBorder(g);
				if( this.isMuted() ) {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					g2.setColor(java.awt.Color.GRAY);
					g2.setStroke(DASHED_STROKE);
					java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( 1, 1, this.getWidth()-3, this.getHeight()-3, 8, 8 );
					g2.draw(rr);
				}
			}
		};
		rv.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 48));
		rv.setText( "drop statement here" );
		edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont(rv, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.EXTRA_LIGHT);
		return rv;
	}
	public void setDrawingDesired(boolean isDrawingDesired) {
		this.isDrawingDesired = isDrawingDesired;
		this.repaint();
	}

	// @Override
	// protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int
	// height ) {
	// return java.awt.Color.RED;
	// }
	// @Override
	// protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int
	// height ) {
	// return new java.awt.GradientPaint( 0, y, TOP_COLOR, 0, y+height,
	// BOTTOM_COLOR );
	// }
	// @Override
	// protected edu.cmu.cs.dennisc.awt.BevelState getBevelState() {
	// return edu.cmu.cs.dennisc.awt.BevelState.SUNKEN;
	// }
	// @Override
	// protected void paintOutline( java.awt.Graphics2D g2,
	// java.awt.geom.RoundRectangle2D.Float rr ) {
	// edu.cmu.cs.dennisc.awt.BeveledRoundRectangle brr = new
	// edu.cmu.cs.dennisc.awt.BeveledRoundRectangle( rr );
	// brr.draw( g2, edu.cmu.cs.dennisc.awt.BevelState.SUNKEN, Float.NaN,
	// Float.NaN, 1.0f );
	// }
}
