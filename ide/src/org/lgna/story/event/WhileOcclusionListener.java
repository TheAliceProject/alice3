package org.lgna.story.event;

public interface WhileOcclusionListener<A extends org.lgna.story.SModel, B extends org.lgna.story.SModel> extends WhileContingencyListener {

	public void whileOccluding( WhileOccludingEvent<A,B> e );

}
