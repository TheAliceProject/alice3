package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lgna.story.SThing;
import org.lgna.story.Visual;
import org.lgna.story.event.AbstractEvent;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;

public abstract class TransformationChangedHandler<L, E extends AbstractEvent> extends AbstractEventHandler<L, E> implements AbsoluteTransformationListener {

	HashMap<Visual, LinkedList<Object>> eventMap = new HashMap<Visual, LinkedList<Object>>();
	List<L> listenerList = Collections.newLinkedList();
	List<SThing> modelList = Collections.newLinkedList();

	public final void fireAllTargeted( SThing changedEntity ) {
		if( shouldFire ) {
			check( changedEntity );
		}
	}

	protected abstract void check( SThing changedEntity );

	public final void absoluteTransformationChanged( AbsoluteTransformationEvent absoluteTransformationEvent ) {
		SThing source = EntityImp.getAbstractionFromSgElement( absoluteTransformationEvent.getTypedSource() );
		fireAllTargeted( source );
		//		if( source instanceof Turnable ) {
		//			fireAllTargeted( (Turnable)source );
		//		} else {
		//			Logger.severe( source );
		//		}
	}
}
