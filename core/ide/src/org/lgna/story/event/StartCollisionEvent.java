package org.lgna.story.event;

public class StartCollisionEvent extends CollisionEvent {

	public StartCollisionEvent( org.lgna.story.SThing a, org.lgna.story.SThing b ) {
		super( a, b );
	}
}
