/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package edu.cmu.cs.dennisc.color;

/**
 * @author Dennis Cosgrove
 */
public final class Color4f implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public static final Color4f BLACK = new Color4f( java.awt.Color.BLACK );
	public static final Color4f BLUE = new Color4f( java.awt.Color.BLUE );
	public static final Color4f CYAN = new Color4f( java.awt.Color.CYAN );
	public static final Color4f DARK_GRAY = new Color4f( java.awt.Color.DARK_GRAY );
	public static final Color4f GRAY = new Color4f( java.awt.Color.GRAY );
	public static final Color4f GREEN = new Color4f( java.awt.Color.GREEN );
	public static final Color4f LIGHT_GRAY = new Color4f( java.awt.Color.LIGHT_GRAY );
	public static final Color4f MAGENTA = new Color4f( java.awt.Color.MAGENTA );
	public static final Color4f ORANGE = new Color4f( java.awt.Color.ORANGE );
	public static final Color4f PINK = new Color4f( java.awt.Color.PINK );
	public static final Color4f RED = new Color4f( java.awt.Color.RED );
	public static final Color4f WHITE = new Color4f( java.awt.Color.WHITE );
	public static final Color4f YELLOW = new Color4f( java.awt.Color.YELLOW );

	public static final Color4f PURPLE = new Color4f( 128 / 255.0f, 0.0f, 128 / 255.0f, 1.0f );
	public static final Color4f BROWN = new Color4f( 162 / 255.0f, 42 / 255.0f, 42 / 255.0f, 1.0f );

	public final float red;
	public final float green;
	public final float blue;
	public final float alpha;

	public Color4f( float red, float green, float blue, float alpha ) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public Color4f( Color4f other ) {
		this( other.red, other.green, other.blue, other.alpha );
	}

	public Color4f( java.awt.Color awtColor ) {
		this( awtColor.getRed() / 255.0f, awtColor.getGreen() / 255.0f, awtColor.getBlue() / 255.0f, awtColor.getAlpha() / 255.0f );
	}

	public Color4f( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.red = binaryDecoder.decodeFloat();
		this.green = binaryDecoder.decodeFloat();
		this.blue = binaryDecoder.decodeFloat();
		this.alpha = binaryDecoder.decodeFloat();
	}

	public static Color4f createNaN() {
		return new Color4f( Float.NaN, Float.NaN, Float.NaN, Float.NaN );
	}

	public static Color4f createInterpolation( Color4f a, Color4f b, float portion ) {
		float red = a.red + ( ( b.red - a.red ) * portion );
		float green = a.green + ( ( b.green - a.green ) * portion );
		float blue = a.blue + ( ( b.blue - a.blue ) * portion );
		float alpha = a.alpha + ( ( b.alpha - a.alpha ) * portion );
		return new Color4f( red, green, blue, alpha );
	}

	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.red );
		binaryEncoder.encode( this.green );
		binaryEncoder.encode( this.blue );
		binaryEncoder.encode( this.alpha );
	}

	@Override
	public boolean equals( Object obj ) {
		if( obj instanceof Color4f ) {
			Color4f color = (Color4f)obj;
			return ( red == color.red ) && ( green == color.green ) && ( blue == color.blue ) && ( alpha == color.alpha );
		} else {
			return false;
		}
	}

	@Override
	public final int hashCode() {
		int rv = 17;
		rv = ( 37 * rv ) + Float.floatToIntBits( this.red );
		rv = ( 37 * rv ) + Float.floatToIntBits( this.green );
		rv = ( 37 * rv ) + Float.floatToIntBits( this.blue );
		rv = ( 37 * rv ) + Float.floatToIntBits( this.alpha );
		return rv;
	}

	public boolean isNaN() {
		return Float.isNaN( red ) || Float.isNaN( green ) || Float.isNaN( blue ) || Float.isNaN( alpha );
	}

	public java.awt.Color getAsAWTColor() {
		if( this.isNaN() ) {
			return null;
		} else {
			return new java.awt.Color( red, green, blue, alpha );
		}
	}

	public float[] getAsArray( float[] rv ) {
		rv[ 0 ] = red;
		rv[ 1 ] = green;
		rv[ 2 ] = blue;
		rv[ 3 ] = alpha;
		return rv;
	}

	public float[] getAsArray() {
		return getAsArray( new float[ 4 ] );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getName() );
		sb.append( "[red=" );
		sb.append( this.red );
		sb.append( ";green=" );
		sb.append( this.green );
		sb.append( ";blue=" );
		sb.append( this.blue );
		sb.append( ";alpha=" );
		sb.append( this.alpha );
		sb.append( "]" );
		return sb.toString();
	}
}
