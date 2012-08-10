package org.lgna.story.event;


public interface OcclusionEndListener<A extends org.lgna.story.SModel, B extends org.lgna.story.SModel> {
	
	public void occlusionEnded( EndOcclusionEvent<A, B> e );

}
