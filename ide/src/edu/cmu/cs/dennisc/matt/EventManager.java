package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;

import org.lgna.story.Model;
import org.lgna.story.event.KeyEvent;
import org.lgna.story.event.KeyListener;
import org.lgna.story.event.MouseButtonEvent;
import org.lgna.story.event.MouseButtonListener;

public class EventManager implements MouseButtonListener, KeyListener{
	
	MouseClickedHandler mouse = new MouseClickedHandler();
	TransformationChangedHandler trans = new TransformationChangedHandler();
	KeyPressedHandler key = new KeyPressedHandler();
	
	
	public void addListener(AbstractEventHandler event) {
//		if(event instanceof MouseClickedListener){
//			mouse.addListener((MouseClickedListener) event);
//		}
//		if(event instanceof TransformationListener){
//			trans.addListener(event);
//		}
//		if(event instanceof KeyPressedListener){
//			key.addListener(event);
//		}
	}

	public void mouseButtonClicked(MouseButtonEvent e) {
		mouse.fireAllTargeted(e.getModelAtMouseLocation());
	}

	public void keyPressed(KeyEvent e) {
		key.fireAllTargeted(e);
	}
	
	public void silenceAllListeners() {
		mouse.silenceListeners();
	}

	public void restoreAllListeners() {
		mouse.restoreListeners();
	}

	public void addMouseButtonListener(MouseClickedListener mouseButtonListener,
			EventPolicy eventPolicy, LinkedList<Model> targets) {
		mouse.addListener(mouseButtonListener, eventPolicy, targets);
	}
	
	public void addKeyPressedListener(KeyPressedListener keyList, EventPolicy policy){
		key.addKeyPressedListener(keyList, policy);
	}

	public void addCollisionListener(CollisionListener collisionListener,
			LinkedList<Model> groupOne, LinkedList<Model> groupTwo) {
		trans.addCollisionListener(collisionListener, groupOne, groupTwo);
	}

}
