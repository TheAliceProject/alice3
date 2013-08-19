package edu.cmu.cs.dennisc.matt;

import java.awt.Point;

import org.lgna.story.ImplementationAccessor;
import org.lgna.story.SThing;
import org.lgna.story.implementation.AsSeenBy;
import org.lgna.story.implementation.CameraImp;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.math.Point3;

public class AabbOcclusionDetector {

	public static boolean doesTheseOcclude( CameraImp camera, SThing object1, SThing object2 ) {
		EntityImp implementation = ImplementationAccessor.getImplementation( object1 );
		EntityImp implementation2 = ImplementationAccessor.getImplementation( object2 );
		SceneImp scene = implementation.getScene();
		Point3[] object1Points = implementation.getAxisAlignedMinimumBoundingBox( AsSeenBy.SCENE ).getHexahedron().getPoints();
		Point3[] object2Points = implementation2.getAxisAlignedMinimumBoundingBox( AsSeenBy.SCENE ).getHexahedron().getPoints();
		Point[] object1AWTPoints = new Point[ 8 ];
		Point[] object2AWTPoints = new Point[ 8 ];
		for( int i = 0; i != object1Points.length; ++i ) {
			object1AWTPoints[ i ] = scene.transformToAwt( object1Points[ i ], camera );
			object2AWTPoints[ i ] = scene.transformToAwt( object2Points[ i ], camera );
		}
		Point[] boundingBox1 = getBoundingBox( object1AWTPoints );//bounding box is pretty crude
		Point[] boundingBox2 = getBoundingBox( object2AWTPoints );
		boolean checkBoundingBox = checkBoundingBox( boundingBox1, boundingBox2 );
		return checkBoundingBox;
	}

	private static boolean checkBoundingBox( Point[] box1, Point[] box2 ) {
		Point maxOne = box1[ 0 ];
		Point minOne = box1[ 1 ];
		Point maxTwo = box2[ 0 ];
		Point minTwo = box2[ 1 ];
		if( checkPointInBox( maxTwo, maxOne, minOne ) ) {//tr
			return true;
		} else if( checkPointInBox( minTwo, maxOne, minOne ) ) {//bl
			return true;
		} else if( checkPointInBox( new Point( maxTwo.x, minTwo.y ), maxOne, minOne ) ) {//br
			return true;
		} else if( checkPointInBox( new Point( minTwo.x, maxTwo.y ), maxOne, minOne ) ) {//tl
			return true;
		} else if( checkPointInBox( maxOne, maxTwo, minTwo ) ) {//tr
			return true;
		} else if( checkPointInBox( minOne, maxTwo, minTwo ) ) {//bl
			return true;
		} else if( checkPointInBox( new Point( maxOne.x, minOne.y ), maxTwo, minTwo ) ) {//br
			return true;
		} else if( checkPointInBox( new Point( minOne.x, maxOne.y ), maxTwo, minTwo ) ) {//tl
			return true;
		} else if( checkAdv( maxOne, minOne, maxTwo, minTwo ) ) {
			return true;
		} else if( checkAdv( maxTwo, minTwo, maxOne, minTwo ) ) {
			return true;
		}
		return false;
	}

	private static boolean checkPointInBox( Point pointToTest, Point boxMax, Point boxMin ) {
		if( ( pointToTest.x < boxMax.x ) && ( pointToTest.x > boxMin.x ) && ( ( pointToTest.y < boxMax.y ) && ( pointToTest.y > boxMin.y ) ) ) {
			return true;
		}
		return false;
	}

	private static boolean checkAdv( Point maxOne, Point minOne, Point maxTwo, Point minTwo ) {
		if( ( maxOne.y > maxTwo.y ) && ( minOne.y < minTwo.y ) ) {
			if( ( maxOne.x < maxTwo.x ) && ( maxOne.x > minTwo.x ) ) {
				return true;
			} else if( ( minOne.x < maxTwo.x ) && ( minOne.x > minTwo.x ) ) {
				return true;
			}
		}
		return false;
	}

	private static Point[] getBoundingBox( Point[] points ) {
		int xMax = -1;
		int yMax = 0;
		int xMin = 0;
		int yMin = 0;
		for( Point p : points ) {
			if( xMax == -1 ) {
				xMax = p.x;
				xMin = p.x;
				yMax = p.y;
				yMin = p.y;
			} else {
				if( p.x > xMax ) {
					xMax = p.x;
				} else if( p.x < xMin ) {
					xMin = p.x;
				} else if( p.y > yMax ) {
					yMax = p.y;
				} else if( p.y < yMin ) {
					yMin = p.y;
				}
			}
		}
		Point max = new Point( xMax, yMax );
		Point min = new Point( xMin, yMin );
		Point[] rv = new Point[ 2 ];
		rv[ 0 ] = max;
		rv[ 1 ] = min;
		return rv;
	}
}
