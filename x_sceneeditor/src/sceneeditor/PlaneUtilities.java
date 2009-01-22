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
package sceneeditor;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

/**
 * @author David Culyba
 */
public class PlaneUtilities {
	
	public static edu.cmu.cs.dennisc.math.Point3 getPointInPlane( edu.cmu.cs.dennisc.math.Plane plane, edu.cmu.cs.dennisc.math.Ray ray ) {
		double t = plane.intersect( ray );
		if ( Double.isNaN( t ) )
		{
			return null;
		}
		return ray.getPointAlong( t );
	}
	
	public static edu.cmu.cs.dennisc.math.Ray getRayFromPixel( OnscreenLookingGlass onscreenLookingGlass, AbstractCamera camera, int xPixel, int yPixel)
	{
		edu.cmu.cs.dennisc.math.Ray ray = onscreenLookingGlass.getRayAtPixel( xPixel, yPixel, camera );
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = camera.getAbsoluteTransformation();
		ray.transform( m );
		return ray;
	}

}
