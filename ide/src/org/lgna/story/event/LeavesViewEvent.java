package org.lgna.story.event;

public class LeavesViewEvent<A extends org.lgna.story.Model> extends ViewEvent<A> {
	public LeavesViewEvent( A model ) {
		super( model );
	}
}
