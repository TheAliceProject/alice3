package org.lgna.story.event;

public class EndCollisionEvent<A extends org.lgna.story.MovableTurnable, B extends org.lgna.story.MovableTurnable> extends CollisionEvent<A,B> {

	public EndCollisionEvent( A collidingFromA, B collidingFromB ) {
		super( collidingFromA, collidingFromB );
	}
}
