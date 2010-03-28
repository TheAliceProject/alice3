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
	@Override
    public boolean isAlphaBlended() {
    	return false;
    }
	private void renderVertex( RenderContext rc, int xyzIndex, int ijkIndex, int uvIndex ) {
		rc.gl.glTexCoord2f( this.uvs[ uvIndex*2+0 ], this.uvs[ uvIndex*2+1 ] );
		rc.gl.glNormal3f( this.ijks[ ijkIndex*3+0 ], this.ijks[ ijkIndex*3+1 ], this.ijks[ ijkIndex*3+2 ] );
		rc.gl.glVertex3d( this.xyzs[ xyzIndex*3+0 ], this.xyzs[ xyzIndex*3+1 ], this.xyzs[ xyzIndex*3+2 ] );
	}
	@Override
	protected void renderGeometry( RenderContext rc ) {
		final int N3 = this.xyzTriangleIndices != null ? this.xyzTriangleIndices.length : 0;
		if( N3 > 0 ) {
			rc.gl.glBegin( javax.media.opengl.GL.GL_TRIANGLES );
			try {
				for( int i=0; i<N3; i++ ) {
					int xyzIndex = this.xyzTriangleIndices[ i ];
					int ijkIndex = this.ijkTriangleIndices != null ? this.ijkTriangleIndices[ i ] : xyzIndex;
					int uvIndex = this.uvTriangleIndices != null ? this.uvTriangleIndices[ i ] : xyzIndex;
					renderVertex( rc, xyzIndex, ijkIndex, uvIndex ); 
				}
			} finally {
				rc.gl.glEnd();
			}
		}
		final int N4 = this.xyzQuadrangleIndices != null ? this.xyzQuadrangleIndices.length : 0;
		if( N4 > 0 ) {
			rc.gl.glBegin( javax.media.opengl.GL.GL_QUADS );
			try {
				for( int i=0; i<N4; i++ ) {
					int xyzIndex = this.xyzQuadrangleIndices[ i ];
					int ijkIndex = this.ijkQuadrangleIndices != null ? this.ijkQuadrangleIndices[ i ] : xyzIndex;
					int uvIndex = this.uvQuadrangleIndices != null ? this.uvQuadrangleIndices[ i ] : xyzIndex;
					renderVertex( rc, xyzIndex, ijkIndex, uvIndex ); 
				}
			} finally {
				rc.gl.glEnd();
			}
		}
	}
	@Override
	protected void pickGeometry( PickContext pc, boolean isSubElementRequired ) {
		throw new RuntimeException( "todo" );
	}    
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.xyzs ) {
			this.xyzs = m_element.xyzs.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.ijks ) {
			this.ijks = m_element.ijks.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.uvs ) {
			this.uvs = m_element.uvs.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.xyzTriangleIndices ) {
            this.xyzTriangleIndices = m_element.xyzTriangleIndices.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.ijkTriangleIndices ) {
            this.ijkTriangleIndices = m_element.ijkTriangleIndices.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.uvTriangleIndices ) {
            this.uvTriangleIndices = m_element.uvTriangleIndices.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.xyzQuadrangleIndices ) {
            this.xyzQuadrangleIndices = m_element.xyzQuadrangleIndices.getValue();
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
