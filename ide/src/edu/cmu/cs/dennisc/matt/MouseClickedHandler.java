package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;

import org.lgna.story.Model;


public class MouseClickedHandler extends AbstractEventHandler{

	HashMap<Model, LinkedList<MouseClickedListener>> map = new HashMap<Model, LinkedList<MouseClickedListener>>();

	public void fireAllTargeted(Model modelAtMouseLocation) {
		if(shouldFire){
			if(modelAtMouseLocation != null){
				LinkedList<MouseClickedListener> events = map.get(modelAtMouseLocation);
				if(events != null){
					LinkedList<Model> list = new LinkedList<Model>();
					list.add(modelAtMouseLocation);
					for(MouseClickedListener event: events){
						fireEvent(event, list);
					}
				}
			}
		}
	}
	public void addListener(MouseClickedListener mouseButtonListener,
			EventPolicy eventPolicy, LinkedList<Model> targets) {
		isFiringMap.put(mouseButtonListener, false);
		policyMap.put(mouseButtonListener, eventPolicy);
		for(Model target: targets){
			if(map.get(target) != null){
				map.get(target).add(mouseButtonListener);
			} else{
				LinkedList<MouseClickedListener> list = new LinkedList<MouseClickedListener>();
				list.add(mouseButtonListener);
				map.put(target, list);
			}
		}
	}
	@Override
	protected void fire(Object event, LinkedList<Model> targets) {
		((MouseClickedListener) event).mouseButtonClicked(targets.getFirst());
	}
}
