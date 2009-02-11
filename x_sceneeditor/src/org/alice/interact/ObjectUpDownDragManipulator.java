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

import java.awt.Point;

import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;

/**
 * @author David Culyba
 */
public class ObjectUpDownDragManipulator extends ObjectTranslateDragManipulator {

	
	
	@Override
	protected Plane createPickPlane( Point3 clickPoint )
	{
		return this.createCameraFacingStoodUpPlane( clickPoint );
	}
	
	@Override
	protected Plane createBadAnglePlane( Point3 clickPoint )
	{
		Vector3 cameraUp = this.camera.getAbsoluteTransformation().orientation.up;
		Vector3 badPlaneNormal = Vector3.createPositiveYAxis();
		badPlaneNormal.subtract( cameraUp );
		badPlaneNormal.normalize();
		return new Plane( clickPoint, badPlaneNormal );
	}
	
	@Override
	protected Point3 getPositionForPlane( Plane movementPlane, Ray pickRay )
	{
		if (pickRay != null)
		{
			Point3 pointInPlane = PlaneUtilities.getPointInPlane( movementPlane, pickRay );
			Point3 newPosition = Point3.createAddition( this.offsetToOrigin, pointInPlane );
			newPosition.x = this.initialObjectPosition.x;
			newPosition.z = this.initialObjectPosition.z;
			return newPosition;
		}
		else
		{
			return null;
		}
	}
	
}
