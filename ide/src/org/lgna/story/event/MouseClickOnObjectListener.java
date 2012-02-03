package org.lgna.story.event;

import org.lgna.story.Visual;

public interface MouseClickOnObjectListener /*< T extends Visual >*/ extends AbstractMouseClickListener {
	public void mouseClicked( MouseClickedOnObjectEvent e );
}
