package org.lgna.story.event;

public interface CollisionStartListener<A extends org.lgna.story.SMovableTurnable, B extends org.lgna.story.SMovableTurnable> {

	public void collisionStarted( StartCollisionEvent<A,B> e );

}
