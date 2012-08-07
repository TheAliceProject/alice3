package org.lgna.story.event;

public class OcclusionEvent<A extends org.lgna.story.SModel, B extends org.lgna.story.SModel> extends AbstractEvent {
	private org.lgna.story.SModel foreground;
	private org.lgna.story.SModel background;
	private A a;
	private B b;

	public OcclusionEvent( A a, B b ) {
		this.a = a;
		this.b = b;
	}

	public void setForeground( org.lgna.story.SModel foreground ) {
		if( a.equals( foreground ) ) {
			foreground = a;
			background = b;
		} else if( b.equals( foreground ) ) {
			foreground = b;
			background = a;
		}
	}

	public org.lgna.story.SModel getForeground() {
		return foreground;
	}
	public org.lgna.story.SModel getBackground() {
		return background;
	}

	public A getFromA() {
		return a;
	}
	public B getFromB() {
		return b;
	}
}
