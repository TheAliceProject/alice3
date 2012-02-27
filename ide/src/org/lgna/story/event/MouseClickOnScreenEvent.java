package org.lgna.story.event;

import java.awt.event.MouseEvent;

import org.lgna.story.Scene;

public class MouseClickOnScreenEvent extends MouseClickEvent {

	public MouseClickOnScreenEvent(MouseEvent e, Scene scene) {
		super(e, scene);
	}

	public MouseClickOnScreenEvent( MouseClickEvent e ) {
		super( e.e, e.scene );
	}
	
}
