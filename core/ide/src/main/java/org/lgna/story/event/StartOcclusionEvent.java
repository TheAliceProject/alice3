package org.lgna.story.event;

public class StartOcclusionEvent extends OcclusionEvent {

	public StartOcclusionEvent( org.lgna.story.SModel foreground, org.lgna.story.SModel background ) {
		super( foreground, background );
	}

}
