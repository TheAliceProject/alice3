package org.lgna.story.event;

public class MouseClickOnObjectEvent extends MouseClickEvent {

	public MouseClickOnObjectEvent( MouseClickEvent e ) {
		super( e.getEvent(), e.getScene() );
	}
}
