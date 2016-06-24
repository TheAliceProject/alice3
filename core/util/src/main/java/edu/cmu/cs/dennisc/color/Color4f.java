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
package edu.cmu.cs.dennisc.color;

/**
 * @author Dennis Cosgrove
 */
public final class Color4f implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public static final Color4f BLACK = Color4f.createFromRgbInts( 0, 0, 0 );
	public static final Color4f BLUE = Color4f.createFromRgbInts( 0, 0, 255 );
	public static final Color4f CYAN = Color4f.createFromRgbInts( 0, 255, 255 );
	public static final Color4f DARK_GRAY = Color4f.createFromRgbInts( 64, 64, 64 );
	public static final Color4f GRAY = Color4f.createFromRgbInts( 128, 128, 128 );
	public static final Color4f GREEN = Color4f.createFromRgbInts( 0, 255, 0 );
	public static final Color4f LIGHT_GRAY = Color4f.createFromRgbInts( 192, 192, 192 );
	public static final Color4f MAGENTA = Color4f.createFromRgbInts( 255, 0, 255 );
	public static final Color4f ORANGE = Color4f.createFromRgbInts( 255, 200, 0 );
	public static final Color4f PINK = Color4f.createFromRgbInts( 255, 175, 175 );
	public static final Color4f RED = Color4f.createFromRgbInts( 255, 0, 0 );
	public static final Color4f WHITE = Color4f.createFromRgbInts( 255, 255, 255 );
	public static final Color4f YELLOW = Color4f.createFromRgbInts( 255, 255, 0 );

	public static final Color4f PURPLE = new Color4f( 128 / 255.0f, 0.0f, 128 / 255.0f, 1.0f );
	public static final Color4f BROWN = new Color4f( 162 / 255.0f, 42 / 255.0f, 42 / 255.0f, 1.0f );

	public static Color4f createFromRgbInts( int r, int g, int b ) {
		return createFromRgbaInts( r, g, b, 255 );
	}

	public static Color4f createFromRgbaInts( int r, int g, int b, int a ) {
		return new Color4f( r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f );
	}

	public Color4f( float red, float green, float blue, float alpha ) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public Color4f( Color4f other ) {
		this( other.red, other.green, other.blue, other.alpha );
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

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.red );
		binaryEncoder.encode( this.green );
		binaryEncoder.encode( this.blue );
		binaryEncoder.encode( this.alpha );
	}

	@Override
	public boolean equals( Object obj ) {
		if( obj instanceof Color4f ) {
			Color4f other = (Color4f)obj;
			return ( this.red == other.red ) && ( this.green == other.green ) && ( this.blue == other.blue ) && ( this.alpha == other.alpha );
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
		return Float.isNaN( this.red ) || Float.isNaN( this.green ) || Float.isNaN( this.blue ) || Float.isNaN( this.alpha );
	}

	public float[] getAsArray( float[] rv ) {
		rv[ 0 ] = this.red;
		rv[ 1 ] = this.green;
		rv[ 2 ] = this.blue;
		rv[ 3 ] = this.alpha;
		return rv;
	}

	public float[] getAsArray() {
		return getAsArray( new float[ 4 ] );
	}

	public java.nio.FloatBuffer getAsFloatBuffer( java.nio.FloatBuffer rv ) {
		rv.rewind();
		rv.put( this.red );
		rv.put( this.green );
		rv.put( this.blue );
		rv.put( this.alpha );
		rv.rewind();
		return rv;
	}

	public java.nio.FloatBuffer getAsFloatBuffer() {
		return this.getAsFloatBuffer( java.nio.FloatBuffer.allocate( 4 ) );
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

	public final float red;
	public final float green;
	public final float blue;
	public final float alpha;
}
