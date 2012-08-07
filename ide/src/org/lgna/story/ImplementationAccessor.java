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

package org.lgna.story;


/**
 * @author Dennis Cosgrove
 */
public class ImplementationAccessor {
	private ImplementationAccessor() {
		throw new AssertionError();
	}
	public static <T extends org.lgna.story.implementation.EntityImp> T getImplementation( SThing entity ) {
		return (T)entity.getImplementation();
	}
	public static org.lgna.story.implementation.ProgramImp getImplementation( SProgram program ) {
		return program.getImplementation();
	}
	public static edu.cmu.cs.dennisc.math.Point3 getPoint3( Position position ) {
		return position.getInternal();
	}
	public static edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getOrthogonalMatrix3x3( Orientation orientation ) {
		return orientation.getInternal();
	}
	public static edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAffineMatrix4x4( VantagePoint vantagePoint ) {
		return vantagePoint.getInternal();
	}
	public static edu.cmu.cs.dennisc.color.Color4f getColor4f( Color color ) {
		return color.getInternal();
	}
	
	
	public static Color createColor( edu.cmu.cs.dennisc.color.Color4f internal ) {
		return Color.createInstance( internal );
	}
	public static Color createInterpolation( Color a, Color b, float portion ) {
		return Color.createInstance( 
				edu.cmu.cs.dennisc.color.Color4f.createInterpolation( a.getInternal(), b.getInternal(), portion )
		);
	}
	
	public static Position createPosition( edu.cmu.cs.dennisc.math.Point3 xyz ) {
		return Position.createInstance( xyz );
	}
	public static Orientation createOrientation( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m ) {
		return Orientation.createInstance( m );
	}
	public static VantagePoint createVantagePoint( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
		return VantagePoint.createInstance( m );
	}
	
	public static Key getKeyFromKeyCode( int keyCode ) {
		return Key.getInstanceFromKeyCode( keyCode );
	}
	public static int getKeyCodeFromKey( Key key ) {
		if( key != null ) {
			return key.getInternal();
		} else {
			return -1;
		}
	}

	public static edu.cmu.cs.dennisc.color.Color4f getColor4f( Paint paint, edu.cmu.cs.dennisc.color.Color4f defaultValue ) {
		if( paint instanceof org.lgna.story.Color ) {
			return getColor4f( (org.lgna.story.Color)paint );
		} else {
			return defaultValue;
		}
	}
	private static final java.util.Map< ImagePaint, edu.cmu.cs.dennisc.texture.BufferedImageTexture > mapImagePaintToTexture = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static edu.cmu.cs.dennisc.texture.Texture getTexture( Paint paint, edu.cmu.cs.dennisc.texture.Texture defaultValue ) {
		if( paint instanceof org.lgna.story.ImageSource ) {
			org.lgna.story.ImageSource imageSource = (org.lgna.story.ImageSource)paint;
			org.lgna.common.resources.ImageResource imageResource = imageSource.getImageResource();
			if( imageResource != null ) {
				return edu.cmu.cs.dennisc.texture.TextureFactory.getTexture( imageResource, true );
			} else {
				return null;
			}
		} else if( paint instanceof org.lgna.story.ImagePaint ) {
			org.lgna.story.ImagePaint imagePaint = (org.lgna.story.ImagePaint)paint;
			edu.cmu.cs.dennisc.texture.BufferedImageTexture rv = mapImagePaintToTexture.get( imagePaint );
			if( rv != null ) {
				//pass
			} else {
				rv = new edu.cmu.cs.dennisc.texture.BufferedImageTexture();
				try {
					rv.setBufferedImage( javax.imageio.ImageIO.read( imagePaint.getResource() ) );
				} catch( java.io.IOException ioe ) {
					throw new RuntimeException( ioe );
				}
				rv.setMipMappingDesired( true );
				mapImagePaintToTexture.put( imagePaint, rv );
			}
			return rv;
		} else if (paint instanceof edu.cmu.cs.dennisc.nebulous.NebulousPaint) {
			edu.cmu.cs.dennisc.nebulous.NebulousPaint nPaint = (edu.cmu.cs.dennisc.nebulous.NebulousPaint)paint;
			edu.cmu.cs.dennisc.nebulous.NebulousTexture nTexture = nPaint.getTexture();
			nTexture.setMipMappingDesired(true);
			return nTexture;
		}
		else {
			return defaultValue;
		}
	}
	
	public static Object getKeyedArgumentValue( Object argumentValue ) {
		try {
			if( argumentValue != null ) {
				Class<?> cls = argumentValue.getClass();
				java.lang.reflect.Method mthd = cls.getDeclaredMethod( "getValue", Object[].class );
				Object array = new Object[] { argumentValue };
				return mthd.invoke( null, array );
			} else {
				return null;
			}
		} catch( Throwable t ) {
			//t.printStackTrace();
			return argumentValue;
		}
	}
}
