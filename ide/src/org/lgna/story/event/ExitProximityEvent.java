package org.lgna.story.event;

import java.util.List;

import org.lgna.story.Entity;

public class ExitProximityEvent extends ProximityEvent {

	public ExitProximityEvent(List<Entity> entities) {
		super(entities);
	}

}
