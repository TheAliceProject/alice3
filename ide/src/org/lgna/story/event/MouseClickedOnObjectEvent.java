package org.lgna.story.event;

import java.awt.event.MouseEvent;

import org.lgna.story.Scene;

public class MouseClickedOnObjectEvent /*< T extends Visual >*/ extends MouseButtonEvent {

	public MouseClickedOnObjectEvent(MouseEvent e, Scene scene) {
		super(e, scene);
	}

	public MouseClickedOnObjectEvent( MouseButtonEvent e ) {
		super( e.e, e.scene );
	}
	
}
