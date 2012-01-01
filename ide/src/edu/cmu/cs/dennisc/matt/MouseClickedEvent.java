package edu.cmu.cs.dennisc.matt;

import org.lgna.story.Model;
import org.lgna.story.Scene;
import org.lgna.story.event.MouseButtonEvent;
import org.lgna.story.event.MouseButtonListener;

public class MouseClickedEvent extends AbstractEvent implements MouseButtonListener {

	private Model target;
	private Scene scene;

	public MouseClickedEvent(Scene scene, Model target){
		this.target = target;
		this.scene = scene;
	}

	@Override
	public void init() {
		target.addMouseButtonListener(this);
	}

	@Override
	public void cleanUp() {
		target.removeMouseButtonListener(this);
	}

	@Override
	protected void fire() {
		target.setOpacity(0);
	}

	public void mouseButtonClicked(MouseButtonEvent e) {
		if(!isFiring()){
			fireEvent();
			System.out.println("FIRE");
		}
	}

}
