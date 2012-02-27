package org.lgna.story.event;

public class OcclusionEvent extends AbstractEvent {

	private org.lgna.story.Entity foreground;
	private org.lgna.story.Entity background;

	public OcclusionEvent( org.lgna.story.Entity foreground, org.lgna.story.Entity background ) {
		this.foreground = foreground;
		this.background = background;
	}

	public org.lgna.story.Entity getForegroundEntity() {
		return foreground;
	}
	public org.lgna.story.Entity getBackgroundEntity() {
		return background;
	}

}
