/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
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

//	private native void getLocalTransformationForBodyPartNamed( double[] result, String name );
//	private native void setLocalTransformationForBodyPartNamed( String name, double[] value );
//	//todo: remove
//	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getLocalTransformationForBodyPartNamed( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, String name ) {
//		assert name != null;
//		assert name.length() > 0;
//		assert name.equals( "<unnamed>" ) == false;
//		assert rv != null;
//
//		double[] buffer = new double[ 12 ];
//		getLocalTransformationForBodyPartNamed( buffer, name );
//		
//		rv.orientation.right.set   ( buffer[ 0 ], buffer[ 1 ], buffer[ 2 ] );
//		rv.orientation.up.set      ( buffer[ 3 ], buffer[ 4 ], buffer[ 5 ] );
//		rv.orientation.backward.set( buffer[ 6 ], buffer[ 7 ], buffer[ 8 ] );
//
//		rv.translation.set( buffer[ 9 ], buffer[ 10 ], buffer[ 11 ] );
//		
//		assert rv.isNaN() == false;
//		return rv;
//	}
//	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getLocalTransformationForBodyPartNamed( String name ) {
//		return getLocalTransformationForBodyPartNamed( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN(), name );
//	}
//	public void setLocalTransformationForBodyPartNamed( String name, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
//		assert name != null;
//		assert name.length() > 0;
//		assert name.equals( "<unnamed>" ) == false;
//		assert m != null;
//		assert m.isNaN() == false;
//		double[] buffer = {
//				m.orientation.right.x,
//				m.orientation.right.y,
//				m.orientation.right.z,
//
//				m.orientation.up.x,
//				m.orientation.up.y,
//				m.orientation.up.z,
//
//				m.orientation.backward.x,
//				m.orientation.backward.y,
//				m.orientation.backward.z,
//
//				m.translation.x,
//				m.translation.y,
//				m.translation.z
//		};
//		setLocalTransformationForBodyPartNamed( name, buffer );
//	}
//	
//	private native void getBoneAxis( double[] result, String name );
//	public edu.cmu.cs.dennisc.math.Vector3 getBoneAxis( edu.cmu.cs.dennisc.math.Vector3 rv, String name ) {
//		assert name != null;
//		assert name.length() > 0;
//		assert name.equals( "<unnamed>" ) == false;
//		assert rv != null;
//
//		double[] buffer = new double[ 3 ];
//		getBoneAxis( buffer, name );
//		
//		rv.set( buffer[ 0 ], buffer[ 1 ], buffer[ 2 ] );
//		
//		assert rv.isNaN() == false;
//		return rv;
//	}
//	public edu.cmu.cs.dennisc.math.Vector3 getBoneAxis( String name ) {
//		return getBoneAxis( edu.cmu.cs.dennisc.math.Vector3.createNaN(), name );
//	}

}
