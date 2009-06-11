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

import edu.cmu.cs.dennisc.scenegraph.*;

/**
 * @author Dennis Cosgrove
 */
public abstract class CameraUtilities {
	//todo: better name
	public static edu.cmu.cs.dennisc.math.AffineMatrix4x4 calculateGoodLookAt( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, edu.cmu.cs.dennisc.math.Angle smallerViewingAngle, Component sgTarget, edu.cmu.cs.dennisc.math.Sphere sphere, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy ) {
		sgTarget.getTransformation( rv, sgAsSeenBy );
		double thetaInRadians = smallerViewingAngle.getAsRadians();
		double distance = sphere.radius / Math.sin( thetaInRadians/2.0 );
		rv.applyTranslationAlongZAxis( distance );
		return rv;
	}
	//todo: better name
	public static edu.cmu.cs.dennisc.math.AffineMatrix4x4 calculateGoodLookAt( edu.cmu.cs.dennisc.math.Angle smallerViewingAngle, Component sgTarget, edu.cmu.cs.dennisc.math.Sphere sphere, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy ) {
		return calculateGoodLookAt( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN(), smallerViewingAngle, sgTarget, sphere, sgAsSeenBy );
	}
}
