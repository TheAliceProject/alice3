package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.ComesIntoViewEventListener;
import org.lgna.story.event.LeavesViewEvent;
import org.lgna.story.event.LeavesViewEventListener;
import org.lgna.story.event.ViewEvent;
import org.lgna.story.event.ViewEventListener;
import org.lgna.story.implementation.CameraImp;

import edu.cmu.cs.dennisc.java.util.Collections;

public class ViewEventHandler extends TransformationChangedHandler < ViewEventListener, ViewEvent > {

	private CameraImp camera;
	private Map < Entity, List< ViewEventListener > > map = Collections.newHashMap();
	private Map<Entity, Boolean > wasInView = Collections.newHashMap();


	@Override
	protected void check(Entity changedEntity) {
		System.out.println("check");
		if ( camera == null ){
			camera = ImplementationAccessor.getImplementation( changedEntity ).getScene().findFirstCamera();
			if( camera == null ){
				return;
			}
		}
		for( ViewEventListener listener : map.get( changedEntity ) ) {
			if ( check( listener, changedEntity ) ) {
				ViewEvent event = listener instanceof ComesIntoViewEventListener ? new ComesIntoViewEvent( changedEntity ) : new LeavesViewEvent( changedEntity );
				fireEvent( listener, event );
			}
		}
		wasInView.put( changedEntity, IsInViewDetector.isThisInView( changedEntity, camera ) );
	}
	
	private boolean check( ViewEventListener listener, Entity changedEntity ) {
		boolean rv = false;
		boolean thisInView = IsInViewDetector.isThisInView( changedEntity, camera );
		if (listener instanceof ComesIntoViewEventListener) {
			if ( thisInView && !wasInView.get( changedEntity ) ) {
				rv = true;
			}
		} else if (listener instanceof LeavesViewEventListener) {
			if ( !thisInView && wasInView.get( changedEntity ) ) {
				rv = true;
			}
		}
		return rv;
	}

	@Override
	protected void nameOfFireCall(ViewEventListener listener, ViewEvent event) {
		if (listener instanceof ComesIntoViewEventListener) {
			ComesIntoViewEventListener intoViewEL = ( ComesIntoViewEventListener ) listener;
			intoViewEL.cameIntoView( ( ComesIntoViewEvent ) event );
		} else if (listener instanceof LeavesViewEventListener) {
			LeavesViewEventListener outOfViewEL = ( LeavesViewEventListener ) listener;
			outOfViewEL.leftView( ( LeavesViewEvent ) event );
		}
	}

	public void addViewEventListener( ViewEventListener listener, Entity[] entities) {
		registerIsFiringMap( listener );
		System.out.println("foo");
		registerPolicyMap( listener, MultipleEventPolicy.IGNORE );
		for( Entity m : entities ) {
			System.out.println("for1");
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
				if ( camera == null ) {
					camera = ImplementationAccessor.getImplementation( m ).getScene().findFirstCamera();
					camera.getSgComposite().addAbsoluteTransformationListener( this );
				}
				wasInView.put( m, IsInViewDetector.isThisInView( m, camera) );
			}
		}
		for( Entity entity : entities ){
			System.out.println("for2");
			if ( map.get( entity ) == null ){
				map.put( entity, new LinkedList<ViewEventListener>() );
			}
			map.get( entity ).add( listener );
		}
	}

}
