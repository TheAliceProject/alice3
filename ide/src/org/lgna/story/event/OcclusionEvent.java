package org.lgna.story.event;

public class OcclusionEvent<A extends org.lgna.story.Model, B extends org.lgna.story.Model> extends AbstractEvent {

	private org.lgna.story.Model foreground;
	private org.lgna.story.Model background;
	private A a;
	private B b;

	public OcclusionEvent( A a, B b ) {
		this.a = a;
		this.b = b;
	}

	public void setForeground( org.lgna.story.Model foreground ) {
		if( a.equals( foreground ) ) {
			foreground = a;
			background = b;
		} else if( b.equals( foreground ) ) {
			foreground = b;
			background = a;
		}
	}

	public org.lgna.story.Model getForeground() {
		return foreground;
	}
	public org.lgna.story.Model getBackground() {
		return background;
	}

	public A getFromA() {
		return a;
	}

	public B getFromB() {
		return b;
	}

}
