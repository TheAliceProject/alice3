/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */

package edu.cmu.cs.dennisc.nebulous;

import javax.media.opengl.GL;

import org.alice.apis.stage.JointIdentifier;
import org.alice.ide.IDE;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Matrix4x4;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.TransformationAffect;

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
	
	public native void getLocalTransformationForBodyPartNamed(double[] transformOut, String name);
	public native void setLocalTransformationForBodyPartNamed(String name, double[] transformIn);
	public native void getAbsoluteTransformationForBodyPartNamed(double[] transformOut, String name);
    public native void setAbsoluteTransformationForBodyPartNamed(String name, double[] transformIn);
	public native void getBoneAxis(double[] axisOut, String name);
	public native String getParentForBodyPartNamed(String partName);
	public native String getChildForBodyPartNamed(String partName);

	public AffineMatrix4x4 getLocalTransformationForJoint(JointIdentifier joint)
	{
	    double[] nativeTransform = new double[12];
        getLocalTransformationForBodyPartNamed(nativeTransform, joint.getNameKey());
        Matrix4x4 m = new Matrix4x4(nativeTransform);
        return new AffineMatrix4x4(m);
	}
	
	public void setLocalTransformationForJoint(JointIdentifier joint, AffineMatrix4x4 localTrans)
	{
	    double[] nativeTransfom = new double[12];
	    nativeTransfom[0] = localTrans.orientation.right.x;
	    nativeTransfom[1] = localTrans.orientation.right.y;
	    nativeTransfom[2] = localTrans.orientation.right.z;
	    nativeTransfom[3] = localTrans.orientation.up.x;
        nativeTransfom[4] = localTrans.orientation.up.y;
        nativeTransfom[5] = localTrans.orientation.up.z;
        nativeTransfom[6] = localTrans.orientation.backward.x;
        nativeTransfom[7] = localTrans.orientation.backward.y;
        nativeTransfom[8] = localTrans.orientation.backward.z;
        nativeTransfom[9] = localTrans.translation.x;
        nativeTransfom[10] = localTrans.translation.y;
        nativeTransfom[11] = localTrans.translation.z;
        setLocalTransformationForBodyPartNamed(joint.getNameKey(), nativeTransfom);
	}
	
	public boolean hasJoint(JointIdentifier joint)
	{
	    return true;
	}
    
    public void applyRotationToJointAboutArbitraryAxisInRadians( JointIdentifier joint, edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians) {
        if( axis.isPositiveXAxis() ) {
            applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutXAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( angleInRadians ) ));
        } else if( axis.isNegativeXAxis() ) {
            applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutXAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( -angleInRadians ) ));
        } else if( axis.isPositiveYAxis() ) {
            applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutYAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( angleInRadians ) ));
        } else if( axis.isNegativeYAxis() ) {
            applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutYAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( -angleInRadians ) ));
        } else if( axis.isPositiveZAxis() ) {
            applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutZAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( angleInRadians ) ));
        } else if( axis.isNegativeZAxis() ) {
            applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutZAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( -angleInRadians ) ));
        } else {
            applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createOrientation( new edu.cmu.cs.dennisc.math.AxisRotation( axis, new edu.cmu.cs.dennisc.math.AngleInRadians( angleInRadians ) ) ));
        }
    }
	
	private void applyTransformationToJoint( JointIdentifier joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4 transformation ) {
            edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = getLocalTransformationForJoint(joint);
            m.setToMultiplication( m, transformation );
            setLocalTransformationForJoint( joint, m );
    }
	
	public void debugRender(edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc)
	{
	    if (SystemUtilities.isPropertyTrue(IDE.DEBUG_PROPERTY_KEY))
        {
            final double UNIT_LENGTH = 0.3;
            
            for (JointIdentifier joint : JointIdentifier.values())
            {
                double[] nativeTransform = new double[12];
                getAbsoluteTransformationForBodyPartNamed(nativeTransform, joint.getNameKey());
//                nativeTransform[9] /= .015;
//                nativeTransform[10] /= .015;
//                nativeTransform[11] /= .015;
//                nativeTransform[9] *= SCALE;
//                nativeTransform[10] *= SCALE;
//                nativeTransform[11] *= SCALE;
                Matrix4x4 transform = new Matrix4x4(nativeTransform);
                rc.gl.glPushMatrix();
                java.nio.DoubleBuffer m_absBuf = java.nio.DoubleBuffer.wrap( transform.getAsColumnMajorArray16() );
                rc.gl.glMultMatrixd(m_absBuf);
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
