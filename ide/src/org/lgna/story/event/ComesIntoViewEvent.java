package org.lgna.story.event;

public class ComesIntoViewEvent<A extends org.lgna.story.Model> extends ViewEvent<A> {
	public ComesIntoViewEvent( A model ) {
		super( model );
	}
}
