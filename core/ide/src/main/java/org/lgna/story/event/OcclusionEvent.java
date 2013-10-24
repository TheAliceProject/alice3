package org.lgna.story.event;

public class OcclusionEvent extends AbstractEvent {

	private org.lgna.story.SMovableTurnable foreground;
	private org.lgna.story.SMovableTurnable background;

	public OcclusionEvent( org.lgna.story.SMovableTurnable foreground, org.lgna.story.SMovableTurnable background ) {
		this.foreground = foreground;
		this.background = background;
	}

	public org.lgna.story.SMovableTurnable getForegroundMovable() {
		return foreground;
	}

	public org.lgna.story.SMovableTurnable getBackgroundMovable() {
		return background;
	}

}
