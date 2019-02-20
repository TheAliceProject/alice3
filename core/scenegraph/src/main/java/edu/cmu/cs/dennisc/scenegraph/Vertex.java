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

package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AbstractMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3f;
import edu.cmu.cs.dennisc.texture.TextureCoordinate2f;

/**
 * @author Dennis Cosgrove
 */
public final class Vertex implements BinaryEncodableAndDecodable {
	public static final int FORMAT_POSITION = 1;
	public static final int FORMAT_NORMAL = 2;
	public static final int FORMAT_DIFFUSE_COLOR = 4;
	public static final int FORMAT_SPECULAR_HIGHLIGHT_COLOR = 8;
	public static final int FORMAT_TEXTURE_COORDINATE_0 = 16;

	public final Point3 position;
	public final Vector3f normal;
	public final Color4f diffuseColor;
	public final Color4f specularHighlightColor;
	public final TextureCoordinate2f textureCoordinate0;

	public Vertex(
			Point3 position,
			Vector3f normal,
			Color4f diffuseColor,
			Color4f specularHighlightColor,
			TextureCoordinate2f textureCoordinate0 ) {
		this.position = position != null ? position : Point3.createNaN();
		this.normal = normal != null ? normal : Vector3f.createNaN();
		this.diffuseColor = diffuseColor != null ? diffuseColor : Color4f.createNaN();
		this.specularHighlightColor = specularHighlightColor != null ? specularHighlightColor : Color4f.createNaN();
		this.textureCoordinate0 = textureCoordinate0 != null ? textureCoordinate0 : TextureCoordinate2f.createNaN();
	}

	public Vertex( Vertex other ) {
		this( other.position, other.normal, other.diffuseColor, other.specularHighlightColor, other.textureCoordinate0 );
	}

	public Vertex( BinaryDecoder binaryDecoder ) {
		position = binaryDecoder.decodeBinaryEncodableAndDecodable();
		normal = binaryDecoder.decodeBinaryEncodableAndDecodable();
		diffuseColor = binaryDecoder.decodeBinaryEncodableAndDecodable();
		specularHighlightColor = binaryDecoder.decodeBinaryEncodableAndDecodable();
		textureCoordinate0 = binaryDecoder.decodeBinaryEncodableAndDecodable();
	}

	@Override
	public void encode( BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( position );
		binaryEncoder.encode( normal );
		binaryEncoder.encode( diffuseColor );
		binaryEncoder.encode( specularHighlightColor );
		binaryEncoder.encode( textureCoordinate0 );
	}

	public static Vertex createXYZ( Point3 xyz ) {
		return new Vertex(
				xyz,
				null,
				null,
				null,
				null
		//				edu.cmu.cs.dennisc.math.Vector3f.createNaN(),
		//				edu.cmu.cs.dennisc.color.Color4f.createNaN(),
		//				edu.cmu.cs.dennisc.color.Color4f.createNaN(),
		//				edu.cmu.cs.dennisc.texture.TextureCoordinate2f.createNaN()
		);
	}

	public static Vertex createXYZ( double x, double y, double z ) {
		return createXYZ( new Point3( x, y, z ) );
	}

	public static Vertex createXYZUV( Point3 xyz, TextureCoordinate2f uv ) {
		return new Vertex(
				xyz,
				null,
				null,
				null,
				uv );
	}

	public static Vertex createXYZUV( double x, double y, double z, float u, float v ) {
		return createXYZUV( new Point3( x, y, z ), new TextureCoordinate2f( u, v ) );
	}

	public static Vertex createXYZIJKUV( Point3 xyz, Vector3f ijk, TextureCoordinate2f uv ) {
		return new Vertex(
				xyz,
				ijk,
				null,
				null,
				uv );
	}

	public static Vertex createXYZIJKUV( double x, double y, double z, float i, float j, float k, float u, float v ) {
		return createXYZIJKUV( new Point3( x, y, z ), new Vector3f( i, j, k ), new TextureCoordinate2f( u, v ) );
	}

	public static Vertex createXYZIJK( Point3 xyz, Vector3f ijk ) {
		return new Vertex(
				xyz,
				ijk,
				null,
				null,
				null );
	}

	public static Vertex createXYZIJK( double x, double y, double z, float i, float j, float k ) {
		return createXYZIJK( new Point3( x, y, z ), new Vector3f( i, j, k ) );
	}

	public static Vertex createXYZRGBA( Point3 xyz, Color4f rgba ) {
		return new Vertex(
				xyz,
				null,
				rgba,
				null,
				null );
	}

	public static Vertex createXYZRGBA( double x, double y, double z, float r, float g, float b, float a ) {
		return createXYZRGBA( new Point3( x, y, z ), new Color4f( r, g, b, a ) );
	}

	public static Vertex createXYZRGB( double x, double y, double z, float r, float g, float b ) {
		return createXYZRGBA( x, y, z, r, g, b, 1.0f );
	}

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

	//todo: 
	//	@Override
	//	public int hashCode() {
	//	}

	@Deprecated
	public int getFormat() {
		int format = 0;
		if( ( position != null ) && ( position.isNaN() == false ) ) {
			format |= FORMAT_POSITION;
		}
		if( ( normal != null ) && ( normal.isNaN() == false ) ) {
			format |= FORMAT_NORMAL;
		}
		if( ( diffuseColor != null ) && ( diffuseColor.isNaN() == false ) ) {
			format |= FORMAT_DIFFUSE_COLOR;
		}
		if( ( specularHighlightColor != null ) && ( specularHighlightColor.isNaN() == false ) ) {
			format |= FORMAT_SPECULAR_HIGHLIGHT_COLOR;
		}
		if( ( textureCoordinate0 != null ) && ( textureCoordinate0.isNaN() == false ) ) {
			format |= FORMAT_TEXTURE_COORDINATE_0;
		}
		return format;
	}

	public void transform( AbstractMatrix4x4 m ) {
		if( position.isNaN() == false ) {
			m.transform( position );
		}
		if( normal.isNaN() == false ) {
			m.transform( normal );
		}
	}

	@Override
	public String toString() {
		return Vertex.class.getName() + "[position=" + position + ",normal=" + normal + ",diffuseColor=" + diffuseColor + ",specularHighlightColor=" + specularHighlightColor + ",textureCoordinate0=" + textureCoordinate0 + "]";
	}
}
