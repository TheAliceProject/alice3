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


}
