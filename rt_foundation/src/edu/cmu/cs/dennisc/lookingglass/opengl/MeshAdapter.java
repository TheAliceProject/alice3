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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
public class MeshAdapter extends GeometryAdapter< edu.cmu.cs.dennisc.scenegraph.Mesh > {
	private double[] xyzs;
	private float[] ijks;
	private float[] uvs;
	private short[] xyzTriangleIndices;
	private short[] ijkTriangleIndices;
	private short[] uvTriangleIndices;
	private short[] xyzQuadrangleIndices;
	private short[] ijkQuadrangleIndices;
	private short[] uvQuadrangleIndices;

	private java.nio.DoubleBuffer xyzBuffer;
	private java.nio.FloatBuffer ijkBuffer;
	private java.nio.FloatBuffer uvBuffer;
	private java.nio.ShortBuffer triangleIndexBuffer;
	private java.nio.ShortBuffer quadrangleIndexBuffer;

	@Override
    public boolean isAlphaBlended() {
    	return false;
    }
	private void renderVertex( javax.media.opengl.GL gl, int xyzIndex, int ijkIndex, int uvIndex ) {
		gl.glTexCoord2f( this.uvs[ uvIndex*2+0 ], this.uvs[ uvIndex*2+1 ] );
		gl.glNormal3f( this.ijks[ ijkIndex*3+0 ], this.ijks[ ijkIndex*3+1 ], this.ijks[ ijkIndex*3+2 ] );
		gl.glVertex3d( this.xyzs[ xyzIndex*3+0 ], this.xyzs[ xyzIndex*3+1 ], this.xyzs[ xyzIndex*3+2 ] );
	}
	
	private void glGeometry( javax.media.opengl.GL gl ) {
		final int N3 = this.xyzTriangleIndices != null ? this.xyzTriangleIndices.length : 0;
		final int N4 = this.xyzQuadrangleIndices != null ? this.xyzQuadrangleIndices.length : 0;
		if( N3 > 0 || N4 > 0 ) {
			final boolean isArrayRenderingDesired = false;
			if( isArrayRenderingDesired==false || this.ijkTriangleIndices != null || this.uvTriangleIndices != null || this.ijkQuadrangleIndices != null || this.uvQuadrangleIndices != null ) {
				if( N3 > 0 ) {
					gl.glBegin( javax.media.opengl.GL.GL_TRIANGLES );
					try {
						for( int i=0; i<N3; i++ ) {
							int xyzIndex = this.xyzTriangleIndices[ i ];
							int ijkIndex = this.ijkTriangleIndices != null ? this.ijkTriangleIndices[ i ] : xyzIndex;
							int uvIndex = this.uvTriangleIndices != null ? this.uvTriangleIndices[ i ] : xyzIndex;
							renderVertex( gl, xyzIndex, ijkIndex, uvIndex ); 
						}
					} finally {
						gl.glEnd();
					}
				}
				if( N4 > 0 ) {
					gl.glBegin( javax.media.opengl.GL.GL_QUADS );
					try {
						for( int i=0; i<N4; i++ ) {
							int xyzIndex = this.xyzQuadrangleIndices[ i ];
							int ijkIndex = this.ijkQuadrangleIndices != null ? this.ijkQuadrangleIndices[ i ] : xyzIndex;
							int uvIndex = this.uvQuadrangleIndices != null ? this.uvQuadrangleIndices[ i ] : xyzIndex;
							renderVertex( gl, xyzIndex, ijkIndex, uvIndex ); 
						}
					} finally {
						gl.glEnd();
					}
				}
			} else {
				if( this.xyzBuffer != null ) {
					//pass
				} else {
					this.xyzBuffer = com.sun.opengl.util.BufferUtil.newDoubleBuffer( xyzs.length );
					this.xyzBuffer.put( this.xyzs );
					this.xyzBuffer.rewind();
				}
				if( this.ijkBuffer != null ) {
					//pass
				} else {
					this.ijkBuffer = com.sun.opengl.util.BufferUtil.newFloatBuffer( ijks.length );
					this.ijkBuffer.put( this.ijks );
					this.ijkBuffer.rewind();
				}
				if( this.uvBuffer != null ) {
					//pass
				} else {
					this.uvBuffer = com.sun.opengl.util.BufferUtil.newFloatBuffer( uvs.length );
					this.uvBuffer.put( this.uvs );
					this.uvBuffer.rewind();
				}
				gl.glEnableClientState(javax.media.opengl.GL.GL_VERTEX_ARRAY);
				gl.glEnableClientState(javax.media.opengl.GL.GL_NORMAL_ARRAY);
				gl.glEnableClientState(javax.media.opengl.GL.GL_TEXTURE_COORD_ARRAY);
				try {
					gl.glVertexPointer( 3, javax.media.opengl.GL.GL_DOUBLE, 0, this.xyzBuffer );
					gl.glNormalPointer( javax.media.opengl.GL.GL_FLOAT, 0, this.ijkBuffer );
					gl.glTexCoordPointer( 2, javax.media.opengl.GL.GL_FLOAT, 0, this.uvBuffer );
					if( N3 > 0 ) {
						gl.glBegin(javax.media.opengl.GL.GL_TRIANGLES);
						for( int i=0; i<this.xyzTriangleIndices.length; i++ ) {
							gl.glArrayElement( i );
						}
						gl.glEnd();
//						if( this.triangleIndexBuffer != null ) {
//							//pass
//						} else {
//							this.triangleIndexBuffer = com.sun.opengl.util.BufferUtil.newShortBuffer( N3 );
//							this.triangleIndexBuffer.put( this.xyzTriangleIndices );
//							this.triangleIndexBuffer.rewind();
//						}
//						gl.glDrawElements( javax.media.opengl.GL.GL_TRIANGLES, N3/3, javax.media.opengl.GL.GL_SHORT, this.triangleIndexBuffer );
					}
					if( N4 > 0 ) {
						gl.glBegin(javax.media.opengl.GL.GL_QUADS);
						for( int i=0; i<this.xyzQuadrangleIndices.length; i++ ) {
							gl.glArrayElement( i );
						}
						gl.glEnd();
//						if( this.quadrangleIndexBuffer != null ) {
//							//pass
//						} else {
//							this.quadrangleIndexBuffer = com.sun.opengl.util.BufferUtil.newShortBuffer( N4 );
//							this.quadrangleIndexBuffer.put( this.xyzQuadrangleIndices );
//							this.quadrangleIndexBuffer.rewind();
//						}
						//gl.glDrawElements( javax.media.opengl.GL.GL_QUADS, N4/4, javax.media.opengl.GL.GL_SHORT, this.quadrangleIndexBuffer );
					}
				} finally {		
					gl.glDisableClientState(javax.media.opengl.GL.GL_TEXTURE_COORD_ARRAY);
					gl.glDisableClientState(javax.media.opengl.GL.GL_NORMAL_ARRAY);
					gl.glDisableClientState(javax.media.opengl.GL.GL_VERTEX_ARRAY);
				}
			}
		}
	}
	@Override
	protected void renderGeometry( RenderContext rc ) {
		glGeometry( rc.gl );
	}
	@Override
	protected void pickGeometry( PickContext pc, boolean isSubElementRequired ) {
		pc.gl.glPushName( -1 );
		if( isSubElementRequired ) {
			throw new RuntimeException( "todo" );
		} else {
			glGeometry( pc.gl );
		}
		pc.gl.glPopName();
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.xyzs ) {
			this.xyzs = m_element.xyzs.getValue();
			this.xyzBuffer = null;
			setIsGeometryChanged( true );
		} else if( property == m_element.ijks ) {
			this.ijks = m_element.ijks.getValue();
			this.ijkBuffer = null;
			setIsGeometryChanged( true );
		} else if( property == m_element.uvs ) {
			this.uvs = m_element.uvs.getValue();
			this.uvBuffer = null;
			setIsGeometryChanged( true );
		} else if( property == m_element.xyzTriangleIndices ) {
            this.xyzTriangleIndices = m_element.xyzTriangleIndices.getValue();
			this.triangleIndexBuffer = null;
			setIsGeometryChanged( true );
		} else if( property == m_element.ijkTriangleIndices ) {
            this.ijkTriangleIndices = m_element.ijkTriangleIndices.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.uvTriangleIndices ) {
            this.uvTriangleIndices = m_element.uvTriangleIndices.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.xyzQuadrangleIndices ) {
            this.xyzQuadrangleIndices = m_element.xyzQuadrangleIndices.getValue();
			this.quadrangleIndexBuffer = null;
			setIsGeometryChanged( true );
		} else if( property == m_element.ijkQuadrangleIndices ) {
            this.ijkQuadrangleIndices = m_element.ijkQuadrangleIndices.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.uvQuadrangleIndices ) {
            this.uvQuadrangleIndices = m_element.uvQuadrangleIndices.getValue();
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}
	@Override
	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource(edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, int subElement) {
		rv.setNaN();
		return rv;
	}
}
