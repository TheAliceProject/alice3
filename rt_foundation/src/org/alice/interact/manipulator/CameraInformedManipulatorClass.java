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
package org.alice.interact.manipulator;


/**
 * @author David Culyba
 */
public abstract class CameraInformedManipulatorClass extends AbstractManipulator{

	protected edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera = null;
	protected edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass;
	
	public void setCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera)
	{
		this.camera = camera;
	}
	
	public void setOnscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		this.onscreenLookingGlass = onscreenLookingGlass;
	}
	
	protected edu.cmu.cs.dennisc.math.Point3 getPointInPlane( edu.cmu.cs.dennisc.math.Plane plane, edu.cmu.cs.dennisc.math.Ray ray ) {
		double t = plane.intersect( ray );
		if ( Double.isNaN( t ) )
		{
			return null;
		}
		return ray.getPointAlong( t );
	}
	
	protected edu.cmu.cs.dennisc.math.Point3 getPointInPlane( edu.cmu.cs.dennisc.math.Plane plane, int xPixel, int yPixel ) {
		edu.cmu.cs.dennisc.math.Ray ray = this.onscreenLookingGlass.getRayAtPixel( xPixel, yPixel, this.camera );
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.camera.getAbsoluteTransformation();
		ray.transform( m );
		return getPointInPlane( plane, ray );

	}
	
}
