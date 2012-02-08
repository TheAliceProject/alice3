package edu.cmu.cs.dennisc.matt;

import java.awt.Point;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.implementation.CameraImp;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.math.Point3;

public class IsInViewDetector {

	public static boolean isThisInView( Entity entity, CameraImp camera ) {
		EntityImp implementation = ImplementationAccessor.getImplementation( entity );
		Point3[] points = implementation.getAxisAlignedMinimumBoundingBox().getHexahedron().getPoints();
		Point[] awtPoints = new Point[points.length];
		for ( int i = 0; i < points.length; ++i ){
			awtPoints[i] = implementation.transformToAwt( points[i], camera );
		}
		return isInView( awtPoints, camera );
	}

	private static boolean isInView(Point[] awtPoints, CameraImp camera) {
		int width = camera.getScene().getProgram().getOnscreenLookingGlass().getWidth();
		int height = camera.getScene().getProgram().getOnscreenLookingGlass().getWidth();
		for( Point p : awtPoints ) {
			if ( p.x < width && p.x > 0 && p.y < height && p.y > 0) {//point is inside
				return true;
			}
		}
		return false;
	}
	
}
