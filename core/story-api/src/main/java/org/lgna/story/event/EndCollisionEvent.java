package org.lgna.story.event;

public class EndCollisionEvent extends CollisionEvent {

	public EndCollisionEvent( org.lgna.story.SThing a, org.lgna.story.SThing b ) {
		super( a, b );
	}

}
