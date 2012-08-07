package org.lgna.story.event;

public class EndCollisionEvent<A extends org.lgna.story.SMovableTurnable, B extends org.lgna.story.SMovableTurnable> extends CollisionEvent<A,B> {
	public EndCollisionEvent( A collidingFromA, B collidingFromB ) {
		super( collidingFromA, collidingFromB );
	}
}
