package edu.cmu.cs.dennisc.matt;

import java.awt.Point;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.implementation.CameraImp;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;

public class IsInViewDetector {

	public static boolean isThisInView( Entity entity, CameraImp camera ) {
		EntityImp implementation = ImplementationAccessor.getImplementation( entity );
		Point3[] points = implementation.getAxisAlignedMinimumBoundingBox().getHexahedron().getPoints();
		Point3[] relativeToCamera = implementation.getAxisAlignedMinimumBoundingBox( camera ).getHexahedron().getPoints();
		Point[] awtPoints = new Point[points.length];
		for ( int i = 0; i < points.length; ++i ){
			awtPoints[i] = implementation.transformToAwt( points[i], camera );
		}
		camera.getScene().getProgram().getOnscreenLookingGlass();
		return isInView( camera, awtPoints, relativeToCamera );
	}

	private static boolean isInView( CameraImp camera, Point[] awtPoints, Point3[] relativeToCamera ) {
		int width = camera.getScene().getProgram().getOnscreenLookingGlass().getWidth();
		int height = camera.getScene().getProgram().getOnscreenLookingGlass().getWidth();
		boolean leftOf = false;
		boolean rightOf = false;
		boolean above = false;
		boolean below = false;
		for( int i = 0; i != awtPoints.length; ++i ) {
			if ( awtPoints[i].x < width && awtPoints[i].x > 0 && awtPoints[i].y < height && awtPoints[i].y > 0 ){
				if( relativeToCamera[i].z < 0 ) {
					return true;
				}
			}else{
				if ( awtPoints[i].x > width ) {
					if( relativeToCamera[i].z < 0 ) {
						rightOf = true;
					}
				} if ( awtPoints[i].x < 0 ) {
					if( relativeToCamera[i].z < 0 ) {
						leftOf = true;
					}
				} if ( awtPoints[i].y > height ) {
					if( relativeToCamera[i].z < 0 ) {
						above = true;
					}
				} if ( awtPoints[i].y < 0) {
					if( relativeToCamera[i].z < 0 ) {
						below = true;
					}
				}
			}

		}
		if( leftOf && rightOf && above && below ) {
			return true;
		}
		if( ( leftOf && rightOf ) && ( !above || !below ) ) {
			return true;
		}
		if( ( !leftOf || !rightOf ) && ( above && below ) ) {
			return true;
		}
		return false;
	}

}
