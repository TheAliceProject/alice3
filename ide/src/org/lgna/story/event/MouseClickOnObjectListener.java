package org.lgna.story.event;

public interface MouseClickOnObjectListener<T extends org.lgna.story.SModel> {
	public void mouseClicked( MouseClickOnObjectEvent<T> e );
}
