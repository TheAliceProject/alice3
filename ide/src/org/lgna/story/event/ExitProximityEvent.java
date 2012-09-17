package org.lgna.story.event;

public class ExitProximityEvent<A extends org.lgna.story.SMovableTurnable, B extends org.lgna.story.SMovableTurnable> extends ProximityEvent<A, B> {
	public ExitProximityEvent( A closeFromA, B closeFromB ) {
		super( closeFromA, closeFromB );
	}
}
