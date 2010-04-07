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
package org.alice.stageide.sceneeditor;

/**
 * @author Dennis Cosgrove
 */
class IsExpandedCheckBoxUI extends javax.swing.plaf.basic.BasicButtonUI {
	private final static IsExpandedCheckBoxUI singleton = new IsExpandedCheckBoxUI();

	public static javax.swing.plaf.ComponentUI createUI( javax.swing.JComponent c ) {
		return singleton;
	}

	private java.awt.Paint getForegroundPaint( javax.swing.ButtonModel model ) {
		if( model.isPressed() ) {
			return new java.awt.Color( 128, 128, 0 );
		} else {
			if( model.isRollover() || model.isArmed() ) {
				return java.awt.Color.WHITE;
			} else {
				return java.awt.Color.YELLOW;
			}
		}
	}
	private java.awt.Paint getBackgroundPaint( javax.swing.ButtonModel model ) {
		if( model.isPressed() ) {
			return null;
		} else {
			if( model.isRollover() || model.isArmed() ) {
				return java.awt.Color.BLACK;
			} else {
				return java.awt.Color.DARK_GRAY;
			}
		}
	}
	@Override
	protected void paintText( java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle textRect, String text ) {
		//		super.paintText( g, b, textRect, text )

		javax.swing.AbstractButton button = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( c, javax.swing.AbstractButton.class );
		javax.swing.ButtonModel model = button.getModel();
		java.awt.Paint forePaint = this.getForegroundPaint( model );
		java.awt.Paint backPaint = this.getBackgroundPaint( model );
		java.awt.Graphics2D g2 = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( g, java.awt.Graphics2D.class );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

		java.awt.FontMetrics fm = g.getFontMetrics();
		int x = textRect.x + getTextShiftOffset();
		int y = textRect.y + fm.getAscent() + getTextShiftOffset();
		if( backPaint != null ) {
			g2.setPaint( backPaint );
			g2.drawString( text, x + 2, y + 2 );
		}
		g2.setPaint( forePaint );
		g2.drawString( text, x, y );
	}
	
	private void paintLines( java.awt.Graphics2D g2, boolean isSelected, int width, int height ) { 
		if( isSelected ) {
			int x = 0;
			int y = 0;
			int minor = 4;
			int MAJOR = 32;
			g2.drawLine( x + minor, y + MAJOR, x + minor, y + minor );
			g2.drawLine( x + minor, y + minor, x + MAJOR, y + minor );
			minor += 6;
			MAJOR -= 4;
			g2.drawLine( x + minor, y + MAJOR, x + minor, y + minor );
			g2.drawLine( x + minor, y + minor, x + MAJOR, y + minor );
		} else {
			int x = width;
			int y = height;
			int minor = -4;
			int MAJOR = -32;
			g2.drawLine( x + minor, y + MAJOR, x + minor, y + minor );
			g2.drawLine( x + minor, y + minor, x + MAJOR, y + minor );
			minor += -6;
			MAJOR -= -4;
			g2.drawLine( x + minor, y + MAJOR, x + minor, y + minor );
			g2.drawLine( x + minor, y + minor, x + MAJOR, y + minor );
		}
	}
	
	@Override
	public void paint( java.awt.Graphics g, javax.swing.JComponent c ) {
		super.paint( g, c );
		javax.swing.AbstractButton button = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( c, javax.swing.AbstractButton.class );
		javax.swing.ButtonModel model = button.getModel();
		java.awt.Graphics2D g2 = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( g, java.awt.Graphics2D.class );
		g2.setStroke( new java.awt.BasicStroke( 3.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ) );
		java.awt.Paint forePaint = this.getForegroundPaint( model );
		java.awt.Paint backPaint = this.getBackgroundPaint( model );
		if( backPaint != null ) {
			g2.setPaint( backPaint );
			g2.translate( 2, 2 );
			this.paintLines( g2, model.isSelected(), c.getWidth(), c.getHeight() );
			g2.translate( -2, -2 );
		}
		g2.setPaint( forePaint );
		this.paintLines( g2, model.isSelected(), c.getWidth(), c.getHeight() );
	}
}
//
///**
// * @author Dennis Cosgrove
// */
//class IsExpandedCheckBox extends edu.cmu.cs.dennisc.zoot.ZCheckBox {
//	private final int X_PAD = 16;
//	private final int Y_PAD = 10;
//
//	public IsExpandedCheckBox() {
//		super( org.alice.ide.IDE.getSingleton().getIsSceneEditorExpandedOperation() );
//		this.setOpaque( false );
//		this.setFont( this.getFont().deriveFont( 18.0f ) );
//		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( Y_PAD, X_PAD, Y_PAD, X_PAD ) );
//	}
//	@Override
//	public void updateUI() {
//		this.setUI( IsExpandedCheckBoxUI.createUI( this ) );
//	}
//	@Override
//	public String getText() {
//		if( isSelected() ) {
//			return "edit code";
//		} else {
//			return "edit scene";
//		}
//	}
//
//	private java.awt.Rectangle innerAreaBuffer = new java.awt.Rectangle();
//
//	@Override
//	public boolean contains( int x, int y ) {
//		java.awt.Rectangle bounds = javax.swing.SwingUtilities.calculateInnerArea( this, innerAreaBuffer );
//		if( this.isSelected() ) {
//			bounds.x -= X_PAD;
//			bounds.y -= Y_PAD;
//		}
//		bounds.width += X_PAD;
//		bounds.height += Y_PAD;
//		return bounds.contains( x, y );
//	}
//}
