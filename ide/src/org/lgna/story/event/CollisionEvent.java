package org.lgna.story.event;

public class CollisionEvent extends AbstractEvent {
	private final java.util.List<org.lgna.story.Entity> entity;
	public CollisionEvent(java.util.List<org.lgna.story.Entity> entity) {
		this.entity = entity;
	}
	public java.util.List<org.lgna.story.Entity> getModels() {
		return entity;
	}
}
