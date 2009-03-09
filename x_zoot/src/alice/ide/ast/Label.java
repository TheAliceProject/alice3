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
package alice.ide.ast;

/**
 * @author Dennis Cosgrove
 */
public class Label extends javax.swing.JLabel {
	public Label() {
		this( "" );
	}
	public Label( String text ) {
		this( text, 1.0f );
	}
	public Label( String text, float scaleFactor ) {
		this.setHorizontalAlignment( javax.swing.SwingConstants.LEADING );
		this.setVerticalAlignment( javax.swing.SwingConstants.CENTER );
		this.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		this.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );
		this.setText( text );
		this.scaleFont( scaleFactor );
	}
	public void scaleFont( float scaleFactor ) {
		java.awt.Font font = this.getFont();
		font = font.deriveFont( font.getSize() * scaleFactor  );
		this.setFont( font );
	}

	public void italicizeFont() {
		java.awt.Font font = this.getFont();
		font = font.deriveFont( java.awt.Font.ITALIC );
		this.setFont( font );
	}
}
