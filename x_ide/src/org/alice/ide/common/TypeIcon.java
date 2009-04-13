/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class TypeIcon implements javax.swing.Icon {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;
	private TypeBorder border;
	public TypeIcon( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this.type = type;
		this.border = TypeBorder.getSingletonFor( type );
	}
	private String getText() {
		String rv;
		if( this.type != null ) {
			rv = this.type.getName();
		} else {
			rv = org.alice.ide.IDE.getSingleton().getTextForNull();
		}
		return rv;
	}
	public int getIconWidth() {
		java.awt.Insets insets = this.border.getBorderInsets( null );
		java.awt.Graphics g = edu.cmu.cs.dennisc.swing.SwingUtilities.getGraphics();
		java.awt.FontMetrics fm = g.getFontMetrics();
		java.awt.geom.Rectangle2D bounds = fm.getStringBounds( this.getText(), g );
		return insets.left + insets.right + (int)bounds.getWidth();
	}
	public int getIconHeight() {
		java.awt.Insets insets = this.border.getBorderInsets( null );
		java.awt.Graphics g = edu.cmu.cs.dennisc.swing.SwingUtilities.getGraphics();
		java.awt.FontMetrics fm = g.getFontMetrics();
		java.awt.geom.Rectangle2D bounds = fm.getStringBounds( this.getText(), g );
		return insets.top + insets.bottom + (int)bounds.getHeight();
	}
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		int w = this.getIconWidth();
		int h = this.getIconHeight();
		this.border.paintBorder( c, g, x, y, w, h );
//		if( c.isEnabled() ) {
			g.setColor( c.getForeground() );
//		} else {
//			g.setColor( java.awt.Color.RED );
//		}
		edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, this.getText(), x, y, w, h );
	}
}

