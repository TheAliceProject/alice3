package org.lgna.story.event;

import org.lgna.story.Entity;

public class ComesIntoViewEvent extends ViewEvent {

	public ComesIntoViewEvent( Entity entity ) {
		this.entity = entity;
	}

}
