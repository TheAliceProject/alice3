package org.lgna.story.event;

public interface EndCollisionListener extends CollisionListener {

	public void whenTheseStopColliding( EndCollisionEvent e );
	
}
