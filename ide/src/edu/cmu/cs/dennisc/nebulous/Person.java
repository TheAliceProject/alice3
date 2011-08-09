/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */

package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public class Person extends Model {
	static {
		edu.cmu.cs.dennisc.lookingglass.opengl.AdapterFactory.register( Person.class, PersonAdapter.class );
		
	}

	public Person( Object o ) throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
		System.out.println(System.getProperty("java.library.path"));
		initialize( o );
	}
	private native void initialize( Object o );
	public native void setGender( Object o );
	public native void setOutfit( Object o );
	public native void setSkinTone( Object o );
	public native void setFitnessLevel( Object o );
	public native void setEyeColor( Object o );
	public native void setHair( Object o );

	public native void getLocalTransformationForBodyPartNamed( double[] transformOut, org.lgna.story.resources.JointId name );
	public native void setLocalTransformationForBodyPartNamed( org.lgna.story.resources.JointId name, double[] transformIn );
	public native void getAbsoluteTransformationForBodyPartNamed( double[] transformOut, org.lgna.story.resources.JointId name );
	public native void setAbsoluteTransformationForBodyPartNamed( org.lgna.story.resources.JointId name, double[] transformIn );
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getLocalTransformationForJoint( org.lgna.story.resources.JointId joint ) {
		double[] buffer = new double[ 12 ];
		getLocalTransformationForBodyPartNamed( buffer, joint );
		return edu.cmu.cs.dennisc.math.AffineMatrix4x4.createFromColumnMajorArray12( buffer );
	}

	public void setLocalTransformationForJoint( org.lgna.story.resources.JointId joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4 localTrans ) {
		setLocalTransformationForBodyPartNamed( joint, localTrans.getAsColumnMajorArray12() );
	}
	
	@Deprecated
	public void applyRotationToJointAboutArbitraryAxisInRadians( org.lgna.story.resources.JointId joint, edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians ) {
		if( axis.isPositiveXAxis() ) {
			applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutXAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( angleInRadians ) ) );
		} else if( axis.isNegativeXAxis() ) {
			applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutXAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( -angleInRadians ) ) );
		} else if( axis.isPositiveYAxis() ) {
			applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutYAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( angleInRadians ) ) );
		} else if( axis.isNegativeYAxis() ) {
			applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutYAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( -angleInRadians ) ) );
		} else if( axis.isPositiveZAxis() ) {
			applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutZAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( angleInRadians ) ) );
		} else if( axis.isNegativeZAxis() ) {
			applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutZAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( -angleInRadians ) ) );
		} else {
			applyTransformationToJoint( joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createOrientation( new edu.cmu.cs.dennisc.math.AxisRotation( axis, new edu.cmu.cs.dennisc.math.AngleInRadians( angleInRadians ) ) ) );
		}
	}
//
	@Deprecated
	private void applyTransformationToJoint( org.lgna.story.resources.JointId joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4 transformation ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = getLocalTransformationForJoint( joint );
		m.setToMultiplication( m, transformation );
		setLocalTransformationForJoint( joint, m );
	}
}
