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

import static javax.media.opengl.GL.GL_CLIP_PLANE0;

/**
 * @author Dennis Cosgrove
 */
public class PlanarReflectorAdapter extends VisualAdapter<edu.cmu.cs.dennisc.scenegraph.PlanarReflector> {
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_geometryTransformation = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN();

	private void updateGeometryTransformation() {
		edu.cmu.cs.dennisc.scenegraph.Geometry[] sgGeometries = m_element.geometries.getValue();
		if( ( sgGeometries != null ) && ( sgGeometries.length > 0 ) ) {
			edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry = sgGeometries[ 0 ];

			sgGeometry.getPlane( m_geometryTransformation );
			//			edu.cmu.cs.dennisc.math.PointD3 point0;
			//			edu.cmu.cs.dennisc.math.PointD3 point1;
			//			edu.cmu.cs.dennisc.math.VectorF3 normal;
			//			if( sgGeometry instanceof edu.cmu.cs.dennisc.scenegraph.Mesh ) {
			//				edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh = (edu.cmu.cs.dennisc.scenegraph.Mesh)sgGeometry;
			//				edu.cmu.cs.dennisc.math.PointD3[] points = sgMesh.points.getValue();
			//				edu.cmu.cs.dennisc.math.VectorF3[] normals = sgMesh.normals.getValue();
			//				assert points.length >= 2;
			//				assert normals.length >= 1;
			//				
			//				point0 = points[ 0 ];
			//				point1 = points[ 1 ];
			//				normal = normals[ 0 ];
			//			} else if( sgGeometry instanceof edu.cmu.cs.dennisc.scenegraph.VertexGeometry ) {
			//				edu.cmu.cs.dennisc.scenegraph.VertexGeometry sgVertexGeometry = (edu.cmu.cs.dennisc.scenegraph.VertexGeometry)sgGeometry;
			//				edu.cmu.cs.dennisc.scenegraph.Vertex[] vertices = sgVertexGeometry.vertices.getValue();
			//				assert vertices.length >= 2;
			//				point0 = vertices[ 0 ].position;
			//				point1 = vertices[ 1 ].position;
			//				normal = vertices[ 0 ].normal;
			//			} else {
			//				throw new RuntimeException( "todo" );
			//			}
			//			
			//
			//			edu.cmu.cs.dennisc.math.VectorD3 forward = new edu.cmu.cs.dennisc.math.VectorD3();
			//			edu.cmu.cs.dennisc.math.VectorD3 upGuide = new edu.cmu.cs.dennisc.math.VectorD3();
			//			edu.cmu.cs.dennisc.math.PointD3 translation = new edu.cmu.cs.dennisc.math.PointD3();
			//
			//			forward.set( normal.x, normal.y, normal.z );
			//			forward.normalize();
			//			forward.negate();
			//			
			//			upGuide.set( point0 );
			//			upGuide.sub( point1 );
			//			upGuide.normalize();
			//			
			//			translation.set( point0 );
			//
			//			edu.cmu.cs.dennisc.math.MatrixD3x3 axes = LinearAlgebra.newMatrix3d( forward, upGuide );
			//			
			//			m_geometryTransformation.setRow( 0, axes.right.x, axes.up.x, axes.backward.x, translation.x );
			//			m_geometryTransformation.setRow( 1, axes.right.y, axes.up.y, axes.backward.y, translation.y );
			//			m_geometryTransformation.setRow( 2, axes.right.z, axes.up.z, axes.backward.z, translation.z );
			//			m_geometryTransformation.setRow( 3, 0       , 0       , 0       , 1             );			
		}
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		super.propertyChanged( property );
		if( property == m_element.geometries ) {
			updateGeometryTransformation();
		}
	}

	@Override
	public void setup( RenderContext rc ) {
		//pass
	}

	@Override
	public void renderAlphaBlended( RenderContext rc ) {
		//pass
	}

	@Override
	public void renderOpaque( RenderContext rc ) {
		//pass
	}

	//	@Override
	//	public void pick( PickContext pc, PickParameters pickParameters ) {
	//		m_geometryAdapter.pick( pc, pickParameters.isSubElementRequired() );
	//	}

	public boolean isFacing( AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = m_element.getTransformation( cameraAdapter.m_element );
		return m.orientation.backward.z > 0;
	}

	private double[] m_reflection = new double[ 16 ];
	private double[] m_equation = new double[ 4 ];
	private java.nio.DoubleBuffer m_reflectionBuffer = java.nio.DoubleBuffer.wrap( m_reflection );
	private java.nio.DoubleBuffer m_equationBuffer = java.nio.DoubleBuffer.wrap( m_equation );

	public synchronized void applyReflection( RenderContext rc ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = m_element.getAbsoluteTransformation();
		m.multiply( m_geometryTransformation );

		edu.cmu.cs.dennisc.math.Plane plane = edu.cmu.cs.dennisc.math.Plane.createInstance( m );
		plane.getEquation( m_equation );
		rc.gl.glClipPlane( GL_CLIP_PLANE0, m_equationBuffer );

		edu.cmu.cs.dennisc.math.Point3 p = m.translation;
		edu.cmu.cs.dennisc.math.Vector3 v = new edu.cmu.cs.dennisc.math.Vector3( -m.orientation.backward.x, -m.orientation.backward.y, -m.orientation.backward.z );

		edu.cmu.cs.dennisc.math.Matrix4x4 r = new edu.cmu.cs.dennisc.math.Matrix4x4();

		double p_dot_v = ( p.x * v.x ) + ( p.y * v.y ) + ( p.z * v.z );

		r.right.x = 1 - ( 2 * v.x * v.x );
		r.up.x = 0 - ( 2 * v.x * v.y );
		r.backward.x = 0 - ( 2 * v.x * v.z );
		r.translation.x = 2 * v.x * p_dot_v;
		r.right.y = 0 - ( 2 * v.y * v.x );
		r.up.y = 1 - ( 2 * v.y * v.y );
		r.backward.y = 0 - ( 2 * v.y * v.z );
		r.translation.y = 2 * v.y * p_dot_v;
		r.right.z = 0 - ( 2 * v.z * v.x );
		r.up.z = 0 - ( 2 * v.z * v.y );
		r.backward.z = 1 - ( 2 * v.z * v.z );
		r.translation.z = 2 * v.z * p_dot_v;
		r.right.w = 0;
		r.up.w = 0;
		r.backward.w = 0;
		r.translation.w = 1;

		r.getAsColumnMajorArray16( m_reflection );
		rc.gl.glMultMatrixd( m_reflectionBuffer );
	}

	public void renderStencil( RenderContext rc ) {
		rc.gl.glPushMatrix();
		synchronized( this ) {
			rc.gl.glMultMatrixd( accessAbsoluteTransformationAsBuffer() );
		}
		actuallyRender( rc );
		rc.gl.glPopMatrix();
	}
}
