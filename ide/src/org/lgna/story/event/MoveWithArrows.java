package org.lgna.story.event;

import org.lgna.story.Entity;
import org.lgna.story.MovableTurnable;

public class MoveWithArrows implements ArrowKeyPressListener {

	private MovableTurnable entity;

	public MoveWithArrows(MovableTurnable entity) {
		this.entity = entity;
	}

	public void keyPressed(ArrowKeyEvent e) {
		entity.move( e.getFowardBackwardLeftRightMoveDirection(), 1 );
	}

}
