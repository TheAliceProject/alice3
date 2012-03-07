package org.lgna.story.event;

public interface CollisionEndListener<A extends org.lgna.story.MovableTurnable, B extends org.lgna.story.MovableTurnable> {

	public void collisionEnded( EndCollisionEvent<A,B> e );

}
