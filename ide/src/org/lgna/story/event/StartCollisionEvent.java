package org.lgna.story.event;

import org.lgna.story.SThing;

public class StartCollisionEvent extends CollisionEvent {

	public StartCollisionEvent( SThing a, SThing b ) {
		super( a, b );
	}
}
