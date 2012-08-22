package org.lgna.story.event;

public class WhileOccludingEvent<A extends org.lgna.story.SModel, B extends org.lgna.story.SModel> extends TimeEvent {

	private A occludingFromA;
	private B occludingFromB;
	private org.lgna.story.SModel foreground;
	private org.lgna.story.SModel background;

	public WhileOccludingEvent( Class<A> clsOne, Class<B> clsTwo, Double timeElapsed, A a, B b ) {
		super( timeElapsed );
		this.occludingFromA = a;
		this.occludingFromB = b;
	}
	
	public A getOccludingFromA() {
		return occludingFromA;
	}
	public B getOccludingFromB() {
		return occludingFromB;
	}
	public org.lgna.story.SModel getForeground() {
		return foreground;
	}
	public org.lgna.story.SModel getBackground() {
		return background;
	}

	public void setForeground( org.lgna.story.SModel foreground ) {
		if( occludingFromA.equals( foreground ) ) {
			this.foreground = occludingFromA;
			this.background = occludingFromB;
		} else if( occludingFromB.equals( foreground ) ) {
			this.foreground = occludingFromB;
			this.background = occludingFromA;
		}
	}

}
