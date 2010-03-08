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
public class Color4f implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
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
	
	public static final Color4f PURPLE = new Color4f( 128/255.0f, 0.0f, 128/255.0f, 1.0f );
	public static final Color4f BROWN = new Color4f( 162/255.0f, 42/255.0f, 42/255.0f, 1.0f );

	public float red = 0.0f;
	public float green = 0.0f;
	public float blue = 0.0f;
	public float alpha = 1.0f;
	
	public Color4f() {
	}
	public Color4f( Color4f other ) {
		set( other );
	}
	public Color4f( float red, float green, float blue, float alpha ) {
		set( red, green, blue, alpha );
	}
	public Color4f( java.awt.Color awtColor ) {
		set( awtColor.getRed()/255.0f, awtColor.getGreen()/255.0f, awtColor.getBlue()/255.0f, awtColor.getAlpha()/255.0f );
	}

	//todo
	public static Color4f createNaN() {
		return new Color4f( Float.NaN, Float.NaN, Float.NaN, Float.NaN );
	}
	
	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		red = binaryDecoder.decodeFloat();
		green = binaryDecoder.decodeFloat();
		blue = binaryDecoder.decodeFloat();
		alpha = binaryDecoder.decodeFloat();
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		binaryEncoder.encode( red );
		binaryEncoder.encode( green );
		binaryEncoder.encode( blue );
		binaryEncoder.encode( alpha );
	}
	
	@Override
	public boolean equals( Object obj ) {
		if( obj instanceof Color4f ) {
			Color4f color = (Color4f)obj;
			return red == color.red && green == color.green && blue == color.blue && alpha == color.alpha;
		} else {
			return false;
		}
	}
 
	public boolean isNaN() {
		return Float.isNaN( red ) || Float.isNaN( green ) || Float.isNaN( blue ) || Float.isNaN( alpha );
	}
	
	public void set( Color4f other ) {
		if( other != null ) {
			this.red = other.red;
			this.green = other.green;
			this.blue = other.blue;
			this.alpha = other.alpha;
		} else {
			setNaN();
		}
	}
	public void set( float red, float green, float blue, float alpha ) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public void setNaN() {
		red = green = blue = alpha = Float.NaN;
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
	
	public void interpolate( Color4f b, float portion ) {
		red += ( b.red - red ) * portion;
		green += ( b.green - green ) * portion;
		blue += ( b.blue - blue ) * portion;
		alpha += ( b.alpha - alpha ) * portion;
	}
	public void interpolate( Color4f a, Color4f b, float portion ) {
		red = a.red + ( b.red - a.red ) * portion;
		green = a.green + ( b.green - a.green ) * portion;
		blue = a.blue + ( b.blue - a.blue ) * portion;
		alpha = a.alpha + ( b.alpha - a.alpha ) * portion;
	}
}
