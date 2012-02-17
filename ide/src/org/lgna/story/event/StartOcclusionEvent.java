package org.lgna.story.event;

import java.util.List;

import org.lgna.story.Entity;

public class StartOcclusionEvent extends OcclusionEvent {

	public StartOcclusionEvent(List<Entity> entityPair) {
		super(entityPair);
	}

}
