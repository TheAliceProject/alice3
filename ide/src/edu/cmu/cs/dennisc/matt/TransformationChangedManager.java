package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.Model;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;


public class TransformationChangedManager extends AbstractEventManager implements AbsoluteTransformationListener{

	HashMap<Model, LinkedList<TransformationListener>> map = new HashMap<Model, LinkedList<TransformationListener>>();
	LinkedList<TransformationListener> listenerList = new LinkedList<TransformationListener>();
	LinkedList<Model> modelList = new LinkedList<Model>();
	CollisionEventManager collision = new CollisionEventManager();

	@Override
	public void addListener(AbstractListener listener) {
		if(listener instanceof TransformationListener){
			if(listener instanceof CollisionListener){
				collision.addListener((CollisionListener) listener);
			}
			for(Model m: ((TransformationListener) listener).getObserving()){
				if(!modelList.contains(m)){
					modelList.add(m);
					ImplementationAccessor.getImplementation(m).getSgComposite().addAbsoluteTransformationListener(this);
				}
			}
		}
	}

	@Override
	public void fireAllTargeted(Model changedEntity) {
		if(shouldFire){
			collision.fireRelevantEvents(changedEntity);
		}
	}

	public void absoluteTransformationChanged(AbsoluteTransformationEvent absoluteTransformationEvent){
		Entity source = EntityImp.getAbstractionFromSgElement(absoluteTransformationEvent.getTypedSource());
		if(source instanceof Model)
		fireAllTargeted((Model) source);
	}

}
