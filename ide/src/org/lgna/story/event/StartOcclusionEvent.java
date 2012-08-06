package org.lgna.story.event;

public class StartOcclusionEvent extends OcclusionEvent {

	public StartOcclusionEvent( org.lgna.story.SMovableTurnable foreground, org.lgna.story.SMovableTurnable background ) {
		super( foreground, background );
	}

}
