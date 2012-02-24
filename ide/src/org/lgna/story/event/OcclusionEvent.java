package org.lgna.story.event;

import java.util.List;

import org.lgna.story.Entity;


public class OcclusionEvent extends AbstractEvent {
	
	private List< Entity > entityPair;

	public OcclusionEvent( List< Entity > entityPair ) {
		assert entityPair.size() == 2;
		this.entityPair = entityPair;
	}

	public Entity getForegroundEntity(){
		return entityPair.get(0);
	}
	public Entity getBackgroundEntity(){
		return entityPair.get(1);
	}
	
}
