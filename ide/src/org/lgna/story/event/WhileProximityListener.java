package org.lgna.story.event;

public interface WhileProximityListener<A extends org.lgna.story.SMovableTurnable, B extends org.lgna.story.SMovableTurnable> extends WhileContingencyListener {

	public void whileClose( WhileProximityEvent<A, B> e );

}
