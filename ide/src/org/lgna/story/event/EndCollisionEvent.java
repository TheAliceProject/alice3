package org.lgna.story.event;

public class EndCollisionEvent extends CollisionEvent {
	public EndCollisionEvent( org.lgna.story.Entity... entities ) {
		super( entities );
	}
}
