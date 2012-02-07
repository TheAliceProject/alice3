package org.lgna.story.event;

import org.lgna.story.Entity;

@Deprecated
public class ComesIntoViewEvent extends AbstractEvent {

	private Entity entity;

	public ComesIntoViewEvent( Entity entity ) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}	
}
