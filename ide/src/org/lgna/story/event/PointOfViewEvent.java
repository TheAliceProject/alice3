package org.lgna.story.event;

public class PointOfViewEvent<A extends org.lgna.story.MovableTurnable> extends AbstractEvent {
	private final A entity;

	public PointOfViewEvent( A entity ) {
		this.entity = entity;
	}
	public A getMoved() {
		return entity;
	}
}
