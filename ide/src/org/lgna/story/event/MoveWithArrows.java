package org.lgna.story.event;

import org.lgna.story.MovableTurnable;
import org.lgna.story.event.ArrowKeyEvent.MoveDirectionSpec;

public class MoveWithArrows implements ArrowKeyPressListener {

	private MovableTurnable entity;

	public MoveWithArrows(MovableTurnable entity) {
		this.entity = entity;
	}

	public void keyPressed(ArrowKeyEvent e) {
		entity.move( e.getMoveDirection( MoveDirectionSpec.FORWARD_BACKWARD_LEFT_RIGHT ), 1 );
	}

}
