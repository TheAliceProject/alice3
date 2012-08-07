﻿//These need wustl header
package edu.cmu.cs.dennisc.matt;

import org.lgna.story.SThing;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.implementation.AsSeenBy;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;

public class AabbCollisionDetector {
	
	public static boolean doTheseCollide(SThing object1, SThing object2) {
		Point3 center1 = new Point3();
		Point3 center2 = new Point3();
		Vector3 corner1 = new Vector3();
		Vector3 corner2 = new Vector3();
		findCenterAndCorner(object1, center1, corner1);
		findCenterAndCorner(object2, center2, corner2);
		
		return doAabbsCollide(center1, corner1, center2, corner2);
	}
	
	public static boolean doTheseCollide(edu.cmu.cs.dennisc.scenegraph.Transformable object1, edu.cmu.cs.dennisc.scenegraph.Transformable object2, double dist){
		
		return doTheseCollide(EntityImp.getAbstractionFromSgElement(object1), EntityImp.getAbstractionFromSgElement(object2), dist);
	}
	
	public static boolean doTheseCollide(edu.cmu.cs.dennisc.scenegraph.Transformable object1, edu.cmu.cs.dennisc.scenegraph.Transformable object2){
		return doTheseCollide(EntityImp.getAbstractionFromSgElement(object1), EntityImp.getAbstractionFromSgElement(object2));
	}



	public static boolean doTheseCollide(SThing object1, SThing object2, double extraProximity) {
		Point3 center1 = new Point3();
		Point3 center2 = new Point3();
		Vector3 corner1 = new Vector3();
		Vector3 corner2 = new Vector3();
		findCenterAndCorner(object1, center1, corner1);
		findCenterAndCorner(object2, center2, corner2);
		
		return doAabbsCollide(center1, corner1, center2, corner2, extraProximity);
	}
	
//	private static void findCenterAndCorner(Entity object, Point3 center, Vector3 corner) {
//		// there are some issues with all these puttings in and
//		// takings out of objects in the scene.
//		if (object.getVehicle() != null) {
//			AxisAlignedBox aabb1 = ImplementationAccessor.getImplementation(object).getAxisAlignedMinimumBoundingBox();
//			Point3 min = aabb1.getMinimum();
//			Point3 max = aabb1.getMaximum();
//			center.setToInterpolation(min, max, .5);
//			corner.setToSubtraction(max, min);
//			corner.multiply(.5);
//		}
//	}
	
	private static void findCenterAndCorner( SThing object, Point3 center, Vector3 corner) {
		AxisAlignedBox aabb1 = ImplementationAccessor.getImplementation(object).getAxisAlignedMinimumBoundingBox(AsSeenBy.SCENE);
		Point3 min = aabb1.getMinimum();
		Point3 max = aabb1.getMaximum();
		center.setToInterpolation(min, max, .5);
		corner.setToSubtraction(max, min);
		corner.multiply(.5);
		
	}
	
	private static boolean doAabbsCollide(Point3 boxAPosition, Vector3 boxACorner, Point3 boxBPosition, Vector3 boxBCorner) {
		return doAabbsCollide(boxAPosition, boxACorner, boxBPosition, boxBCorner, .0);
	}
	
	//it collides if it's not exactly touching but extraProximity far apart
	private static boolean doAabbsCollide(Point3 boxAPosition, Vector3 boxACorner, Point3 boxBPosition, Vector3 boxBCorner, double extraProximity) {
		Vector3 distVector = Vector3.createSubtraction(boxAPosition, boxBPosition);
		distVector.x = Math.abs(distVector.x);
		distVector.y = Math.abs(distVector.y);
		distVector.z = Math.abs(distVector.z);
		
		return distVector.x - extraProximity < boxACorner.x + boxBCorner.x
			&& distVector.y - extraProximity < boxACorner.y + boxBCorner.y
			&& distVector.z - extraProximity < boxACorner.z + boxBCorner.z;
	}
}
