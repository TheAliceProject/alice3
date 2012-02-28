package org.lgna.story.event;

public class OcclusionEvent extends AbstractEvent {

	private org.lgna.story.MovableTurnable foreground;
	private org.lgna.story.MovableTurnable background;

	public OcclusionEvent( org.lgna.story.MovableTurnable foreground, org.lgna.story.MovableTurnable background ) {
		this.foreground = foreground;
		this.background = background;
	}

	public org.lgna.story.MovableTurnable getForegroundMovable() {
		return foreground;
	}
	public org.lgna.story.MovableTurnable getBackgroundMovable() {
		return background;
	}

}
