package org.lgna.story.event;

public class StartCollisionEvent extends CollisionEvent {
	public StartCollisionEvent( org.lgna.story.Entity... entities ) {
		super( entities );
	}
}
