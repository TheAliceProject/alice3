package org.lgna.story.event;

public interface PointOfViewChangeListener<A extends org.lgna.story.SModel> {
	public void pointOfViewChanged( PointOfViewEvent<A> e );
}
