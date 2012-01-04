package edu.cmu.cs.dennisc.matt;

import org.lgna.story.event.MouseButtonEvent;
import org.lgna.story.event.MouseButtonListener;

public class EventManager implements MouseButtonListener {
	
	MouseClickedManager mouse = new MouseClickedManager();
	TransformationChangedManager trans = new TransformationChangedManager();
	
	
	public void addListener(AbstractListener event) {
		if(event instanceof MouseClickedListener){
			mouse.addListener((MouseClickedListener) event);
		}
		if(event instanceof TransformationListener){
			trans.addListener(event);
		}
	}

	public void mouseButtonClicked(MouseButtonEvent e) {
		mouse.fireAllTargeted(e.getModelAtMouseLocation());
	}
	
	public void silenceAllListeners() {
		mouse.silenceListeners();
	}

	public void restoreAllListeners() {
		mouse.restoreListeners();
	}

}
