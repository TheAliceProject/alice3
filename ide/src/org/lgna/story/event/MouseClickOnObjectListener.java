package org.lgna.story.event;

public interface MouseClickOnObjectListener<T extends org.lgna.story.Model> {
	public void mouseClicked( MouseClickOnObjectEvent<T> e );
}
