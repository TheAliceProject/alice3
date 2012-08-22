package org.lgna.story.event;

public class WhileInViewEvent<A extends org.lgna.story.SModel> extends TimeEvent {

	private A enteringView;

	public WhileInViewEvent( Class<A> clsOne, Double timeSinceLastFire, A enteringView ) {
		super( timeSinceLastFire );
		this.enteringView = enteringView;
	}

	public A getEnteringView() {
		return enteringView;
	}

}
