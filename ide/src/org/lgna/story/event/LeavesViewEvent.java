package org.lgna.story.event;

import org.lgna.story.Entity;

public class LeavesViewEvent extends ViewEvent {

	public LeavesViewEvent( Entity entity ) {
		this.entity = entity;
	}
	
}
