package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.PointOfViewEvent;

import edu.cmu.cs.dennisc.java.util.Collections;

public class TransformationHandler extends TransformationChangedHandler<PointOfViewChangeListener,PointOfViewEvent> {

	private Map<Entity,List<PointOfViewChangeListener>> checkMap = Collections.newHashMap();

	public void addTransformationListener( PointOfViewChangeListener transformationlistener, Entity[] shouldListenTo ) {
		registerIsFiringMap( transformationlistener );
		registerPolicyMap( transformationlistener, MultipleEventPolicy.IGNORE );
		List<Entity> allObserving = Collections.newArrayList( shouldListenTo );
		for( Entity m : allObserving ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
				//				collisionEventHandler.register( collisionListener, groupOne, groupTwo );
			}
		}
		for( Entity e : shouldListenTo ) {
			if( checkMap.get( e ) == null ) {
				checkMap.put( e, new LinkedList<PointOfViewChangeListener>() );
			}
			checkMap.get( e ).add( transformationlistener );
		}
	}

	@Override
	protected void ammend( Object key, int i, Entity newObject ) {
	}

	@Override
	protected void check( Entity changedEntity ) {
		for( PointOfViewChangeListener listener : checkMap.get( changedEntity ) ) {
			fireEvent( listener, new PointOfViewEvent( changedEntity ) );
		}
	}

	@Override
	protected void nameOfFireCall( PointOfViewChangeListener listener, PointOfViewEvent event ) {
		listener.pointOfViewChanged( event );
	}
}
