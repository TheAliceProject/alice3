package org.lgna.story.event;

import java.util.List;

import org.lgna.story.Entity;

public class EndCollisionEvent extends CollisionEvent {
	public EndCollisionEvent( List<Entity> entity ) {
		super(entity);
	}
}
