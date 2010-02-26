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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
class SuggestiveTextUtilties {
	public static void drawBlankTextIfNecessary( javax.swing.text.JTextComponent textComponent, java.awt.Graphics g, String textForBlankCondition ) {
		if( textComponent.isEditable() && textComponent.isEnabled() ) {
			String text = textComponent.getText();
			if( text.length() > 0 ) {
				//pass
			} else {
				java.awt.Font font = textComponent.getFont().deriveFont( java.awt.Font.ITALIC );
				g.setFont( font );
				g.setColor( edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 127 ) );
				java.awt.FontMetrics fm = g.getFontMetrics();
				//java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( text, g );
				g.drawString( textForBlankCondition, 0, textComponent.getHeight()-textComponent.getInsets().bottom-fm.getDescent() );
			}
		}
	}
}
