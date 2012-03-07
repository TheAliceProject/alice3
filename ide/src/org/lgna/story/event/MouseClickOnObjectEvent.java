package org.lgna.story.event;

public class MouseClickOnObjectEvent<T extends org.lgna.story.Model> extends MouseClickEvent {
	public MouseClickOnObjectEvent( java.awt.event.MouseEvent e, org.lgna.story.Scene scene ) {
		super( e, scene );
	}
	public MouseClickOnObjectEvent( MouseClickEvent e ) {
		super( e.e, e.scene );
	}
	public T getObjectAtMouseLocation() {
		org.lgna.story.Model m = super.getModelAtMouseLocation();
		try {
			return (T)m;
		} catch( ClassCastException e ) {
			return null;
		}
	}
}
