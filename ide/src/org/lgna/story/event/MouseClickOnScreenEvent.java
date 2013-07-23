package org.lgna.story.event;

public class MouseClickOnScreenEvent extends MouseClickEvent {
	public MouseClickOnScreenEvent( MouseClickEvent event ) {
		super( event.getEvent(), event.getScene() );
	}
}
