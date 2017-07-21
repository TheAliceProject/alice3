/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package org.lgna.story.implementation.eventhandling;

import org.lgna.story.SThing;
import org.lgna.story.implementation.AsSeenBy;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author Gazihan Alankus, Matt May
 */
public class AabbCollisionDetector {

	public static boolean doTheseCollide( SThing object1, SThing object2 ) {
		Point3 center1 = new Point3();
		Point3 center2 = new Point3();
		Vector3 corner1 = new Vector3();
		Vector3 corner2 = new Vector3();
		findCenterAndCorner( object1, center1, corner1 );
		findCenterAndCorner( object2, center2, corner2 );

		return doAabbsCollide( center1, corner1, center2, corner2 );
	}

	public static boolean doTheseCollide( edu.cmu.cs.dennisc.scenegraph.Transformable object1, edu.cmu.cs.dennisc.scenegraph.Transformable object2, double dist ) {

		return doTheseCollide( EntityImp.getAbstractionFromSgElement( object1 ), EntityImp.getAbstractionFromSgElement( object2 ), dist );
	}

	public static boolean doTheseCollide( edu.cmu.cs.dennisc.scenegraph.Transformable object1, edu.cmu.cs.dennisc.scenegraph.Transformable object2 ) {
		return doTheseCollide( EntityImp.getAbstractionFromSgElement( object1 ), EntityImp.getAbstractionFromSgElement( object2 ) );
	}

	public static boolean doTheseCollide( SThing object1, SThing object2, double extraProximity ) {
		Point3 center1 = new Point3();
		Point3 center2 = new Point3();
		Vector3 corner1 = new Vector3();
		Vector3 corner2 = new Vector3();
		findCenterAndCorner( object1, center1, corner1 );
		findCenterAndCorner( object2, center2, corner2 );

		return doAabbsCollide( center1, corner1, center2, corner2, extraProximity );
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

	private static void findCenterAndCorner( SThing object, Point3 center, Vector3 corner ) {
		AxisAlignedBox aabb1 = org.lgna.story.EmployeesOnly.getImplementation( object ).getAxisAlignedMinimumBoundingBox( AsSeenBy.SCENE );
		Point3 min = aabb1.getMinimum();
		Point3 max = aabb1.getMaximum();
		center.setToInterpolation( min, max, .5 );
		corner.setToSubtraction( max, min );
		corner.multiply( .5 );

	}

	private static boolean doAabbsCollide( Point3 boxAPosition, Vector3 boxACorner, Point3 boxBPosition, Vector3 boxBCorner ) {
		return doAabbsCollide( boxAPosition, boxACorner, boxBPosition, boxBCorner, .0 );
	}

	//it collides if it's not exactly touching but extraProximity far apart
	private static boolean doAabbsCollide( Point3 boxAPosition, Vector3 boxACorner, Point3 boxBPosition, Vector3 boxBCorner, double extraProximity ) {
		Vector3 distVector = Vector3.createSubtraction( boxAPosition, boxBPosition );
		distVector.x = Math.abs( distVector.x );
		distVector.y = Math.abs( distVector.y );
		distVector.z = Math.abs( distVector.z );

		return ( ( distVector.x - extraProximity ) < ( boxACorner.x + boxBCorner.x ) )
				&& ( ( distVector.y - extraProximity ) < ( boxACorner.y + boxBCorner.y ) )
				&& ( ( distVector.z - extraProximity ) < ( boxACorner.z + boxBCorner.z ) );
	}
}
