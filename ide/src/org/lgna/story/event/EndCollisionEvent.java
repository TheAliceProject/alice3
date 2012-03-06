package org.lgna.story.event;

public class EndCollisionEvent<A extends org.lgna.story.MovableTurnable, B extends org.lgna.story.MovableTurnable> extends CollisionEvent<A,B> {

	public EndCollisionEvent( A[] firstArray, B[] secondArray ) {
		super( firstArray, secondArray );
	}
	//keeping for now for migration
	//	public EndCollisionEvent( org.lgna.story.MovableTurnable... movable ) {
	//		super( movable );
	//	}
}
