package org.lgna.story.event;

import org.lgna.story.MovableTurnable;

public class EndCollisionEvent extends CollisionEvent {
	public EndCollisionEvent( MovableTurnable... movable ) {
		super( movable );
	}
}
