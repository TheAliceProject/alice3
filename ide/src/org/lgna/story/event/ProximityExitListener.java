package org.lgna.story.event;

public interface ProximityExitListener<A extends org.lgna.story.SMovableTurnable, B extends org.lgna.story.SMovableTurnable> {

	public void proximityExited( ExitProximityEvent<A, B> e );

}
