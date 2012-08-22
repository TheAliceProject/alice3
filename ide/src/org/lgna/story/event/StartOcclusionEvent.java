package org.lgna.story.event;

public class StartOcclusionEvent<A extends org.lgna.story.SModel, B extends org.lgna.story.SModel> extends OcclusionEvent<A, B> {
	public StartOcclusionEvent( A a, B b ) {
		super( a, b );
	}
}
