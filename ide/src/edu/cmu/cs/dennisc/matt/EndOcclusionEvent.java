package edu.cmu.cs.dennisc.matt;

public class EndOcclusionEvent<A extends org.lgna.story.Model, B extends org.lgna.story.Model> extends org.lgna.story.event.OcclusionEvent<A,B> {

	public EndOcclusionEvent( A a, B b ) {
		super( a, b );
	}

}
