package edu.cmu.cs.dennisc.matt;

import org.lgna.story.event.OcclusionEvent;

public class EndOcclusionEvent extends OcclusionEvent {

	public EndOcclusionEvent( org.lgna.story.Entity foreground, org.lgna.story.Entity background ) {
		super( foreground, background );
	}

}
