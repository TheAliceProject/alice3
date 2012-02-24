package org.lgna.story.event;

import java.util.List;

import org.lgna.story.Entity;

public class CollisionEvent extends AbstractEvent {
	
	private final List<Entity> entity;

	public CollisionEvent(List<Entity> entity) {
		this.entity = entity;
	}
	public List<Entity> getModels() {
		return entity;
	}
}
