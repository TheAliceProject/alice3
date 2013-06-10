package org.lgna.story.event;

import org.lgna.story.SThing;

public abstract class CollisionEvent extends AbstractBinarySThingEvent {
	public CollisionEvent( SThing a, SThing b ) {
		super( a, b );
	}
}
