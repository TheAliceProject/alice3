package org.lgna.story.event;

public interface WhileCollisionListener<A extends org.lgna.story.SMovableTurnable, B extends org.lgna.story.SMovableTurnable> extends WhileContingencyListener {

	public void whileColliding(WhileCollisionEvent<A,B> event);
	
}
