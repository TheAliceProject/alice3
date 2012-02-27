package org.lgna.story.event;

public class StartOcclusionEvent extends OcclusionEvent {

	public StartOcclusionEvent( org.lgna.story.Entity foreground, org.lgna.story.Entity background ) {
		super( foreground, background );
	}

}
