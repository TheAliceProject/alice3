package org.lgna.story.event;

public class StartOcclusionEvent extends OcclusionEvent {

	public StartOcclusionEvent( org.lgna.story.MovableTurnable foreground, org.lgna.story.MovableTurnable background ) {
		super( foreground, background );
	}

}
