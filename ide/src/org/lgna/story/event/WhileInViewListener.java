package org.lgna.story.event;

public interface WhileInViewListener<A extends org.lgna.story.SModel> extends WhileContingencyListener {

	public void whileInView( WhileInViewEvent<A> e );

}
