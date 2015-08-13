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
