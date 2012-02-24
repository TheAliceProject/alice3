package org.lgna.story.event;

import java.awt.event.MouseEvent;

import org.lgna.story.Scene;

public class MouseClickedOnScreenEvent extends MouseButtonEvent {

	public MouseClickedOnScreenEvent(MouseEvent e, Scene scene) {
		super(e, scene);
	}

	public MouseClickedOnScreenEvent( MouseButtonEvent e ) {
		super( e.e, e.scene );
	}
	
}
