package org.lgna.story.event;

public abstract class ViewEvent extends AbstractEvent {
	private final org.lgna.story.SModel model;

	public ViewEvent( org.lgna.story.SModel model ) {
		this.model = model;
	}

	public org.lgna.story.SModel getModel() {
		return model;
	}
}
