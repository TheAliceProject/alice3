package org.lgna.story.event;

public interface StartCollisionListener extends CollisionListener{
	
	public void whenTheseCollide( StartCollisionEvent e );
	
}
