package org.lgna.story.event;

public class MouseClickOnObjectEvent extends MouseClickEvent {
	public MouseClickOnObjectEvent( java.awt.event.MouseEvent e, org.lgna.story.Scene scene ) {
		super( e, scene );
	}
	public MouseClickOnObjectEvent( MouseClickEvent e ) {
		super( e.e, e.scene );
	}
}
