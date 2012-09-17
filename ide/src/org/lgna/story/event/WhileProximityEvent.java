package org.lgna.story.event;

public class WhileProximityEvent<A extends org.lgna.story.SMovableTurnable, B extends org.lgna.story.SMovableTurnable> extends TimeEvent {

	private A collidingFromA;
	private B collidingFromB;

	public WhileProximityEvent( Class<A> clsOne, Class<B> clsTwo, Double timeElapsed, A a, B b ) {
		super( timeElapsed );
		this.collidingFromA = a;
		this.collidingFromB = b;
	}

	public A getCollidingFromA() {
		return collidingFromA;
	}

	public B getCollidingFromB() {
		return collidingFromB;
	}

}
