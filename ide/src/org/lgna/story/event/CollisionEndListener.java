package org.lgna.story.event;

public interface CollisionEndListener<A extends org.lgna.story.SMovableTurnable, B extends org.lgna.story.SMovableTurnable> {

	public void collisionEnded( EndCollisionEvent<A,B> e );

}
