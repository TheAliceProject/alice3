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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class TypeIcon implements javax.swing.Icon {
	private org.lgna.project.ast.AbstractType<?,?,?> type;
	private TypeBorder border;
	public static TypeIcon getInstance( org.lgna.project.ast.AbstractType<?,?,?> type ) {
		return new TypeIcon( type );
	}
	public TypeIcon( org.lgna.project.ast.AbstractType<?,?,?> type ) {
		this.type = type;
		this.border = TypeBorder.getSingletonFor( type );
	}

	protected java.awt.Font getFont() {
		return javax.swing.UIManager.getFont( "defaultFont" );
	}
	protected java.awt.Color getTextColor( java.awt.Component c ) {
		if( c.isEnabled() ) {
			return java.awt.Color.BLACK;
		} else {
			return java.awt.Color.GRAY;
		}
	}
	private String getText() {
		org.alice.ide.formatter.Formatter formatter = org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem();
		return formatter.getTextForType( this.type );
	}
	public int getIconWidth() {
		return this.getBorderWidth();
	}
	public int getIconHeight() {
		return this.getBorderHeight();
	}
	private java.awt.geom.Rectangle2D getTextBounds() {
		String text = this.getText();
		if( text != null ) {
			java.awt.Graphics g = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.getGraphics();
			java.awt.Font font = this.getFont();
			java.awt.FontMetrics fm;
			if( font != null ) {
				fm = g.getFontMetrics( font );
			} else {
				fm = g.getFontMetrics();
			}
			return fm.getStringBounds( text, g );
		} else {
			return new java.awt.geom.Rectangle2D.Float( 0,0,0,0 );
		}
	}
	private int getBorderWidth() {
		java.awt.Insets insets = this.border.getBorderInsets( null );
		java.awt.geom.Rectangle2D bounds = this.getTextBounds();
		return insets.left + insets.right + (int)bounds.getWidth();
	}
	private int getBorderHeight() {
		java.awt.Insets insets = this.border.getBorderInsets( null );
		java.awt.geom.Rectangle2D bounds = this.getTextBounds();
		return insets.top + insets.bottom + (int)bounds.getHeight();
	}
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		int w = this.getBorderWidth();
		int h = this.getBorderHeight();
		this.border.paintBorder( c, g, x, y, w, h );
		g.setColor( this.getTextColor( c ) );
		
		java.awt.Font font = g.getFont();
		if( font.isItalic() ) {
			g.setFont( edu.cmu.cs.dennisc.java.awt.FontUtilities.deriveFont( font, edu.cmu.cs.dennisc.java.awt.font.TextPosture.REGULAR ) );
		}
		edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g, this.getText(), x, y, w, h );
		if( font.isItalic() ) {
			g.setFont( font );
		}
	}
}

