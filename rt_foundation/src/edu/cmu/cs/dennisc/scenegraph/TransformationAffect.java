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

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */

public enum TransformationAffect {
	AFFECT_TRANSLATION_X_ONLY( false, true, false, false ),
	AFFECT_TRANSLATION_Y_ONLY( false, false, true, false ),
	AFFECT_TRANSLATION_Z_ONLY( false, false, false, true ),
	AFFECT_TRANSLATION_XY_ONLY( false, true, true, false ),
	AFFECT_TRANSLATION_XZ_ONLY( false, true, false, true ),
	AFFECT_TRANSLATION_YZ_ONLY( false, false, true, true ),
	AFFECT_TRANSLATION_ONLY( false, true, true, true ),
	AFFECT_ORIENTAION_ONLY( true, false, false, false ),
	AFFECT_ALL( true, true, true, true );
	private boolean m_isAffectOrientationDesired;
	private boolean m_isAffectTranslationXDesired;
	private boolean m_isAffectTranslationYDesired;
	private boolean m_isAffectTranslationZDesired;
	TransformationAffect( boolean isAffectOrientationDesired, boolean isAffectTranslationXDesired, boolean isAffectTranslationYDesired, boolean isAffectTranslationZDesired ) {
		m_isAffectOrientationDesired = isAffectOrientationDesired;
		m_isAffectTranslationXDesired = isAffectTranslationXDesired;
		m_isAffectTranslationYDesired = isAffectTranslationYDesired;
		m_isAffectTranslationZDesired = isAffectTranslationZDesired;
	}
	public void set( edu.cmu.cs.dennisc.math.AffineMatrix4x4 dst, edu.cmu.cs.dennisc.math.AffineMatrix4x4 src ) {
		if( m_isAffectOrientationDesired ) {
			dst.orientation.setValue( src.orientation );
		}
		if( m_isAffectTranslationXDesired ) {
			dst.translation.x = src.translation.x;
		}
		if( m_isAffectTranslationYDesired ) {
			dst.translation.y = src.translation.y;
		}
		if( m_isAffectTranslationZDesired ) {
			dst.translation.z = src.translation.z;
		}
	}
	public static TransformationAffect getTranslationAffect( double x, double y, double z ) {
		if( Double.isNaN( x ) ) {
			if( Double.isNaN( y ) ) {
				if( Double.isNaN( z ) ) {
					return null;
				} else {
					return AFFECT_TRANSLATION_Z_ONLY;
				}
			} else {
				if( Double.isNaN( z ) ) {
					return AFFECT_TRANSLATION_Y_ONLY;
				} else {
					return AFFECT_TRANSLATION_YZ_ONLY;
				}
			}
		} else {
			if( Double.isNaN( y ) ) {
				if( Double.isNaN( z ) ) {
					return AFFECT_TRANSLATION_X_ONLY;
				} else {
					return AFFECT_TRANSLATION_XZ_ONLY;
				}
			} else {
				if( Double.isNaN( z ) ) {
					return AFFECT_TRANSLATION_XY_ONLY;
				} else {
					return AFFECT_TRANSLATION_ONLY;
				}
			}
		}
	}
}
