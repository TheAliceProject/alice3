package edu.cmu.cs.dennisc.matt;

import java.awt.Point;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.implementation.CameraImp;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.math.Point3;

public class AabbOcclusionDetector {

	public static boolean doesTheseOcclude(CameraImp camera, Entity object1, Entity object2) {
		EntityImp implementation = ImplementationAccessor.getImplementation( object1 );
		EntityImp implementation2 = ImplementationAccessor.getImplementation( object2 );
		Point3[] object1Points = implementation.getAxisAlignedMinimumBoundingBox().getHexahedron().getPoints();
		Point3[] object2Points = implementation2.getAxisAlignedMinimumBoundingBox().getHexahedron().getPoints();
		Point[] object1AWTPoints = new Point[8];
		Point[] object2AWTPoints = new Point[8];
		for(int i = 0; i != object1Points.length; ++i){
			object1AWTPoints[i] = implementation.transformToAwt( object1Points[i], camera );
			object2AWTPoints[i] = implementation2.transformToAwt( object2Points[i], camera );
		}
		Point[] boundingBox1 = getBoundingBox(object1AWTPoints);//bounding box is pretty crude
		Point[] boundingBox2 = getBoundingBox(object2AWTPoints);
		return checkBoundingBox(boundingBox1, boundingBox2);
	}

	private static boolean checkBoundingBox(Point[] box1, Point[] box2) {
		Point maxOne = box1[0];
		Point minOne = box1[1];
		Point maxTwo = box2[0];
		Point minTwo = box2[1];
		if(((maxOne.x > minTwo.x && maxOne.x < maxTwo.x) && ((maxOne.y > minTwo.y && maxOne.y < maxTwo.y) || (minOne.y > minTwo.y && minOne.y < maxTwo.y) || (maxOne.y > maxTwo.y && minOne.y < minTwo.y)))
				|| ((minOne.x > minTwo.x && minOne.x < maxTwo.x) && ((minOne.y > minTwo.y && minOne.y < maxTwo.y) || (maxOne.y > minTwo.y && maxOne.y < maxTwo.y) || (maxOne.y > maxTwo.y && minOne.y < minTwo.y)))){
			return true;
		}else return false;
	}

	private static Point[] getBoundingBox(Point[] points) {
		int xMax = -1;
		int yMax = 0;
		int xMin = 0;
		int yMin = 0;
		for(Point p: points){
			if(xMax == -1){
				xMax = p.x;
				xMin = p.x;
				yMax = p.y;
				yMin = p.y;
			}else{
				if(p.x > xMax ){
					xMax = p.x;
				}else if(p.x < xMin){
					xMin = p.x;
				}else if(p.y > yMax){
					yMax = p.y;
				}else if(p.y < yMin){
					yMin = p.y;
				}
			}
		}
		Point max = new Point(xMax, yMax);
		Point min = new Point(xMin, yMin);
		Point[] rv = new Point[2];
		rv[0] = max;
		rv[1] = min;
		return rv;
	}
}
