package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;

import org.lgna.story.Model;


public class MouseClickedManager {

	HashMap<Model, LinkedList<MouseClickedListener>> map = new HashMap<Model, LinkedList<MouseClickedListener>>();
	protected boolean shouldFire = true;

	public void silenceListeners(){
		shouldFire = false;
	}
	public void restoreListeners(){
		shouldFire = true;
	}
	
	public void addListener(AbstractListener listener) {
		if(listener instanceof MouseClickedListener){
			MouseClickedListener mouseListener = (MouseClickedListener) listener; 
			LinkedList<Model> targets = mouseListener.getTargets();
			for(Model target: targets){
				if(map.get(target) != null){
					map.get(target).add(mouseListener);
				} else{
					LinkedList<MouseClickedListener> list = new LinkedList<MouseClickedListener>();
					list.add(mouseListener);
					map.put(target, list);
				}
			}
		}
	}

	public void fireAllTargeted(Model modelAtMouseLocation) {
		if(shouldFire){
			if(modelAtMouseLocation != null){
				LinkedList<MouseClickedListener> events = map.get(modelAtMouseLocation);
				if(events != null){
					LinkedList<Model> list = new LinkedList<Model>();
					list.add(modelAtMouseLocation);
					for(MouseClickedListener event: events){
						event.fireEvent(list);
					}
				}
			}
		}
	}
}
