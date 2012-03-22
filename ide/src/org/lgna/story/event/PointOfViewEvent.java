package org.lgna.story.event;

import org.lgna.story.MovableTurnable;

public class PointOfViewEvent<A extends MovableTurnable> extends AbstractEvent {
	private final A entity;

	public PointOfViewEvent( A entity ) {
		this.entity = entity;
	}
	public A getMoved() {
		return entity;
	}
}
