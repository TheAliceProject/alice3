package org.lgna.story.event;

public interface OcclusionStartListener<A extends org.lgna.story.SModel, B extends org.lgna.story.SModel> {

	public void occlusionStarted( StartOcclusionEvent<A, B> e );

}
