package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;

import org.lgna.story.Model;

import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;


public class TransformationChangedManager extends AbstractEventManager implements AbsoluteTransformationListener{

	HashMap<Model, LinkedList<TransformationListener>> map = new HashMap<Model, LinkedList<TransformationListener>>();

	@Override
	public void addListener(AbstractListener listener) {
		if(listener instanceof TransformationListener){
			TransformationListener tranList = (TransformationListener) listener;
			for(Model m: tranList.getObserving()){
				if(map.containsKey(m)){
					//pass
				}else{
					if(map.get(m) != null){
						map.get(m).add(tranList);
					}
					else{
						map.put(m, new LinkedList<TransformationListener>());
						map.get(m).add(tranList);
						m.getImplementation().getSgComposite().addAbsoluteTransformationListener(this);
					}
				}
			}
		}
	}

	@Override
	public void fireAllTargeted(Model changedEntity) {
		// TODO Auto-generated method stub

	}

	public void absoluteTransformationChanged(
			AbsoluteTransformationEvent absoluteTransformationEvent) {
		// TODO Auto-generated method stub

	}

}
