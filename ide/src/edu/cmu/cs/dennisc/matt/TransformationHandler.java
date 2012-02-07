package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.TransformationEvent;
import org.lgna.story.event.TransformationListener;

import edu.cmu.cs.dennisc.java.util.Collections;

public class TransformationHandler  extends TransformationChangedHandler < TransformationListener, TransformationEvent > {

	private Map< Entity, List < TransformationListener > > checkMap = Collections.newHashMap();

	public void addTransformationListener( TransformationListener transformationlistener, Entity[] shouldListenTo ) {
		registerIsFiringMap(transformationlistener);
		registerPolicyMap(transformationlistener, MultipleEventPolicy.IGNORE);
		List< Entity > allObserving = Collections.newArrayList( shouldListenTo );
		for( Entity m : allObserving ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
//				collisionEventHandler.register( collisionListener, groupOne, groupTwo );
			}
		}
		for ( Entity e: shouldListenTo ){
			if( checkMap.get( e ) == null ) {
				checkMap.put( e, new LinkedList< TransformationListener >() );
			}
			checkMap.get(e).add( transformationlistener );
		}
	}

	@Override
	protected void check(Entity changedEntity) {
		for( TransformationListener listener : checkMap.get( changedEntity ) ){
			fireEvent( listener, new TransformationEvent( changedEntity ) );
		}
	}

	@Override
	protected void nameOfFireCall(TransformationListener listener, TransformationEvent event) {
		listener.whenThisMoves( event );
	}
}
