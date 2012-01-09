package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;

import org.lgna.story.Model;

public class CollisionEventManager {

	HashMap<Model, LinkedList<Model>> modelMap = new HashMap<Model, LinkedList<Model>>();
	LinkedList<Model> modelList = new LinkedList<Model>();
	
	HashMap<Model, HashMap<Model, LinkedList<CollisionListener>>> eventMap = 
			new HashMap<Model, HashMap<Model,LinkedList<CollisionListener>>>();
					//this unreadable clusterfuck of a datastructure is so we only have to check for collisions 1 time @dennisc

	public void addListener(CollisionListener cListener){
		for(Model m: ((TransformationListener) cListener).getObserving()){
			if(!modelList.contains(m)){
				modelList.add(m);
			}
		}
		for(Model m: cListener.groupOne){
			if(!modelMap.containsKey(m)){
				modelMap.put(m, new LinkedList<Model>());
			}
			for(Model t: cListener.groupTwo){
				if(!modelMap.get(m).contains(t)){
					modelMap.get(m).add(t);
				}
				if(eventMap.get(m) == null){
					eventMap.put(m, new HashMap<Model, LinkedList<CollisionListener>>());
				}
				if(eventMap.get(m).get(t) == null){
					eventMap.get(m).put(t, new LinkedList<CollisionListener>());
				}
				eventMap.get(m).get(t).add(cListener);
			}
		}
		for(Model m: cListener.groupTwo){
			if(!modelMap.containsKey(m)){
				modelMap.put(m, new LinkedList<Model>());
			}
			for(Model t: cListener.groupOne){
				if(modelMap.get(m).contains(t)){
					modelMap.get(m).add(t);
				}
				if(eventMap.get(m) == null){
					eventMap.put(m, new HashMap<Model, LinkedList<CollisionListener>>());
				}
				if(eventMap.get(m).get(t) == null){
					eventMap.get(m).put(t, new LinkedList<CollisionListener>());
				}
				eventMap.get(m).get(t).add(cListener);
			}
		}
	}

	public void fireRelevantEvents(Model changed){
		for(Model m: modelMap.get(changed)){
			if(AabbCollisionDetector.doTheseCollide(m, changed)){
				for(CollisionListener event: eventMap.get(changed).get(m)){
					LinkedList<Model> list = new LinkedList<Model>();
					list.add(m);
					list.add(changed);
					event.fireEvent(list);
				}
			}
		}
	}
}
