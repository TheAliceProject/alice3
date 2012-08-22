package org.lgna.story.event;

public class WhileCollisionEvent<A extends org.lgna.story.SMovableTurnable, B extends org.lgna.story.SMovableTurnable> extends TimeEvent {

	private A collidingFromA;
	private B collidingFromB;

	public WhileCollisionEvent( Class<A> clsOne, Class<B> clsTwo, Double timeElapsed, A a, B b ) {
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
