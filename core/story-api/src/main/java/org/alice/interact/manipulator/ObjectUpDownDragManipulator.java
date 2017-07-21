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
package org.alice.interact.manipulator;

import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author David Culyba
 */
public class ObjectUpDownDragManipulator extends ObjectTranslateDragManipulator {

	@Override
	protected void initializeEventMessages() {
		this.setMainManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, null, this.manipulatedTransformable ) );
		this.clearManipulationEvents();
		this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription( MovementDirection.UP, MovementType.ABSOLUTE ), this.manipulatedTransformable ) );
		this.addManipulationEvent( new ManipulationEvent( ManipulationEvent.EventType.Translate, new MovementDescription( MovementDirection.DOWN, MovementType.ABSOLUTE ), this.manipulatedTransformable ) );
	}

	@Override
	protected Plane createPickPlane( Point3 clickPoint ) {
		return this.createCameraFacingStoodUpPlane( clickPoint );
	}

	@Override
	protected Plane createBadAnglePlane( Point3 clickPoint ) {
		Vector3 cameraUp = this.getCamera().getAbsoluteTransformation().orientation.up;
		Vector3 badPlaneNormal = Vector3.createPositiveYAxis();
		badPlaneNormal.subtract( cameraUp );
		badPlaneNormal.normalize();
		if( badPlaneNormal.isNaN() ) {
			badPlaneNormal = Vector3.createPositiveYAxis();
		}
		return Plane.createInstance( clickPoint, badPlaneNormal );
	}

	@Override
	protected Point3 getPositionForPlane( Plane movementPlane, Ray pickRay ) {
		if( pickRay != null ) {
			Point3 pointInPlane = PlaneUtilities.getPointInPlane( movementPlane, pickRay );
			Point3 newPosition = Point3.createAddition( this.offsetToOrigin, pointInPlane );
			newPosition.x = this.initialObjectPosition.x;
			newPosition.z = this.initialObjectPosition.z;
			return newPosition;
		} else {
			return null;
		}
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return new HandleSet( HandleSet.HandleGroup.Y_AXIS, HandleSet.HandleGroup.VISUALIZATION, HandleSet.HandleGroup.ABSOLUTE_TRANSLATION );
	}

}
