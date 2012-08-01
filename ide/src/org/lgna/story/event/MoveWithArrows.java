package org.lgna.story.event;

import org.lgna.story.SMovableTurnable;
import org.lgna.story.event.ArrowKeyEvent.MoveDirectionPlane;

public class MoveWithArrows implements ArrowKeyPressListener {

	private SMovableTurnable entity;

	public MoveWithArrows( SMovableTurnable entity ) {
		this.entity = entity;
	}

	public void arrowKeyPressed( ArrowKeyEvent e ) {
		entity.move( e.getMoveDirection( MoveDirectionPlane.FORWARD_BACKWARD_LEFT_RIGHT ), 1 );
	}

}
