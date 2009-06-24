/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.scenegraph.util;

//todo: unify w/ lookingglass.TransformationUtilities?
/**
 * @author Dennis Cosgrove
 */
public abstract class TransformationUtilities {
	private static boolean isAbsolute( edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgReferenceFrame ) {
		//todo: call isSceneOf()?
		if( sgReferenceFrame instanceof edu.cmu.cs.dennisc.scenegraph.Scene || sgReferenceFrame == edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE ) {
			return true;
		} else {
			return false;
		}
	}
	
	
	//Vector4d
	public static edu.cmu.cs.dennisc.math.Vector4 transformToAbsolute( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.math.Vector4 xyzwInFrom, edu.cmu.cs.dennisc.scenegraph.Component sgFrom ) {
		rv.set( xyzwInFrom );
		transformToAbsolute_AffectReturnValuePassedIn( rv, sgFrom );
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.Vector4 transformToAbsolute_New( edu.cmu.cs.dennisc.math.Vector4 xyzwInFrom, edu.cmu.cs.dennisc.scenegraph.Component sgFrom ) {
		return transformToAbsolute( new edu.cmu.cs.dennisc.math.Vector4(), xyzwInFrom, sgFrom );
	}
	public static edu.cmu.cs.dennisc.math.Vector4 transformToAbsolute_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.scenegraph.Component sgFrom ) {
		if( isAbsolute( sgFrom ) ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgFrom.getAbsoluteTransformation();
			m.transform( rv );
		}
		return rv;
	}
	

	public static edu.cmu.cs.dennisc.math.Vector4 transformFromAbsolute( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.math.Vector4 xyzwInAbsolute, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		rv.set( xyzwInAbsolute );
		transformFromAbsolute_AffectReturnValuePassedIn( rv, sgTo );
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.Vector4 transformFromAbsolute_New( edu.cmu.cs.dennisc.math.Vector4 xyzwInAbsolute, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		return transformFromAbsolute( new edu.cmu.cs.dennisc.math.Vector4(), xyzwInAbsolute, sgTo );
	}
	public static edu.cmu.cs.dennisc.math.Vector4 transformFromAbsolute_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		if( isAbsolute( sgTo ) ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgTo.getInverseAbsoluteTransformation();
			m.transform( rv );
		}
		return rv;
	}

	
	public static edu.cmu.cs.dennisc.math.Vector4 transformTo( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.math.Vector4 xyzwInFrom, edu.cmu.cs.dennisc.scenegraph.Component sgFrom, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		rv.set( xyzwInFrom );
		transformTo_AffectReturnValuePassedIn( rv, sgFrom, sgTo );
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.Vector4 transformTo_New( edu.cmu.cs.dennisc.math.Vector4 xyzwInFrom, edu.cmu.cs.dennisc.scenegraph.Component sgFrom, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		return transformTo( new edu.cmu.cs.dennisc.math.Vector4(), xyzwInFrom, sgFrom, sgTo );
	}
	public static edu.cmu.cs.dennisc.math.Vector4 transformTo_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.scenegraph.Component sgFrom, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		transformToAbsolute_AffectReturnValuePassedIn( rv, sgFrom );
		transformFromAbsolute_AffectReturnValuePassedIn( rv, sgTo );
		return rv;
	}



	//Vector3d
	public static edu.cmu.cs.dennisc.math.Vector3 transformToAbsolute( edu.cmu.cs.dennisc.math.Vector3 rv, edu.cmu.cs.dennisc.math.Vector3 xyzInFrom, edu.cmu.cs.dennisc.scenegraph.Component sgFrom ) {
		rv.set( xyzInFrom );
		transformToAbsolute_AffectReturnValuePassedIn( rv, sgFrom );
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.Vector3 transformToAbsolute_New( edu.cmu.cs.dennisc.math.Vector3 xyzInFrom, edu.cmu.cs.dennisc.scenegraph.Component sgFrom ) {
		return transformToAbsolute( new edu.cmu.cs.dennisc.math.Vector3(), xyzInFrom, sgFrom );
	}
	public static edu.cmu.cs.dennisc.math.Vector3 transformToAbsolute_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector3 rv, edu.cmu.cs.dennisc.scenegraph.Component sgFrom ) {
		if( isAbsolute( sgFrom ) ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgFrom.getAbsoluteTransformation();
			m.transform( rv );
		}
		return rv;
	}
	

	public static edu.cmu.cs.dennisc.math.Vector3 transformFromAbsolute( edu.cmu.cs.dennisc.math.Vector3 rv, edu.cmu.cs.dennisc.math.Vector3 xyzInAbsolute, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		rv.set( xyzInAbsolute );
		transformFromAbsolute_AffectReturnValuePassedIn( rv, sgTo );
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.Vector3 transformFromAbsolute_New( edu.cmu.cs.dennisc.math.Vector3 xyzInAbsolute, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		return transformFromAbsolute( new edu.cmu.cs.dennisc.math.Vector3(), xyzInAbsolute, sgTo );
	}
	public static edu.cmu.cs.dennisc.math.Vector3 transformFromAbsolute_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector3 rv, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		if( isAbsolute( sgTo ) ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgTo.getInverseAbsoluteTransformation();
			m.transform( rv );
		}
		return rv;
	}

	
	public static edu.cmu.cs.dennisc.math.Vector3 transformTo( edu.cmu.cs.dennisc.math.Vector3 rv, edu.cmu.cs.dennisc.math.Vector3 xyzInFrom, edu.cmu.cs.dennisc.scenegraph.Component sgFrom, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		rv.set( xyzInFrom );
		transformTo_AffectReturnValuePassedIn( rv, sgFrom, sgTo );
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.Vector3 transformTo_New( edu.cmu.cs.dennisc.math.Vector3 xyzInFrom, edu.cmu.cs.dennisc.scenegraph.Component sgFrom, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		return transformTo( new edu.cmu.cs.dennisc.math.Vector3(), xyzInFrom, sgFrom, sgTo );
	}
	public static edu.cmu.cs.dennisc.math.Vector3 transformTo_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector3 rv, edu.cmu.cs.dennisc.scenegraph.Component sgFrom, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		transformToAbsolute_AffectReturnValuePassedIn( rv, sgFrom );
		transformFromAbsolute_AffectReturnValuePassedIn( rv, sgTo );
		return rv;
	}



	//Point3d
	public static edu.cmu.cs.dennisc.math.Point3 transformToAbsolute( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Point3 xyzInFrom, edu.cmu.cs.dennisc.scenegraph.Component sgFrom ) {
		rv.set( xyzInFrom );
		transformToAbsolute_AffectReturnValuePassedIn( rv, sgFrom );
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.Point3 transformToAbsolute_New( edu.cmu.cs.dennisc.math.Point3 xyzInFrom, edu.cmu.cs.dennisc.scenegraph.Component sgFrom ) {
		return transformToAbsolute( new edu.cmu.cs.dennisc.math.Point3(), xyzInFrom, sgFrom );
	}
	public static edu.cmu.cs.dennisc.math.Point3 transformToAbsolute_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.scenegraph.Component sgFrom ) {
		if( isAbsolute( sgFrom ) ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgFrom.getAbsoluteTransformation();
			m.transform( rv );
		}
		return rv;
	}
	

	public static edu.cmu.cs.dennisc.math.Point3 transformFromAbsolute( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Point3 xyzInAbsolute, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		rv.set( xyzInAbsolute );
		transformFromAbsolute_AffectReturnValuePassedIn( rv, sgTo );
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.Point3 transformFromAbsolute_New( edu.cmu.cs.dennisc.math.Point3 xyzInAbsolute, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		return transformFromAbsolute( new edu.cmu.cs.dennisc.math.Point3(), xyzInAbsolute, sgTo );
	}
	public static edu.cmu.cs.dennisc.math.Point3 transformFromAbsolute_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		if( isAbsolute( sgTo ) ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgTo.getInverseAbsoluteTransformation();
			m.transform( rv );
		}
		return rv;
	}

	
	public static edu.cmu.cs.dennisc.math.Point3 transformTo( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Point3 xyzInFrom, edu.cmu.cs.dennisc.scenegraph.Component sgFrom, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		rv.set( xyzInFrom );
		transformTo_AffectReturnValuePassedIn( rv, sgFrom, sgTo );
		return rv;
	}
	public static edu.cmu.cs.dennisc.math.Point3 transformTo_New( edu.cmu.cs.dennisc.math.Point3 xyzInFrom, edu.cmu.cs.dennisc.scenegraph.Component sgFrom, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		return transformTo( new edu.cmu.cs.dennisc.math.Point3(), xyzInFrom, sgFrom, sgTo );
	}
	public static edu.cmu.cs.dennisc.math.Point3 transformTo_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.scenegraph.Component sgFrom, edu.cmu.cs.dennisc.scenegraph.Component sgTo ) {
		transformToAbsolute_AffectReturnValuePassedIn( rv, sgFrom );
		transformFromAbsolute_AffectReturnValuePassedIn( rv, sgTo );
		return rv;
	}
}
