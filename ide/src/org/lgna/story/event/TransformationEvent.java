package org.lgna.story.event;

import org.lgna.story.Entity;

public class TransformationEvent extends AbstractEvent {
	
	private final Entity entity;
	
	public TransformationEvent( Entity entity ) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

}
