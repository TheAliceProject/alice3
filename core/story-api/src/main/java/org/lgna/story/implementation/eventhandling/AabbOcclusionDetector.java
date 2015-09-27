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

import java.awt.Point;

import org.lgna.story.EmployeesOnly;
import org.lgna.story.SThing;
import org.lgna.story.implementation.AsSeenBy;
import org.lgna.story.implementation.CameraImp;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.math.Point3;

/**
 * @author Gazihan Alankus, Matt May
 */
public class AabbOcclusionDetector {

	public static boolean doesTheseOcclude( CameraImp camera, SThing object1, SThing object2 ) {
		EntityImp implementation = EmployeesOnly.getImplementation( object1 );
		EntityImp implementation2 = EmployeesOnly.getImplementation( object2 );
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
