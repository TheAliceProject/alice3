package org.lgna.story.event;

public class StartOcclusionEvent<A extends org.lgna.story.Model, B extends org.lgna.story.Model> extends OcclusionEvent<A,B> {

	public StartOcclusionEvent( A a, B b ) {
		super( a, b );
	}

}
