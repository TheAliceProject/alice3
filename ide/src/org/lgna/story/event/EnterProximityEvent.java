package org.lgna.story.event;

public class EnterProximityEvent<A extends org.lgna.story.MovableTurnable, B extends org.lgna.story.MovableTurnable> extends ProximityEvent<A,B> {
	public EnterProximityEvent( A closeFromA, B closeFromB ) {
		super( closeFromA, closeFromB );
	}
}
