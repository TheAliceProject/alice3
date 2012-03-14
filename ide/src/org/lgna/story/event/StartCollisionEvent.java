package org.lgna.story.event;

public class StartCollisionEvent<A extends org.lgna.story.MovableTurnable, B extends org.lgna.story.MovableTurnable> extends CollisionEvent<A,B> {

	public StartCollisionEvent( A collidingFromA, B collidingFromB ) {
		super( collidingFromA, collidingFromB );
	}
}
