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
package zoot;

/**
 * @author Dennis Cosgrove
 */
public class ZLabel extends javax.swing.JLabel {
	public ZLabel() {
		this.setHorizontalAlignment( javax.swing.SwingConstants.LEADING );
		this.setVerticalAlignment( javax.swing.SwingConstants.CENTER );
		this.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		this.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );
	}
	
	public ZLabel( javax.swing.Icon icon ) {
		this();
		this.setIcon( icon );
	}
	public ZLabel( String text ) {
		this();
		this.setText( text );
	}
	public ZLabel( String text, java.util.Map< ? extends java.awt.font.TextAttribute, Object > map ) {
		this( text );
		this.setFontToDerivedFont( map );
	}
	public ZLabel( String text, java.awt.font.TextAttribute attribute, Object value ) {
		this( text );
		this.setFontToDerivedFont( attribute, value );
	}
	public ZLabel( String text, zoot.font.ZTextAttribute< ? >... textAttributes ) {
		this( text );
		this.setFontToDerivedFont( textAttributes );
	}
	
	public void setFontToDerivedFont( java.util.Map< ? extends java.awt.font.TextAttribute, Object > map ) {
		java.awt.Font font = this.getFont();
		this.setFont( font.deriveFont( map ) );
	}
	public void setFontToDerivedFont( java.awt.font.TextAttribute attribute, Object value ) {
		java.util.Map< java.awt.font.TextAttribute, Object > map = new java.util.HashMap< java.awt.font.TextAttribute, Object >();
		map.put( attribute, value );
		this.setFontToDerivedFont( map );
	}
	public void setFontToDerivedFont( zoot.font.ZTextAttribute< ? >... textAttributes ) {
		java.awt.Font font = this.getFont();
		java.util.Map< java.awt.font.TextAttribute, Object > map = new java.util.HashMap< java.awt.font.TextAttribute, Object >();
		for( zoot.font.ZTextAttribute< ? > textAttribute : textAttributes ) {
			map.put( textAttribute.getKey(), textAttribute.getValue() );
		}
		this.setFont( font.deriveFont( map ) );
	}
	public void setFontToScaledFont( Float scaleFactor ) {
		java.awt.Font font = this.getFont();
		this.setFont( font.deriveFont( font.getSize2D() * scaleFactor ) );
	}
}
