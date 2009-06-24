/**
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
package org.alice.interact;

import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.math.AngleInRadians;

/**
 * @author David Culyba
 */
public class VectorUtilities {
	
	public static AngleInRadians getAngleBetweenVectors(Vector3 a, Vector3 b )
	{
		double angleDot = Vector3.calculateDotProduct( a, b );
		if (angleDot < -1.0d) angleDot = -1.0d;
		if (angleDot > 1.0d) angleDot = 1.0d;
		
		return new edu.cmu.cs.dennisc.math.AngleInRadians(Math.acos(angleDot));
	}
	
	/*
	 * Projects the projector onto the projectee
	 */
	public static Vector3 projectOntoVector( Vector3 projector, Vector3 projectee)
	{
		Vector3 normalizedProjectee = new Vector3(projectee);
		normalizedProjectee.normalize();
		double dotProduct = Vector3.calculateDotProduct( projector, normalizedProjectee );
		normalizedProjectee.multiply( dotProduct );
		return normalizedProjectee;
	}
	
}
