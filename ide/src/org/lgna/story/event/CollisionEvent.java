package org.lgna.story.event;

public abstract class CollisionEvent extends AbstractEvent {
	private final org.lgna.story.MovableTurnable[] movables;

	public CollisionEvent( org.lgna.story.MovableTurnable... movables ) {
		this.movables = movables;
	}
	public org.lgna.story.MovableTurnable[] getModels() {
		return movables;
	}
}
