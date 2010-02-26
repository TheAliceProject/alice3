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
package edu.cmu.cs.dennisc.awt;

/**
 * @author Dennis Cosgrove
 */
public class FontUtilities {
	private FontUtilities() {
		throw new AssertionError();
	}
	public static void setFontToDerivedFont( java.awt.Component component, java.util.Map< ? extends java.awt.font.TextAttribute, Object > map ) {
		java.awt.Font font = component.getFont();
		component.setFont( font.deriveFont( map ) );
	}
	public static void setFontToDerivedFont( java.awt.Component component, java.awt.font.TextAttribute attribute, Object value ) {
		java.util.Map< java.awt.font.TextAttribute, Object > map = new java.util.HashMap< java.awt.font.TextAttribute, Object >();
		map.put( attribute, value );
		setFontToDerivedFont( component, map );
	}
	public static void setFontToDerivedFont( java.awt.Component component, java.awt.font.TextAttribute attributeA, Object valueA, java.awt.font.TextAttribute attributeB, Object valueB ) {
		java.util.Map< java.awt.font.TextAttribute, Object > map = new java.util.HashMap< java.awt.font.TextAttribute, Object >();
		map.put( attributeA, valueA );
		map.put( attributeB, valueB );
		setFontToDerivedFont( component, map );
	}
	public static void setFontToDerivedFont( java.awt.Component component, java.awt.font.TextAttribute attributeA, Object valueA, java.awt.font.TextAttribute attributeB, Object valueB, java.awt.font.TextAttribute attributeC, Object valueC ) {
		java.util.Map< java.awt.font.TextAttribute, Object > map = new java.util.HashMap< java.awt.font.TextAttribute, Object >();
		map.put( attributeA, valueA );
		map.put( attributeB, valueB );
		map.put( attributeC, valueC );
		setFontToDerivedFont( component, map );
	}
	public static void setFontToDerivedFont( java.awt.Component component, edu.cmu.cs.dennisc.zoot.font.ZTextAttribute< ? >... textAttributes ) {
		java.awt.Font font = component.getFont();
		java.util.Map< java.awt.font.TextAttribute, Object > map = new java.util.HashMap< java.awt.font.TextAttribute, Object >();
		for( edu.cmu.cs.dennisc.zoot.font.ZTextAttribute< ? > textAttribute : textAttributes ) {
			map.put( textAttribute.getKey(), textAttribute.getValue() );
		}
		component.setFont( font.deriveFont( map ) );
	}
	public static void setFontToScaledFont( java.awt.Component component, float scaleFactor ) {
		java.awt.Font font = component.getFont();
		component.setFont( font.deriveFont( font.getSize2D() * scaleFactor ) );
	}
}
