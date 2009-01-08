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

package org.alice.apis.moveandturn;

import org.alice.apis.moveandturn.font.Attribute;
import org.alice.apis.moveandturn.font.FamilyAttribute;
import org.alice.apis.moveandturn.font.FamilyConstant;
import org.alice.apis.moveandturn.font.PostureAttribute;
import org.alice.apis.moveandturn.font.PostureConstant;
import org.alice.apis.moveandturn.font.SizeAttribute;
import org.alice.apis.moveandturn.font.SizeValue;
import org.alice.apis.moveandturn.font.WeightAttribute;
import org.alice.apis.moveandturn.font.WeightConstant;

/**
 * @author Dennis Cosgrove
 */
public class Font {
	private java.awt.Font m_awtFont;

	
	public Font( 
			@edu.cmu.cs.dennisc.lang.ParameterAnnotation( isVariable=true )
			Attribute< ? >... attributes 
	) {
		java.util.Map< java.awt.font.TextAttribute, Object > map = new java.util.HashMap< java.awt.font.TextAttribute, Object >();
		for( Attribute< ? > attribute : attributes ) {
			map.put( attribute.getKey(), attribute.getValue() );
		}
		m_awtFont = new java.awt.Font( map );
	}
	public Font( java.awt.Font awtFont ) {
		m_awtFont = awtFont;
	}

	public java.awt.Font getAsAWTFont() {
		return m_awtFont;
	}

	//todo
	public Font deriveScaledFont( float scalar ) {
		return deriveSizeFont( (int)(m_awtFont.getSize() * scalar) );
	}
	public Font deriveSizeFont( float size ) {
		return new Font( m_awtFont.deriveFont( size ) );
	}

	public FamilyAttribute getFamily() {
		String family = m_awtFont.getFamily();
		if( family.equals( "Serif" ) ) {
			return FamilyConstant.SERIF;
		} else {
			return FamilyConstant.SANS_SERIF;
		}
	}
	public WeightAttribute getWeight() {
		int style = m_awtFont.getStyle();
		if( (style & java.awt.Font.BOLD) != 0 ) {
			return WeightConstant.BOLD;
		} else {
			return WeightConstant.REGULAR;
		}
	}
	public PostureAttribute getPosture() {
		int style = m_awtFont.getStyle();
		if( (style & java.awt.Font.ITALIC) != 0 ) {
			return PostureConstant.OBLIQUE;
		} else {
			return PostureConstant.REGULAR;
		}
	}
	public SizeAttribute getSize() {
		int size = m_awtFont.getSize();
		return new SizeValue( (float)size );
	}
}
