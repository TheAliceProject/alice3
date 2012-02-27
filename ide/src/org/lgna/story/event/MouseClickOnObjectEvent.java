package org.lgna.story.event;

import java.awt.event.MouseEvent;

import org.lgna.story.Scene;

public class MouseClickOnObjectEvent extends MouseClickEvent {

	public MouseClickOnObjectEvent( MouseEvent e, Scene scene ) {
		super( e, scene );
	}

	public MouseClickOnObjectEvent( MouseClickEvent e ) {
		super( e.e, e.scene );
	}

}
