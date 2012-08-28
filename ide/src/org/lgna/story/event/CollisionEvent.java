package org.lgna.story.event;

public abstract class CollisionEvent extends AbstractEvent {
	private final org.lgna.story.SMovableTurnable[] movables;

	public CollisionEvent( org.lgna.story.SMovableTurnable... movables ) {
		this.movables = movables;
	}

	public org.lgna.story.SMovableTurnable[] getModels() {
		return movables;
	}
}
