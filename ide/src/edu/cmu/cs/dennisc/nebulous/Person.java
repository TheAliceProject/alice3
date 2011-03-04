/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */

package edu.cmu.cs.dennisc.nebulous;

import javax.media.opengl.GL;
import org.alice.ide.IDE;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Matrix4x4;
import edu.cmu.cs.dennisc.print.PrintUtilities;

/**
 * @author Dennis Cosgrove
 */
public class Person extends Model {
	static {
		edu.cmu.cs.dennisc.lookingglass.opengl.AdapterFactory.register( Person.class, PersonAdapter.class );
	}

	public Person( Object o ) throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
		initialize( o );
	}
	private native void initialize( Object o );
	public native void setGender( Object o );
	public native void setOutfit( Object o );
	public native void setSkinTone( Object o );
	public native void setFitnessLevel( Object o );
	public native void setEyeColor( Object o );
	public native void setHair( Object o );

	public native void getLocalTransformationForBodyPartNamed( double[] transformOut, org.lookingglassandalice.storytelling.resources.JointId name );
	public native void setLocalTransformationForBodyPartNamed( org.lookingglassandalice.storytelling.resources.JointId name, double[] transformIn );
	public native void getAbsoluteTransformationForBodyPartNamed( double[] transformOut, org.lookingglassandalice.storytelling.resources.JointId name );
	public native void setAbsoluteTransformationForBodyPartNamed( org.lookingglassandalice.storytelling.resources.JointId name, double[] transformIn );
	public AffineMatrix4x4 getLocalTransformationForJoint( org.lookingglassandalice.storytelling.resources.JointId joint ) {
		double[] nativeTransform = new double[ 12 ];
		getLocalTransformationForBodyPartNamed( nativeTransform, joint );
		Matrix4x4 m = new Matrix4x4( nativeTransform );
		return new AffineMatrix4x4( m );
	}

	public void setLocalTransformationForJoint( org.lookingglassandalice.storytelling.resources.JointId joint, AffineMatrix4x4 localTrans ) {
		//localTrans.getAsColumnMajorArray16();
		
		double[] nativeTransfom = new double[ 12 ];
		nativeTransfom[ 0 ] = localTrans.orientation.right.x;
		nativeTransfom[ 1 ] = localTrans.orientation.right.y;
		nativeTransfom[ 2 ] = localTrans.orientation.right.z;
		nativeTransfom[ 3 ] = localTrans.orientation.up.x;
		nativeTransfom[ 4 ] = localTrans.orientation.up.y;
		nativeTransfom[ 5 ] = localTrans.orientation.up.z;
		nativeTransfom[ 6 ] = localTrans.orientation.backward.x;
		nativeTransfom[ 7 ] = localTrans.orientation.backward.y;
		nativeTransfom[ 8 ] = localTrans.orientation.backward.z;
		nativeTransfom[ 9 ] = localTrans.translation.x;
		nativeTransfom[ 10 ] = localTrans.translation.y;
		nativeTransfom[ 11 ] = localTrans.translation.z;
		setLocalTransformationForBodyPartNamed( joint, nativeTransfom );
	}

	@Deprecated
	public void debugRender( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc ) {
		if( SystemUtilities.isPropertyTrue( IDE.DEBUG_PROPERTY_KEY ) ) {
			final double UNIT_LENGTH = 0.3;

			for( org.lookingglassandalice.storytelling.resources.JointId joint : org.lookingglassandalice.storytelling.resources.PersonResource.PersonJointId.values() ) {
				double[] nativeTransform = new double[ 12 ];
				getAbsoluteTransformationForBodyPartNamed( nativeTransform, joint );
				//                nativeTransform[9] /= .015;
				//                nativeTransform[10] /= .015;
				//                nativeTransform[11] /= .015;
				//                nativeTransform[9] *= SCALE;
				//                nativeTransform[10] *= SCALE;
				//                nativeTransform[11] *= SCALE;
				Matrix4x4 transform = new Matrix4x4( nativeTransform );
				rc.gl.glPushMatrix();
				java.nio.DoubleBuffer m_absBuf = java.nio.DoubleBuffer.wrap( transform.getAsColumnMajorArray16() );
				rc.gl.glMultMatrixd( m_absBuf );
				rc.gl.glEnable( GL.GL_NORMALIZE );
				{
					rc.gl.glDisable( GL.GL_LIGHTING );

					rc.gl.glBegin( GL.GL_LINES );

					rc.gl.glColor3f( 1.0f, 0.0f, 0.0f );
					rc.gl.glVertex3d( 0, 0, 0 );
					rc.gl.glVertex3d( UNIT_LENGTH, 0, 0 );

					rc.gl.glColor3f( 0.0f, 1.0f, 0.0f );
					rc.gl.glVertex3d( 0, 0, 0 );
					rc.gl.glVertex3d( 0, UNIT_LENGTH, 0 );

					rc.gl.glColor3f( 0.0f, 0.0f, 1.0f );
					rc.gl.glVertex3d( 0, 0, 0 );
					rc.gl.glVertex3d( 0, 0, UNIT_LENGTH );

					//                    rc.gl.glColor3f( 1.0f, 1.0f, 1.0f );
					//                    rc.gl.glVertex3d( 0, 0, 0 );
					//                    rc.gl.glVertex3d( 0, 0, -2*UNIT_LENGTH );

					rc.gl.glEnd();

					rc.gl.glEnable( GL.GL_LIGHTING );
				}
				rc.gl.glPopMatrix();
			}

		}
	}

}
