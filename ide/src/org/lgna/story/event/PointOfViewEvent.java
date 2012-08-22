package org.lgna.story.event;

public class PointOfViewEvent extends AbstractEvent {
	private final org.lgna.story.SThing entity;
	public PointOfViewEvent( org.lgna.story.SThing entity ) {
		this.entity = entity;
	}
	public org.lgna.story.SThing getEntity() {
		return entity;
	}
}
