package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lgna.story.Entity;
import org.lgna.story.Model;
import org.lgna.story.event.AbstractEvent;
import org.lgna.story.implementation.EntityImp;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;

public abstract class TransformationChangedHandler<L, E extends AbstractEvent> extends AbstractEventHandler< L, E > implements AbsoluteTransformationListener {

	HashMap<Model, LinkedList<Object>> eventMap = new HashMap<Model, LinkedList<Object>>();
	List< L > listenerList = Collections.newLinkedList();
	List< Model > modelList = Collections.newLinkedList();

	public final void fireAllTargeted( Model changedEntity ){
		if( shouldFire ) {
			check( changedEntity );
		}
	}

	protected abstract void check( Model changedEntity );

	public final void absoluteTransformationChanged( AbsoluteTransformationEvent absoluteTransformationEvent ) {
		Entity source = EntityImp.getAbstractionFromSgElement( absoluteTransformationEvent.getTypedSource() );
		if( source instanceof Model ) {
			fireAllTargeted( (Model)source );
		}
	}
}
