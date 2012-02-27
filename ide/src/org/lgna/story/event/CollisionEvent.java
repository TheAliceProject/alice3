package org.lgna.story.event;

public class CollisionEvent extends AbstractEvent {
	private final org.lgna.story.Entity[] entity;

	public CollisionEvent( org.lgna.story.Entity... entities ) {
		this.entity = entities;
	}
	public org.lgna.story.Entity[] getModels() {
		return entity;
	}
}
