package org.lgna.story.event;

public class MoveWithArrows implements ArrowKeyPressListener {

	private org.lgna.story.SMovableTurnable entity;

	public MoveWithArrows( org.lgna.story.SMovableTurnable entity ) {
		this.entity = entity;
	}

	public void arrowKeyPressed( ArrowKeyEvent e ) {
		entity.move( e.getMoveDirection( org.lgna.story.event.ArrowKeyEvent.MoveDirectionPlane.FORWARD_BACKWARD_LEFT_RIGHT ), 1 );
	}

}
