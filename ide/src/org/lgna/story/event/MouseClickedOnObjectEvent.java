package org.lgna.story.event;

import java.awt.event.MouseEvent;

import org.lgna.story.Scene;

public class MouseClickedOnObjectEvent /*< T extends Visual >*/ extends MouseButtonEvent {

	public MouseClickedOnObjectEvent(MouseEvent e, Scene scene) {
		super(e, scene);
		// TODO Auto-generated constructor stub
	}

}
