package org.lgna.story.event;

public class MouseClickOnObjectEvent<T extends org.lgna.story.SModel> extends MouseClickEvent {
	public MouseClickOnObjectEvent( java.awt.event.MouseEvent e, org.lgna.story.SScene scene ) {
		super( e, scene );
	}
	public MouseClickOnObjectEvent( MouseClickEvent e ) {
		super( e.e, e.scene );
		this.isPickPerformed = true;
		this.modelAtMouseLocation = e.getModelAtMouseLocation();
	}
	public T getObjectAtMouseLocation() {
		org.lgna.story.SModel m = super.getModelAtMouseLocation();
		try {
			return (T)m;
		} catch( ClassCastException e ) {
			return null;
		}
	}
}
