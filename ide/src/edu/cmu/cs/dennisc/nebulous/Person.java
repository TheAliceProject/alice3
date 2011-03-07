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
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getLocalTransformationForJoint( org.lookingglassandalice.storytelling.resources.JointId joint ) {
		double[] buffer = new double[ 12 ];
		getLocalTransformationForBodyPartNamed( buffer, joint );
		return edu.cmu.cs.dennisc.math.AffineMatrix4x4.createFromColumnMajorArray12( buffer );
	}

	public void setLocalTransformationForJoint( org.lookingglassandalice.storytelling.resources.JointId joint, edu.cmu.cs.dennisc.math.AffineMatrix4x4 localTrans ) {
		setLocalTransformationForBodyPartNamed( joint, localTrans.getAsColumnMajorArray12() );
	}
}
