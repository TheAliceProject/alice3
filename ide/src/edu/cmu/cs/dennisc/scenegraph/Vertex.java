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

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public class Vertex implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public static final int FORMAT_POSITION = 1;
	public static final int FORMAT_NORMAL = 2;
	public static final int FORMAT_DIFFUSE_COLOR = 4;
	public static final int FORMAT_SPECULAR_HIGHLIGHT_COLOR = 8;
	public static final int FORMAT_TEXTURE_COORDINATE_0 = 16;

	public final edu.cmu.cs.dennisc.math.Point3 position = edu.cmu.cs.dennisc.math.Point3.createNaN();
	public final edu.cmu.cs.dennisc.math.Vector3f normal = edu.cmu.cs.dennisc.math.Vector3f.createNaN();
	public final edu.cmu.cs.dennisc.color.Color4f diffuseColor = edu.cmu.cs.dennisc.color.Color4f.createNaN();
	public final edu.cmu.cs.dennisc.color.Color4f specularHighlightColor = edu.cmu.cs.dennisc.color.Color4f.createNaN();
	public final edu.cmu.cs.dennisc.texture.TextureCoordinate2f textureCoordinate0 = edu.cmu.cs.dennisc.texture.TextureCoordinate2f.createNaN();

	public Vertex() {
	}
//	public Vertex( int format ) {
//		initializeToFormat( format );
//	}
	public Vertex( edu.cmu.cs.dennisc.math.Point3 position, edu.cmu.cs.dennisc.math.Vector3f normal, edu.cmu.cs.dennisc.color.Color4f diffuseColor, edu.cmu.cs.dennisc.color.Color4f specularHighlightColor,
			edu.cmu.cs.dennisc.texture.TextureCoordinate2f textureCoordinate0 ) {
		this.position.set( position );
		this.normal.set( normal );
		this.diffuseColor.set( diffuseColor );
		this.specularHighlightColor.set( specularHighlightColor );
		this.textureCoordinate0.set( textureCoordinate0 );
	}
	public Vertex( Vertex other ) {
		this( other.position, other.normal, other.diffuseColor, other.specularHighlightColor, other.textureCoordinate0 );
	}

	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		position.decode( binaryDecoder );
		normal.decode( binaryDecoder );
		diffuseColor.decode( binaryDecoder );
		specularHighlightColor.decode( binaryDecoder );
		textureCoordinate0.decode( binaryDecoder );
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		position.encode( binaryEncoder );
		normal.encode( binaryEncoder );
		diffuseColor.encode( binaryEncoder );
		specularHighlightColor.encode( binaryEncoder );
		textureCoordinate0.encode( binaryEncoder );
	}

	public static Vertex createXYZ( double x, double y, double z ) {
		Vertex vertex = new Vertex();
		vertex.position.set( x, y, z );
		return vertex;
	}
	public static Vertex createXYZUV( double x, double y, double z, float u, float v ) {
		Vertex vertex = new Vertex();
		vertex.position.set( x, y, z );
		vertex.textureCoordinate0.set( u, v );
		return vertex;
	}
	public static Vertex createXYZIJKUV( double x, double y, double z, float i, float j, float k, float u, float v ) {
		Vertex vertex = new Vertex();
		vertex.position.set( x, y, z );
		vertex.normal.set( i, j, k );
		vertex.textureCoordinate0.set( u, v );
		return vertex;
	}
	public static Vertex createXYZIJK( double x, double y, double z, float i, float j, float k ) {
		Vertex vertex = new Vertex();
		vertex.position.set( x, y, z );
		vertex.normal.set( i, j, k );
		return vertex;
	}

	public static Vertex createXYZRGBA( double x, double y, double z, float r, float g, float b, float a ) {
		Vertex vertex = new Vertex();
		vertex.position.set( x, y, z );
		vertex.diffuseColor.set( r, g, b, a );
		return vertex;
	}
	public static Vertex createXYZRGB( double x, double y, double z, float r, float g, float b ) {
		return createXYZRGBA( x, y, z, r, g, b, 1.0f );
	}

	//todo?
	//	@Override
	//	public synchronized Object clone() {
	//		try {
	//			//todo?
	//			//do i need to make new clones of position, etc?
	//			return super.clone();
	//		} catch( CloneNotSupportedException e ) {
	//			throw new InternalError();
	//		}
	//	}
	@Override
	public boolean equals( Object o ) {
		if( this == o ) {
			return true;
		} else {
			if( o instanceof Vertex ) {
				Vertex v = (Vertex)o;
				if( v.position.isNaN() ) {
					if( this.position.isNaN() ) {
						//pass
					} else {
						return false;
					}
				} else {
					if( !v.position.equals( this.position ) ) {
						return false;
					}
				}
				if( v.normal.isNaN() ) {
					if( this.normal.isNaN() ) {
						//pass
					} else {
						return false;
					}
				} else {
					if( !v.normal.equals( this.normal ) ) {
						return false;
					}
				}
				if( v.diffuseColor.isNaN() ) {
					if( this.diffuseColor.isNaN() ) {
						//pass
					} else {
						return false;
					}
				} else {
					if( !v.diffuseColor.equals( diffuseColor ) ) {
						return false;
					}
				}
				if( v.specularHighlightColor.isNaN() ) {
					if( this.specularHighlightColor.isNaN() ) {
						//pass
					} else {
						return false;
					}
				} else {
					if( !v.specularHighlightColor.equals( this.specularHighlightColor ) ) {
						return false;
					}
				}
				if( v.textureCoordinate0.isNaN() ) {
					if( this.textureCoordinate0.isNaN() ) {
						//pass
					} else {
						return false;
					}
				} else {
					if( !v.textureCoordinate0.equals( this.textureCoordinate0 ) ) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
	}

	@Deprecated
	public int getFormat() {
		int format = 0;
		if( position.isNaN() == false ) {
			format |= FORMAT_POSITION;
		}
		if( normal.isNaN() == false ) {
			format |= FORMAT_NORMAL;
		}
		if( diffuseColor.isNaN() == false ) {
			format |= FORMAT_DIFFUSE_COLOR;
		}
		if( specularHighlightColor.isNaN() == false ) {
			format |= FORMAT_SPECULAR_HIGHLIGHT_COLOR;
		}
		if( textureCoordinate0.isNaN() == false ) {
			format |= FORMAT_TEXTURE_COORDINATE_0;
		}
		return format;
	}
//	public void initializeToFormat( int format ) {
//		if( (format & FORMAT_POSITION) != 0 ) {
//			position = new edu.cmu.cs.dennisc.math.PointD3();
//		}
//		if( (format & FORMAT_NORMAL) != 0 ) {
//			normal = new edu.cmu.cs.dennisc.math.VectorF3();
//		}
//		if( (format & FORMAT_DIFFUSE_COLOR) != 0 ) {
//			diffuseColor = new edu.cmu.cs.dennisc.color.ColorF4();
//		}
//		if( (format & FORMAT_SPECULAR_HIGHLIGHT_COLOR) != 0 ) {
//			specularHighlightColor = new edu.cmu.cs.dennisc.color.ColorF4();
//		}
//		if( (format & FORMAT_TEXTURE_COORDINATE_0) != 0 ) {
//			textureCoordinate0 = new edu.cmu.cs.dennisc.texture.TextureCoordinateF2();
//		}
//	}
	public void transform( edu.cmu.cs.dennisc.math.AbstractMatrix4x4 m ) {
		if( position.isNaN() == false ) {
			m.transform( position );
		}
		if( normal.isNaN() == false ) {
			m.transform( normal );
		}
	}

	//todo: handle null better?
	public void set( Vertex other ) {
//		assert getFormat() == other.getFormat();
//		if( position != null ) {
//			position.set( other.position );
//		}
//		if( normal != null ) {
//			normal.set( other.normal );
//		}
//		if( diffuseColor != null ) {
//			diffuseColor.set( other.diffuseColor );
//		}
//		if( specularHighlightColor != null ) {
//			specularHighlightColor.set( other.specularHighlightColor );
//		}
//		if( textureCoordinate0 != null ) {
//			textureCoordinate0.set( other.textureCoordinate0 );
//		}
	}
	@Override
	public String toString() {
		return Vertex.class.getName() + "[position=" + position + ",normal=" + normal + ",diffuseColor=" + diffuseColor + ",specularHighlightColor=" + specularHighlightColor + ",textureCoordinate0=" + textureCoordinate0 + "]";
	}
}
