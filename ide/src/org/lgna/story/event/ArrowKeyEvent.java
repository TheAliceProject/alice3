package org.lgna.story.event;

import java.util.List;

import org.lgna.story.Key;
import org.lgna.story.MoveDirection;
import org.lgna.story.TurnDirection;

import edu.cmu.cs.dennisc.java.util.Collections;

public class ArrowKeyEvent extends KeyEvent {

	public enum MoveDirectionPlane {
		FORWARD_BACKWARD_LEFT_RIGHT, UP_DOWN_LEFT_RIGHT;
	};

	public static final List<Key> ARROWS = Collections.newArrayList( Key.UP, Key.DOWN, Key.LEFT, Key.RIGHT, Key.A, Key.S, Key.D, Key.W );
	private KeyEvent e;

	public ArrowKeyEvent( KeyEvent e ) {
		super( e.getJavaEvent() );
		boolean isArrow = false;
		for( Key k : ARROWS ) {
			if( e.isKey( k ) ) {
				isArrow = true;
				break;
			}
		}
		assert isArrow;
		this.e = e;
	}

	public MoveDirection getMoveDirection( MoveDirectionPlane plane ) {
		if( plane.equals( MoveDirectionPlane.UP_DOWN_LEFT_RIGHT ) ) {
			return getUpDownLeftRightMoveDirection();
		}
		return getFowardBackwardLeftRightMoveDirection();
	}

	public TurnDirection getTurnDirection() {
		if( e.isKey( Key.A ) || e.isKey( Key.LEFT ) ) {
			return TurnDirection.LEFT;
		} else if( e.isKey( Key.S ) || e.isKey( Key.DOWN ) ) {
			return TurnDirection.BACKWARD;
		} else if( e.isKey( Key.W ) || e.isKey( Key.UP ) ) {
			return TurnDirection.FORWARD;
		} else if( e.isKey( Key.D ) || e.isKey( Key.RIGHT ) ) {
			return TurnDirection.RIGHT;
		}
		return null;
	}

	private MoveDirection getFowardBackwardLeftRightMoveDirection() {
		if( e.isKey( Key.A ) || e.isKey( Key.LEFT ) ) {
			return MoveDirection.LEFT;
		} else if( e.isKey( Key.S ) || e.isKey( Key.DOWN ) ) {
			return MoveDirection.BACKWARD;
		} else if( e.isKey( Key.W ) || e.isKey( Key.UP ) ) {
			return MoveDirection.FORWARD;
		} else if( e.isKey( Key.D ) || e.isKey( Key.RIGHT ) ) {
			return MoveDirection.RIGHT;
		}
		return null;
	}

	private MoveDirection getUpDownLeftRightMoveDirection() {
		if( e.isKey( Key.A ) || e.isKey( Key.LEFT ) ) {
			return MoveDirection.LEFT;
		} else if( e.isKey( Key.S ) || e.isKey( Key.DOWN ) ) {
			return MoveDirection.DOWN;
		} else if( e.isKey( Key.W ) || e.isKey( Key.UP ) ) {
			return MoveDirection.UP;
		} else if( e.isKey( Key.D ) || e.isKey( Key.RIGHT ) ) {
			return MoveDirection.RIGHT;
		}
		return null;
	}
}