package org.lgna.story.event;

public class StartCollisionEvent<A extends org.lgna.story.SMovableTurnable, B extends org.lgna.story.SMovableTurnable> extends CollisionEvent<A,B> {
	public StartCollisionEvent( A collidingFromA, B collidingFromB ) {
		super( collidingFromA, collidingFromB );
	}
}
