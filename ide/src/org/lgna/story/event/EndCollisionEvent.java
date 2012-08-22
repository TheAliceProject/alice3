package org.lgna.story.event;

public class EndCollisionEvent extends CollisionEvent {
	public EndCollisionEvent( org.lgna.story.SMovableTurnable... movable ) {
		super( movable );
	}
}
