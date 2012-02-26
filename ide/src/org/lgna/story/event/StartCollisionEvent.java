package org.lgna.story.event;

import java.util.List;

import org.lgna.story.Entity;

public class StartCollisionEvent extends CollisionEvent {	
	public StartCollisionEvent( List<Entity> entity ) {
		super(entity);
	}
}
