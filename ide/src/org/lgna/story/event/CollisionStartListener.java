package org.lgna.story.event;

public interface CollisionStartListener<A extends org.lgna.story.MovableTurnable, B extends org.lgna.story.MovableTurnable> {

	public void collisionStarted( StartCollisionEvent<A,B> e );

}
