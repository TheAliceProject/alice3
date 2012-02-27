package org.lgna.story.event;

public class EndCollisionEvent extends CollisionEvent {
	public EndCollisionEvent( java.util.List<org.lgna.story.Entity> entity ) {
		super(entity);
	}
}
