package org.lgna.story.event;

import org.lgna.story.SThing;

public class EndCollisionEvent extends CollisionEvent {

	public EndCollisionEvent( SThing a, SThing b ) {
		super( a, b );
	}

}
