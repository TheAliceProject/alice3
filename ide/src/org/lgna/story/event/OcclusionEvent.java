package org.lgna.story.event;

public class OcclusionEvent extends AbstractEvent {
	
	private java.util.List< org.lgna.story.Entity > entityPair;

	public OcclusionEvent( java.util.List< org.lgna.story.Entity > entityPair ) {
		assert entityPair.size() == 2;
		this.entityPair = entityPair;
	}

	public org.lgna.story.Entity getForegroundEntity(){
		return entityPair.get(0);
	}
	public org.lgna.story.Entity getBackgroundEntity(){
		return entityPair.get(1);
	}
	
}
