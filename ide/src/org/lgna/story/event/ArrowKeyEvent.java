package org.lgna.story.event;

import org.lgna.story.Key;

public class ArrowKeyEvent extends KeyEvent {

	public enum MoveDirectionPlane {
		FORWARD_BACKWARD_LEFT_RIGHT, UP_DOWN_LEFT_RIGHT;
	};

	public static final java.util.List<Key> ARROWS = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( Key.UP, Key.DOWN, Key.LEFT, Key.RIGHT, Key.A, Key.S, Key.D, Key.W );
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

	public org.lgna.story.MoveDirection getMoveDirection( MoveDirectionPlane plane ) {
		if( plane.equals( MoveDirectionPlane.UP_DOWN_LEFT_RIGHT ) ) {
			return getUpDownLeftRightMoveDirection();
		}
		return getFowardBackwardLeftRightMoveDirection();
	}

	public org.lgna.story.TurnDirection getTurnDirection() {
		if( e.isKey( Key.A ) || e.isKey( Key.LEFT ) ) {
			return org.lgna.story.TurnDirection.LEFT;
		} else if( e.isKey( Key.S ) || e.isKey( Key.DOWN ) ) {
			return org.lgna.story.TurnDirection.BACKWARD;
		} else if( e.isKey( Key.W ) || e.isKey( Key.UP ) ) {
			return org.lgna.story.TurnDirection.FORWARD;
		} else if( e.isKey( Key.D ) || e.isKey( Key.RIGHT ) ) {
			return org.lgna.story.TurnDirection.RIGHT;
		}
		return null;
	}

	private org.lgna.story.MoveDirection getFowardBackwardLeftRightMoveDirection() {
		if( e.isKey( Key.A ) || e.isKey( Key.LEFT ) ) {
			return org.lgna.story.MoveDirection.LEFT;
		} else if( e.isKey( Key.S ) || e.isKey( Key.DOWN ) ) {
			return org.lgna.story.MoveDirection.BACKWARD;
		} else if( e.isKey( Key.W ) || e.isKey( Key.UP ) ) {
			return org.lgna.story.MoveDirection.FORWARD;
		} else if( e.isKey( Key.D ) || e.isKey( Key.RIGHT ) ) {
			return org.lgna.story.MoveDirection.RIGHT;
		}
		return null;
	}

	private org.lgna.story.MoveDirection getUpDownLeftRightMoveDirection() {
		if( e.isKey( Key.A ) || e.isKey( Key.LEFT ) ) {
			return org.lgna.story.MoveDirection.LEFT;
		} else if( e.isKey( Key.S ) || e.isKey( Key.DOWN ) ) {
			return org.lgna.story.MoveDirection.DOWN;
		} else if( e.isKey( Key.W ) || e.isKey( Key.UP ) ) {
			return org.lgna.story.MoveDirection.UP;
		} else if( e.isKey( Key.D ) || e.isKey( Key.RIGHT ) ) {
			return org.lgna.story.MoveDirection.RIGHT;
		}
		return null;
	}
}