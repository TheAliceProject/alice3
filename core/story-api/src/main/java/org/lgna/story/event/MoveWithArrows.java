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
package org.lgna.story.event;

import java.util.Map;

import org.lgna.story.Duration;
import org.lgna.story.HeldKeyPolicy;
import org.lgna.story.MoveDirection;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.implementation.eventhandling.KeyPressedHandler;

import edu.cmu.cs.dennisc.java.util.Maps;

public class MoveWithArrows {

	private MoveWithArrows( org.lgna.story.SMovableTurnable entity, Double speed ) {
		this.entity = entity;
		this.moveSpeed = speed;
	}

	public static void createNewAndAddTo( SMovableTurnable moving, Double speed, KeyPressedHandler keyHandler ) {
		MoveWithArrows mover = new MoveWithArrows( moving, speed );
		keyHandler.addListener( mover.getPressListener(), MultipleEventPolicy.IGNORE, ArrowKeyEvent.ARROWS, HeldKeyPolicy.FIRE_MULTIPLE );
		keyHandler.addListener( mover.getReleaseListener(), MultipleEventPolicy.IGNORE, ArrowKeyEvent.ARROWS, HeldKeyPolicy.FIRE_ONCE_ON_RELEASE );
	}

	public ArrowKeyPressListener getPressListener() {
		return this.pressListener;
	}

	public ArrowKeyPressListener getReleaseListener() {
		return this.releaseListener;
	}

	private void startFiring( MoveDirection direction ) {
		if( lastFrameTimeMs != 0 ) {
			double timeElapsed = .001 * ( System.currentTimeMillis() - lastFrameTimeMs );

			org.lgna.story.SThingMarker stoodUpTemporaryMarker = new org.lgna.story.SThingMarker();
			stoodUpTemporaryMarker.setVehicle( entity.getVehicle() );
			stoodUpTemporaryMarker.moveAndOrientTo( entity, new Duration( 0 ) );
			stoodUpTemporaryMarker.orientToUpright( new Duration( 0 ) );

			if( ( direction == MoveDirection.FORWARD ) || ( direction == MoveDirection.BACKWARD ) ) {
				entity.move( direction, moveSpeed * timeElapsed, new Duration( 0 ), new org.lgna.story.AsSeenBy( stoodUpTemporaryMarker ) );
			}
			else if( direction == MoveDirection.RIGHT ) {
				entity.turn( org.lgna.story.TurnDirection.RIGHT, turnSpeed * timeElapsed, new Duration( 0 ), new org.lgna.story.AsSeenBy( stoodUpTemporaryMarker ) );
			}
			else if( direction == MoveDirection.LEFT ) {
				entity.turn( org.lgna.story.TurnDirection.LEFT, turnSpeed * timeElapsed, new Duration( 0 ), new org.lgna.story.AsSeenBy( stoodUpTemporaryMarker ) );
			}

			stoodUpTemporaryMarker.setVehicle( null );
		}
		lastFrameTimeMs = System.currentTimeMillis();
	}

	private final org.lgna.story.SMovableTurnable entity;
	private Map<MoveDirection, Boolean> pressed = Maps.newConcurrentHashMap();
	private final double moveSpeed;
	private final double turnSpeed = .2;
	private long lastFrameTimeMs = 0;

	private final ArrowKeyPressListener pressListener = new ArrowKeyPressListener() {

		@Override
		public void arrowKeyPressed( ArrowKeyEvent e ) {
			MoveDirection moveDirection = e.getMoveDirection( org.lgna.story.event.ArrowKeyEvent.MoveDirectionPlane.FORWARD_BACKWARD_LEFT_RIGHT );
			pressed.put( moveDirection, true );
			startFiring( moveDirection );
		}
	};
	private final ArrowKeyPressListener releaseListener = new ArrowKeyPressListener() {

		@Override
		public void arrowKeyPressed( ArrowKeyEvent e ) {
			MoveDirection moveDirection = e.getMoveDirection( org.lgna.story.event.ArrowKeyEvent.MoveDirectionPlane.FORWARD_BACKWARD_LEFT_RIGHT );
			pressed.put( moveDirection, false );
			lastFrameTimeMs = 0;
		}
	};
}
