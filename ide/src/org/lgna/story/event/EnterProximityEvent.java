package org.lgna.story.event;

import java.util.List;

import org.lgna.story.Entity;

public class EnterProximityEvent extends ProximityEvent {

	public EnterProximityEvent(List<Entity> entities) {
		super(entities);
	}

}
