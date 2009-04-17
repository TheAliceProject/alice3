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
public class ColorUtilities {
	public static final java.awt.Color GARISH_COLOR = java.awt.Color.MAGENTA;
	public static java.awt.Color createGray( int grayscale ) {
		return new java.awt.Color( grayscale, grayscale, grayscale );
	}
	private static float[] s_aBuffer = new float[ 4 ];
	private static float[] s_bBuffer = new float[ 4 ];
	private static float[] s_rvBuffer = new float[ 4 ];
	public static java.awt.Color interpolate( java.awt.Color a, java.awt.Color b, float portion ) {
		synchronized( s_rvBuffer ) {
			a.getComponents( s_aBuffer );
			b.getComponents( s_bBuffer );
			for( int i=0; i<4; i++ ) {
				s_rvBuffer[ i ] = s_aBuffer[ i ]*(1-portion) + s_bBuffer[ i ]*portion; 
			}
			return new java.awt.Color( s_rvBuffer[ 0 ], s_rvBuffer[ 1 ], s_rvBuffer[ 2 ], s_rvBuffer[ 3 ] );
		}
	}
	private static java.awt.Color constructColor( int rgb, int alpha ) {
		java.awt.Color c = new java.awt.Color( rgb );
		return new java.awt.Color( c.getRed(), c.getGreen(), c.getBlue(), alpha );
	}
	private static java.awt.Color constructColor( float[] hsb, int alpha ) {
		return constructColor( java.awt.Color.HSBtoRGB( bound( hsb[ 0 ] ), bound( hsb[ 1 ] ), bound( hsb[ 2 ] ) ), alpha );
	}
	
	private static float bound( float f ) {
		return Math.max(  Math.min( f, 1.0f ), 0.0f );
	}
	
	private static float[] s_hsbBuffer = new float[ 3 ];
	public static java.awt.Color shiftHSB( java.awt.Color color, double hueDelta, double saturationDelta, double brightnessDelta ) {
		synchronized( s_hsbBuffer ) {
			java.awt.Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), s_hsbBuffer );
			s_hsbBuffer[ 0 ] += hueDelta;
			s_hsbBuffer[ 1 ] += saturationDelta;
			s_hsbBuffer[ 2 ] += brightnessDelta;
			return constructColor( s_hsbBuffer, color.getAlpha() );
		}
	}
	public static synchronized java.awt.Color scaleHSB( java.awt.Color color, double hueScale, double saturationScale, double brightnessScale ) {
		synchronized( s_hsbBuffer ) {
			java.awt.Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), s_hsbBuffer );
			s_hsbBuffer[ 0 ] *= hueScale;
			s_hsbBuffer[ 1 ] *= saturationScale;
			s_hsbBuffer[ 2 ] *= brightnessScale;
			return constructColor( s_hsbBuffer, color.getAlpha() );
		}
	}
	public static synchronized java.awt.Color setAlpha( java.awt.Color color, int alpha ) {
		return new java.awt.Color( color.getRed(), color.getGreen(), color.getBlue(), alpha );
	}
	
}
