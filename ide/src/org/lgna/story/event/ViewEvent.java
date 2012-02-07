package org.lgna.story.event;

import org.lgna.story.Entity;

public class ViewEvent extends AbstractEvent {

	protected Entity entity;

	public Entity getEntity() {
		return entity;
	}
}
