package org.lgna.story.event;

import org.lgna.story.Key;

public class ArrowKeyEvent extends AbstractKeyEvent {

	public enum MoveDirectionPlane {
		FORWARD_BACKWARD_LEFT_RIGHT, UP_DOWN_LEFT_RIGHT;
	};

	public static final java.util.List<Key> ARROWS = edu.cmu.cs.dennisc.java.util.Collections.newArrayList( Key.UP, Key.DOWN, Key.LEFT, Key.RIGHT, Key.A, Key.S, Key.D, Key.W );
	public ArrowKeyEvent( java.awt.event.KeyEvent e ) {
		super( e );
		boolean isArrow = false;
		for( Key k : ARROWS ) {
			if( this.isKey( k ) ) {
				isArrow = true;
				break;
			}
		}
		assert isArrow : e;
	}
	public ArrowKeyEvent( AbstractKeyEvent other ) {
		this( other.getJavaEvent() );
	}

	public org.lgna.story.MoveDirection getMoveDirection( MoveDirectionPlane plane ) {
		if( plane.equals( MoveDirectionPlane.UP_DOWN_LEFT_RIGHT ) ) {
			return getUpDownLeftRightMoveDirection();
		}
		return getFowardBackwardLeftRightMoveDirection();
	}

	public org.lgna.story.TurnDirection getTurnDirection() {
		if( this.isKey( Key.A ) || this.isKey( Key.LEFT ) ) {
			return org.lgna.story.TurnDirection.LEFT;
		} else if( this.isKey( Key.S ) || this.isKey( Key.DOWN ) ) {
			return org.lgna.story.TurnDirection.BACKWARD;
		} else if( this.isKey( Key.W ) || this.isKey( Key.UP ) ) {
			return org.lgna.story.TurnDirection.FORWARD;
		} else if( this.isKey( Key.D ) || this.isKey( Key.RIGHT ) ) {
			return org.lgna.story.TurnDirection.RIGHT;
		}
		return null;
	}

	private org.lgna.story.MoveDirection getFowardBackwardLeftRightMoveDirection() {
		if( this.isKey( Key.A ) || this.isKey( Key.LEFT ) ) {
			return org.lgna.story.MoveDirection.LEFT;
		} else if( this.isKey( Key.S ) || this.isKey( Key.DOWN ) ) {
			return org.lgna.story.MoveDirection.BACKWARD;
		} else if( this.isKey( Key.W ) || this.isKey( Key.UP ) ) {
			return org.lgna.story.MoveDirection.FORWARD;
		} else if( this.isKey( Key.D ) || this.isKey( Key.RIGHT ) ) {
			return org.lgna.story.MoveDirection.RIGHT;
		}
		return null;
	}

	private org.lgna.story.MoveDirection getUpDownLeftRightMoveDirection() {
		if( this.isKey( Key.A ) || this.isKey( Key.LEFT ) ) {
			return org.lgna.story.MoveDirection.LEFT;
		} else if( this.isKey( Key.S ) || this.isKey( Key.DOWN ) ) {
			return org.lgna.story.MoveDirection.DOWN;
		} else if( this.isKey( Key.W ) || this.isKey( Key.UP ) ) {
			return org.lgna.story.MoveDirection.UP;
		} else if( this.isKey( Key.D ) || this.isKey( Key.RIGHT ) ) {
			return org.lgna.story.MoveDirection.RIGHT;
		}
		return null;
	}
}