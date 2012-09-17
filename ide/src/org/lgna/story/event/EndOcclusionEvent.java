package org.lgna.story.event;

public class EndOcclusionEvent<A extends org.lgna.story.SModel, B extends org.lgna.story.SModel> extends org.lgna.story.event.OcclusionEvent<A, B> {

	public EndOcclusionEvent( A a, B b ) {
		super( a, b );
	}

}
