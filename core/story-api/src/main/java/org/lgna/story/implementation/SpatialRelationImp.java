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

package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public enum SpatialRelationImp {
	LEFT_OF( new edu.cmu.cs.dennisc.math.Point3( -1, 0, 0 ) ),
	RIGHT_OF( new edu.cmu.cs.dennisc.math.Point3( 1, 0, 0 ) ),
	ABOVE( new edu.cmu.cs.dennisc.math.Point3( 0, 1, 0 ) ),
	BELOW( new edu.cmu.cs.dennisc.math.Point3( 0, -1, 0 ) ),
	IN_FRONT_OF( new edu.cmu.cs.dennisc.math.Point3( 0, 0, -1 ) ),
	BEHIND( new edu.cmu.cs.dennisc.math.Point3( 0, 0, 1 ) );

	//	FRONT_RIGHT_OF ( new edu.cmu.cs.dennisc.math.Point3( 0.7071068, 0, -0.7071068 ) ),
	//	FRONT_LEFT_OF ( new edu.cmu.cs.dennisc.math.Point3( -0.7071068, 0, -0.7071068 ) ),
	//	BEHIND_RIGHT_OF ( new edu.cmu.cs.dennisc.math.Point3(  0.7071068, 0, 0.7071068 ) ),
	//	BEHIND_LEFT_OF ( new edu.cmu.cs.dennisc.math.Point3( -0.7071068, 0, 0.7071068 ) );

	//public static final SpatialRelation IN = new SpatialRelation();
	//public static final SpatialRelation ON = new SpatialRelation();
	//public static final SpatialRelation AT = new SpatialRelation();

	SpatialRelationImp( edu.cmu.cs.dennisc.math.Point3 placeAxis ) {
		this.placeAxis = placeAxis;
	}

	public edu.cmu.cs.dennisc.math.Point3 getPlaceLocation( double alongAxisOffset, edu.cmu.cs.dennisc.math.AxisAlignedBox subjectBoundingBox, edu.cmu.cs.dennisc.math.AxisAlignedBox objectBoundingBox ) {
		double x = alongAxisOffset * this.placeAxis.x;
		double y = alongAxisOffset * this.placeAxis.y;
		double z = alongAxisOffset * this.placeAxis.z;

		if( this.placeAxis.x > 0 ) {
			x = objectBoundingBox.getMaximum().x;
			x -= subjectBoundingBox.getMinimum().x;
		} else if( this.placeAxis.x < 0 ) {
			x = objectBoundingBox.getMinimum().x;
			x -= subjectBoundingBox.getMaximum().x;
		}
		if( this.placeAxis.y > 0 ) {
			y = objectBoundingBox.getMaximum().y;
			y -= subjectBoundingBox.getMinimum().y;
		} else if( this.placeAxis.y < 0 ) {
			y = objectBoundingBox.getMinimum().y;
			y -= subjectBoundingBox.getMaximum().y;
		}
		if( this.placeAxis.z > 0 ) {
			z = objectBoundingBox.getMaximum().z;
			z -= subjectBoundingBox.getMinimum().z;
		} else if( this.placeAxis.z < 0 ) {
			z = objectBoundingBox.getMinimum().z;
			z -= subjectBoundingBox.getMaximum().z;
		}

		return new edu.cmu.cs.dennisc.math.Point3( x, y, z );
	}

	private final edu.cmu.cs.dennisc.math.Point3 placeAxis;
}
