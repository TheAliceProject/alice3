package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SThing;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.PointOfViewEvent;

import edu.cmu.cs.dennisc.java.util.concurrent.Collections;

public class TransformationHandler extends TransformationChangedHandler<PointOfViewChangeListener, PointOfViewEvent> {

	private Map<SThing, List<PointOfViewChangeListener>> checkMap = Collections.newConcurrentHashMap();

	public void addTransformationListener( PointOfViewChangeListener transformationlistener, SThing[] shouldListenTo ) {
		registerIsFiringMap( transformationlistener );
		registerPolicyMap( transformationlistener, MultipleEventPolicy.IGNORE );
		List<SThing> allObserving = Collections.newCopyOnWriteArrayList( shouldListenTo );
		for( SThing m : allObserving ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
				//				collisionEventHandler.register( collisionListener, groupOne, groupTwo );
			}
		}
		for( SThing e : shouldListenTo ) {
			if( checkMap.get( e ) == null ) {
				checkMap.put( e, new LinkedList<PointOfViewChangeListener>() );
			}
			checkMap.get( e ).add( transformationlistener );
		}
	}

	@Override
	protected void check( SThing changedEntity ) {
		for( PointOfViewChangeListener listener : checkMap.get( changedEntity ) ) {
			fireEvent( listener, new PointOfViewEvent( changedEntity ) );
		}
	}

	@Override
	protected void nameOfFireCall( PointOfViewChangeListener listener, PointOfViewEvent event ) {
		listener.pointOfViewChanged( event );
	}
}
