package org.lgna.story.event;

public class LeavesViewEvent<A extends org.lgna.story.SModel> extends ViewEvent<A> {
	public LeavesViewEvent( A model ) {
		super( model );
	}
}
