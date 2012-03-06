package org.lgna.story.event;

public class StartCollisionEvent<A extends org.lgna.story.MovableTurnable, B extends org.lgna.story.MovableTurnable> extends CollisionEvent<A,B> {

	public StartCollisionEvent( A[] firstArray, B[] secondArray ) {
		super( firstArray, secondArray );
	}
	//keeping around for migration
	//	public StartCollisionEvent( org.lgna.story.MovableTurnable... moveable ) {
	//		super( moveable );
	//	}
}
