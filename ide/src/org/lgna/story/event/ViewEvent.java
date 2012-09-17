package org.lgna.story.event;

public abstract class ViewEvent<A extends org.lgna.story.SModel> extends AbstractEvent {
	private final A a;

	public ViewEvent( A model ) {
		this.a = model;
	}

	public A getEnteringView() {
		return a;
	}
}
