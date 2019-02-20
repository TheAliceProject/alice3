/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.lgna.story;

import edu.cmu.cs.dennisc.java.lang.ParameterAnnotation;
import org.lgna.story.fontattributes.Attribute;
import org.lgna.story.fontattributes.FamilyAttribute;
import org.lgna.story.fontattributes.FamilyConstant;
import org.lgna.story.fontattributes.PostureAttribute;
import org.lgna.story.fontattributes.PostureConstant;
import org.lgna.story.fontattributes.SizeAttribute;
import org.lgna.story.fontattributes.SizeValue;
import org.lgna.story.fontattributes.WeightAttribute;
import org.lgna.story.fontattributes.WeightConstant;

import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public final class Font {
	private java.awt.Font m_awtFont;

	public Font(
			@ParameterAnnotation( isVariable = true )
			Attribute<?>... attributes ) {
		Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
		for( Attribute<?> attribute : attributes ) {
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
		return deriveSizeFont( (int)( m_awtFont.getSize() * scalar ) );
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
		if( ( style & java.awt.Font.BOLD ) != 0 ) {
			return WeightConstant.BOLD;
		} else {
			return WeightConstant.REGULAR;
		}
	}

	public PostureAttribute getPosture() {
		int style = m_awtFont.getStyle();
		if( ( style & java.awt.Font.ITALIC ) != 0 ) {
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
