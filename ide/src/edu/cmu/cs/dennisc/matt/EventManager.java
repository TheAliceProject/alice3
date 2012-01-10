package edu.cmu.cs.dennisc.matt;

import org.lgna.story.event.KeyEvent;
import org.lgna.story.event.KeyListener;
import org.lgna.story.event.MouseButtonEvent;
import org.lgna.story.event.MouseButtonListener;

public class EventManager implements MouseButtonListener, KeyListener{
	
	MouseClickedManager mouse = new MouseClickedManager();
	TransformationChangedManager trans = new TransformationChangedManager();
	KeyPressedManager key = new KeyPressedManager();
	
	
	public void addListener(AbstractListener event) {
		if(event instanceof MouseClickedListener){
			mouse.addListener((MouseClickedListener) event);
		}
		if(event instanceof TransformationListener){
			trans.addListener(event);
		}
		if(event instanceof KeyPressedListener){
			key.addListener(event);
		}
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

}
