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
package edu.cmu.cs.dennisc.java.awt;

/**
 * @author Dennis Cosgrove
 */
public class FontUtilities {
	private FontUtilities() {
		throw new AssertionError();
	}

	public static java.awt.Font deriveFont( java.awt.Font font, java.awt.font.TextAttribute attribute, Object value ) {
		if( font != null ) {
			java.util.Map<java.awt.font.TextAttribute, Object> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
			map.put( attribute, value );
			return font.deriveFont( map );
		} else {
			return null;
		}
	}

	public static java.awt.Font deriveFont( java.awt.Font font, java.awt.font.TextAttribute attributeA, Object valueA, java.awt.font.TextAttribute attributeB, Object valueB ) {
		if( font != null ) {
			java.util.Map<java.awt.font.TextAttribute, Object> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
			map.put( attributeA, valueA );
			map.put( attributeB, valueB );
			return font.deriveFont( map );
		} else {
			return null;
		}
	}

	public static java.awt.Font deriveFont( java.awt.Font font, java.awt.font.TextAttribute attributeA, Object valueA, java.awt.font.TextAttribute attributeB, Object valueB, java.awt.font.TextAttribute attributeC, Object valueC ) {
		if( font != null ) {
			java.util.Map<java.awt.font.TextAttribute, Object> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
			map.put( attributeA, valueA );
			map.put( attributeB, valueB );
			map.put( attributeC, valueC );
			return font.deriveFont( map );
		} else {
			return null;
		}
	}

	public static java.awt.Font deriveFont( java.awt.Font font, edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		if( font != null ) {
			java.util.Map<java.awt.font.TextAttribute, Object> map = new java.util.HashMap<java.awt.font.TextAttribute, Object>();
			for( edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?> textAttribute : textAttributes ) {
				map.put( textAttribute.getKey(), textAttribute.getValue() );
			}
			return font.deriveFont( map );
		} else {
			return null;
		}
	}

	public static java.awt.Font scaleFont( java.awt.Font font, float scaleFactor ) {
		if( font != null ) {
			if( scaleFactor != 1.0f ) {
				return font.deriveFont( font.getSize2D() * scaleFactor );
			} else {
				return font;
			}
		} else {
			return null;
		}
	}

	public static java.awt.Font deriveFont( java.awt.Component component, java.util.Map<? extends java.awt.font.TextAttribute, Object> map ) {
		return component.getFont().deriveFont( map );
	}

	public static java.awt.Font deriveFont( java.awt.Component component, java.awt.font.TextAttribute attribute, Object value ) {
		return FontUtilities.deriveFont( component.getFont(), attribute, value );
	}

	public static java.awt.Font deriveFont( java.awt.Component component, java.awt.font.TextAttribute attributeA, Object valueA, java.awt.font.TextAttribute attributeB, Object valueB ) {
		return FontUtilities.deriveFont( component.getFont(), attributeA, valueA, attributeB, valueB );
	}

	public static java.awt.Font deriveFont( java.awt.Component component, java.awt.font.TextAttribute attributeA, Object valueA, java.awt.font.TextAttribute attributeB, Object valueB, java.awt.font.TextAttribute attributeC, Object valueC ) {
		return FontUtilities.deriveFont( component.getFont(), attributeA, valueA, attributeB, valueB, attributeC, valueC );
	}

	public static java.awt.Font deriveFont( java.awt.Component component, edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		return FontUtilities.deriveFont( component.getFont(), textAttributes );
	}

	public static java.awt.Font scaleFont( java.awt.Component component, float scaleFactor ) {
		return FontUtilities.scaleFont( component.getFont(), scaleFactor );
	}

	public static void setFontToDerivedFont( java.awt.Component component, java.util.Map<? extends java.awt.font.TextAttribute, Object> map ) {
		component.setFont( deriveFont( component, map ) );
	}

	public static void setFontToDerivedFont( java.awt.Component component, java.awt.font.TextAttribute attribute, Object value ) {
		component.setFont( deriveFont( component, attribute, value ) );
	}

	public static void setFontToDerivedFont( java.awt.Component component, java.awt.font.TextAttribute attributeA, Object valueA, java.awt.font.TextAttribute attributeB, Object valueB ) {
		component.setFont( deriveFont( component, attributeA, valueA, attributeB, valueB ) );
	}

	public static void setFontToDerivedFont( java.awt.Component component, java.awt.font.TextAttribute attributeA, Object valueA, java.awt.font.TextAttribute attributeB, Object valueB, java.awt.font.TextAttribute attributeC, Object valueC ) {
		component.setFont( deriveFont( component, attributeA, valueA, attributeB, valueB, attributeC, valueC ) );
	}

	public static void setFontToDerivedFont( java.awt.Component component, edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		component.setFont( deriveFont( component, textAttributes ) );
	}

	public static void setFontToScaledFont( java.awt.Component component, float scaleFactor ) {
		if( scaleFactor != 1.0f ) {
			component.setFont( scaleFont( component, scaleFactor ) );
		}
	}
}
