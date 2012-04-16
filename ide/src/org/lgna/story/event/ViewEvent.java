package org.lgna.story.event;

public abstract class ViewEvent extends AbstractEvent {
	private final org.lgna.story.Model model;

	public ViewEvent( org.lgna.story.Model model ) {
		this.model = model;
	}
	public org.lgna.story.Model getModel() {
		return model;
	}
}
