package edu.cmu.cs.dennisc.matt;

import org.lgna.story.event.OcclusionEvent;

public class EndOcclusionEvent extends OcclusionEvent {

	public EndOcclusionEvent( org.lgna.story.SMovableTurnable foreground, org.lgna.story.SMovableTurnable background ) {
		super( foreground, background );
	}

}
