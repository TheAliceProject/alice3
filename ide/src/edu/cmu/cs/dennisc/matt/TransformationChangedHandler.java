package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.Model;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;


public class TransformationChangedHandler extends AbstractEventHandler implements AbsoluteTransformationListener{

	//	HashMap<Model, LinkedList<Object>> eventMap = new HashMap<Model, LinkedList<Object>>();
	LinkedList<TransformationListener> listenerList = new LinkedList<TransformationListener>();
	LinkedList<Model> modelList = new LinkedList<Model>();
	protected boolean shouldFire = true;
	private CollisionEventHandler collisionEventHandler = new CollisionEventHandler();

	public void silenceListeners(){
		shouldFire = false;
	}
	public void restoreListeners(){
		shouldFire = true;
	}

	public void fireAllTargeted(Model changedEntity) {
		if(shouldFire){
			collisionEventHandler.check(changedEntity);
		}
	}

	public void absoluteTransformationChanged(AbsoluteTransformationEvent absoluteTransformationEvent){
		Entity source = EntityImp.getAbstractionFromSgElement(absoluteTransformationEvent.getTypedSource());
		if(source instanceof Model){
			fireAllTargeted((Model) source);
		}
	}
	public void addCollisionListener(CollisionListener collisionListener,
			LinkedList<Model> groupOne, LinkedList<Model> groupTwo) {
		policyMap.put(collisionListener, EventPolicy.IGNORE);
		isFiringMap.put(collisionListener, false);
		LinkedList<Model> allObserving = groupOne;
		allObserving.addAll(groupTwo);
		for(Model m: allObserving){
			if(!modelList.contains(m)){
				modelList.add(m);
				ImplementationAccessor.getImplementation(m).getSgComposite().addAbsoluteTransformationListener(this);
				collisionEventHandler.register(collisionListener, groupOne, groupTwo);
			}
		}
	}
	@Override
	protected void fire(Object event, LinkedList<Model> targets) {
		if(event instanceof CollisionListener){
			((CollisionListener) event).whenTheseCollide(targets);
		}
	}

	protected class CollisionEventHandler{

		HashMap<Model, LinkedList<Model>> checkMap = new HashMap<Model, LinkedList<Model>>();
		HashMap<Model, HashMap<Model, LinkedList<CollisionListener>>> eventMap = new HashMap<Model, HashMap<Model,LinkedList<CollisionListener>>>();

		public void check(Model changedEntity) {
			for(Model m: checkMap.get(changedEntity)){
				if(AabbCollisionDetector.doTheseCollide(m, changedEntity)){
					LinkedList<Model> models = new LinkedList<Model>();
					models.add(changedEntity);
					models.add(m);
					for(CollisionListener colList: eventMap.get(changedEntity).get(m)){
						fireEvent(colList, models);
					}
				}
			}
		}

		public void register(CollisionListener collisionListener,
				LinkedList<Model> groupOne, LinkedList<Model> groupTwo) {
			for(Model m: groupOne){
				if(eventMap.get(m) == null){
					eventMap.put(m, new HashMap<Model, LinkedList<CollisionListener>>());
					checkMap.put(m, new LinkedList<Model>());
				}
				for(Model t: groupTwo){
					if(eventMap.get(m).get(t) == null){
						eventMap.get(m).put(t, new LinkedList<CollisionListener>());
					}
					eventMap.get(m).get(t).add(collisionListener);
					checkMap.get(m).add(t);
				}
			}
			for(Model m: groupTwo){
				if(eventMap.get(m) == null){
					eventMap.put(m, new HashMap<Model, LinkedList<CollisionListener>>());
					checkMap.put(m, new LinkedList<Model>());
				}
				for(Model t: groupOne){
					if(eventMap.get(m).get(t) == null){
						eventMap.get(m).put(t, new LinkedList<CollisionListener>());
					}
					eventMap.get(m).get(t).add(collisionListener);
					checkMap.get(m).add(t);
				}
			}
		}
	}
}
