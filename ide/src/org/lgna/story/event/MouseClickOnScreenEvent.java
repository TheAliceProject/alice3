package org.lgna.story.event;

public class MouseClickOnScreenEvent extends MouseClickEvent {
	public MouseClickOnScreenEvent( java.awt.event.MouseEvent e, org.lgna.story.SScene scene ) {
		super( e, scene );
	}
	public MouseClickOnScreenEvent( MouseClickEvent e ) {
		super( e.e, e.scene );
		this.isPickPerformed = true;
	}
}
