package org.lgna.story.event;

public abstract class CollisionEvent extends AbstractBinarySThingEvent {
	public CollisionEvent( org.lgna.story.SThing a, org.lgna.story.SThing b ) {
		super( a, b );
	}
}
