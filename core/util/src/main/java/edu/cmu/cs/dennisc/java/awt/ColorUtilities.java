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
public class ColorUtilities {
	public static final java.awt.Color GARISH_COLOR = java.awt.Color.MAGENTA;

	public static java.awt.Color createGray( int grayscale ) {
		return new java.awt.Color( grayscale, grayscale, grayscale );
	}

	private static float[] s_hsbBuffer = new float[ 3 ];

	private static float[] s_aBuffer = new float[ 4 ];
	private static float[] s_bBuffer = new float[ 4 ];
	private static float[] s_rvBuffer = new float[ 4 ];

	public static java.awt.Color interpolate( java.awt.Color a, java.awt.Color b, float portion ) {
		synchronized( s_rvBuffer ) {
			a.getComponents( s_aBuffer );
			b.getComponents( s_bBuffer );
			for( int i = 0; i < 4; i++ ) {
				s_rvBuffer[ i ] = ( s_aBuffer[ i ] * ( 1 - portion ) ) + ( s_bBuffer[ i ] * portion );
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

	public static synchronized java.awt.Color setAlpha( java.awt.Color color, int alpha ) {
		return new java.awt.Color( color.getRed(), color.getGreen(), color.getBlue(), alpha );
	}

	private static float bound( float f ) {
		return Math.max( Math.min( f, 1.0f ), 0.0f );
	}

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

	public static float getHue( java.awt.Color color ) {
		synchronized( s_hsbBuffer ) {
			java.awt.Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), s_hsbBuffer );
			return s_hsbBuffer[ 0 ];
		}
	}

	public static float getSaturation( java.awt.Color color ) {
		synchronized( s_hsbBuffer ) {
			java.awt.Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), s_hsbBuffer );
			return s_hsbBuffer[ 1 ];
		}
	}

	public static float getBrightness( java.awt.Color color ) {
		synchronized( s_hsbBuffer ) {
			java.awt.Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), s_hsbBuffer );
			return s_hsbBuffer[ 2 ];
		}
	}

	public static String toHashText( java.awt.Color color ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "#" );
		sb.append( Integer.toHexString( color.getRGB() & 0xFFFFFF ) );
		return sb.toString();
	}

	public static java.awt.Color toAwtColor( edu.cmu.cs.dennisc.color.Color4f color ) {
		if( color != null ) {
			if( color.isNaN() ) {
				return null;
			} else {
				return new java.awt.Color( color.red, color.green, color.blue, color.alpha );
			}
		} else {
			return null;
		}
	}
}
