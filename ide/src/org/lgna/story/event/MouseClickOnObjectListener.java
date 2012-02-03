package org.lgna.story.event;

public interface MouseClickOnObjectListener /*< T extends Visual >*/ extends AbstractMouseClickListener {
	public void mouseClicked( MouseClickedOnObjectEvent e );
}
