package edu.cmu.cs.dennisc.matt;

import org.lgna.story.event.OcclusionEvent;

public class EndOcclusionEvent extends OcclusionEvent {

	public EndOcclusionEvent( org.lgna.story.MovableTurnable foreground, org.lgna.story.MovableTurnable background ) {
		super( foreground, background );
	}

}
