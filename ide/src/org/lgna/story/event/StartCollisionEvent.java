package org.lgna.story.event;

public class StartCollisionEvent extends CollisionEvent {	
	public StartCollisionEvent( java.util.List<org.lgna.story.Entity> entity ) {
		super(entity);
	}
}
