package org.lgna.story.event;

public class PointOfViewEvent extends AbstractEvent {
	private final org.lgna.story.Entity entity;
	public PointOfViewEvent( org.lgna.story.Entity entity ) {
		this.entity = entity;
	}
	public org.lgna.story.Entity getEntity() {
		return entity;
	}
}
