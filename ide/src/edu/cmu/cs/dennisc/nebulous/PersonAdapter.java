/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */
package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public class PersonAdapter extends GenericModelAdapter< Person > {
	@Override
	protected void renderGeometry( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc ) {
		super.renderGeometry( rc );
		//if debug
	}
//	@Deprecated
//	public void debugRender( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc ) {
//		if( SystemUtilities.isPropertyTrue( IDE.DEBUG_PROPERTY_KEY ) ) {
//			final double UNIT_LENGTH = 0.3;
//
//			for( org.lookingglassandalice.storytelling.resources.JointId joint : org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.values() ) {
//				double[] nativeTransform = new double[ 12 ];
//				getAbsoluteTransformationForBodyPartNamed( nativeTransform, joint );
//				//                nativeTransform[9] /= .015;
//				//                nativeTransform[10] /= .015;
//				//                nativeTransform[11] /= .015;
//				//                nativeTransform[9] *= SCALE;
//				//                nativeTransform[10] *= SCALE;
//				//                nativeTransform[11] *= SCALE;
//				Matrix4x4 transform = new Matrix4x4( nativeTransform );
//				rc.gl.glPushMatrix();
//				java.nio.DoubleBuffer m_absBuf = java.nio.DoubleBuffer.wrap( transform.getAsColumnMajorArray16() );
//				rc.gl.glMultMatrixd( m_absBuf );
//				rc.gl.glEnable( GL.GL_NORMALIZE );
//				{
//					rc.gl.glDisable( GL.GL_LIGHTING );
//
//					rc.gl.glBegin( GL.GL_LINES );
//
//					rc.gl.glColor3f( 1.0f, 0.0f, 0.0f );
//					rc.gl.glVertex3d( 0, 0, 0 );
//					rc.gl.glVertex3d( UNIT_LENGTH, 0, 0 );
//
//					rc.gl.glColor3f( 0.0f, 1.0f, 0.0f );
//					rc.gl.glVertex3d( 0, 0, 0 );
//					rc.gl.glVertex3d( 0, UNIT_LENGTH, 0 );
//
//					rc.gl.glColor3f( 0.0f, 0.0f, 1.0f );
//					rc.gl.glVertex3d( 0, 0, 0 );
//					rc.gl.glVertex3d( 0, 0, UNIT_LENGTH );
//
//					//                    rc.gl.glColor3f( 1.0f, 1.0f, 1.0f );
//					//                    rc.gl.glVertex3d( 0, 0, 0 );
//					//                    rc.gl.glVertex3d( 0, 0, -2*UNIT_LENGTH );
//
//					rc.gl.glEnd();
//
//					rc.gl.glEnable( GL.GL_LIGHTING );
//				}
//				rc.gl.glPopMatrix();
//			}
//
//		}
//	}
}
