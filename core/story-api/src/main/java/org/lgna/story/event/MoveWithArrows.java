package org.lgna.story.event;

import java.util.Map;

import org.lgna.story.AnimationStyle;
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
		this.increment = speed / movesPerSecond;
	}

	public static void createNewAndAddTo( SMovableTurnable moving, Double speed, KeyPressedHandler keyHandler ) {
		MoveWithArrows mover = new MoveWithArrows( moving, speed );
		keyHandler.addListener( mover.getPressListener(), MultipleEventPolicy.COMBINE, ArrowKeyEvent.ARROWS, HeldKeyPolicy.FIRE_ONCE_ON_PRESS );
		keyHandler.addListener( mover.getReleaseListener(), MultipleEventPolicy.COMBINE, ArrowKeyEvent.ARROWS, HeldKeyPolicy.FIRE_ONCE_ON_RELEASE );
	}

	public ArrowKeyPressListener getPressListener() {
		return this.pressListener;
	}

	public ArrowKeyPressListener getReleaseListener() {
		return this.releaseListener;
	}

	private void startFiring( MoveDirection direction ) {
		while( pressed.get( direction ) ) {
			entity.move( direction, increment, AnimationStyle.BEGIN_AND_END_ABRUPTLY, new Duration( 1 / movesPerSecond ) );
		}
	}

	private final org.lgna.story.SMovableTurnable entity;
	private Map<MoveDirection, Boolean> pressed = Maps.newConcurrentHashMap();
	private final double increment;
	private final double movesPerSecond = 4;

	private final ArrowKeyPressListener pressListener = new ArrowKeyPressListener() {

		public void arrowKeyPressed( ArrowKeyEvent e ) {
			MoveDirection moveDirection = e.getMoveDirection( org.lgna.story.event.ArrowKeyEvent.MoveDirectionPlane.FORWARD_BACKWARD_LEFT_RIGHT );
			pressed.put( moveDirection, true );
			startFiring( moveDirection );
		}
	};
	private final ArrowKeyPressListener releaseListener = new ArrowKeyPressListener() {

		public void arrowKeyPressed( ArrowKeyEvent e ) {
			MoveDirection moveDirection = e.getMoveDirection( org.lgna.story.event.ArrowKeyEvent.MoveDirectionPlane.FORWARD_BACKWARD_LEFT_RIGHT );
			pressed.put( moveDirection, false );
		}
	};
}
