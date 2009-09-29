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
package edu.cmu.cs.dennisc.zoot;

/**
 * @author Dennis Cosgrove
 */
public class ZLabel extends javax.swing.JLabel implements edu.cmu.cs.dennisc.pattern.Reusable {
//	private static edu.cmu.cs.dennisc.pattern.AbstractPool< ZLabel > pool = new edu.cmu.cs.dennisc.pattern.AbstractPool< ZLabel >() {
//		@Override
//		protected zoot.ZLabel createInstance() {
//			return new ZLabel();
//		}
//	};
	
	//todo: reduce visibility to private
	protected ZLabel() {
		//this.setOpaque( true );
		//this.setBackground( edu.cmu.cs.dennisc.awt.ColorUtilities.GARISH_COLOR );
		this.setHorizontalAlignment( javax.swing.SwingConstants.LEADING );
		this.setVerticalAlignment( javax.swing.SwingConstants.CENTER );
		this.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		this.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );
	}

//	@Override
//	protected void finalize() throws Throwable {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "finalize ZLabel" );
//		super.finalize();
//	}
	
	public static ZLabel acquire() {
		//return ZLabel.pool.acquire();
		return new ZLabel();
	}
	public static ZLabel acquire( javax.swing.Icon icon ) {
		ZLabel rv = acquire();
		rv.setIcon( icon );
		return rv;
	}
	public static ZLabel acquire( String text ) {
		ZLabel rv = acquire();
		rv.setText( text );
		return rv;
	}
	public static ZLabel acquire( String text, java.util.Map< ? extends java.awt.font.TextAttribute, Object > map ) {
		ZLabel rv = acquire( text );
		rv.setFontToDerivedFont( map );
		return rv;
	}
	public static ZLabel acquire( String text, java.awt.font.TextAttribute attribute, Object value ) {
		ZLabel rv = acquire( text );
		rv.setFontToDerivedFont( attribute, value );
		return rv;
	}
	public static ZLabel acquire( String text, edu.cmu.cs.dennisc.zoot.font.ZTextAttribute< ? >... textAttributes ) {
		ZLabel rv = acquire( text );
		rv.setFontToDerivedFont( textAttributes );
		return rv;
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
	public void setFontToDerivedFont( edu.cmu.cs.dennisc.zoot.font.ZTextAttribute< ? >... textAttributes ) {
		java.awt.Font font = this.getFont();
		java.util.Map< java.awt.font.TextAttribute, Object > map = new java.util.HashMap< java.awt.font.TextAttribute, Object >();
		for( edu.cmu.cs.dennisc.zoot.font.ZTextAttribute< ? > textAttribute : textAttributes ) {
			map.put( textAttribute.getKey(), textAttribute.getValue() );
		}
		this.setFont( font.deriveFont( map ) );
	}
	public void setFontToScaledFont( Float scaleFactor ) {
		java.awt.Font font = this.getFont();
		this.setFont( font.deriveFont( font.getSize2D() * scaleFactor ) );
	}
}
