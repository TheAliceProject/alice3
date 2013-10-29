package org.lgna.story.event;

public class OcclusionEvent extends AbstractEvent {

	private org.lgna.story.SModel foreground;
	private org.lgna.story.SModel background;

	public OcclusionEvent( org.lgna.story.SModel foreground, org.lgna.story.SModel background ) {
		this.foreground = foreground;
		this.background = background;
	}

	public org.lgna.story.SModel getForegroundModel() {
		return foreground;
	}

	public org.lgna.story.SModel getBackgroundModel() {
		return background;
	}

}
