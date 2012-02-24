package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.EnterViewListener;
import org.lgna.story.event.ExitViewListener;
import org.lgna.story.event.LeavesViewEvent;
import org.lgna.story.event.ViewEvent;
import org.lgna.story.implementation.CameraImp;

import edu.cmu.cs.dennisc.java.util.Collections;

public class ViewEventHandler extends TransformationChangedHandler < Object, ViewEvent > {

	private CameraImp camera;
	private Map < Entity, List< Object > > map = Collections.newHashMap();
	private Map<Entity, Boolean > wasInView = Collections.newHashMap();


	@Override
	protected void check(Entity changedEntity) {
		if ( camera == null ){ //should not really be hit
			camera = ImplementationAccessor.getImplementation( changedEntity ).getScene().findFirstCamera();
			if( camera == null ){
				return;
			} else {
				camera.getSgComposite().addAbsoluteTransformationListener( this );
			}
		}
		if ( changedEntity == camera.getAbstraction() ) {
			for( Entity entity : map.keySet() ) {
				for( Object listener : map.get( entity ) ) {
					if ( check( listener, entity ) ) {
						ViewEvent event = listener instanceof EnterViewListener ? new ComesIntoViewEvent( changedEntity ) : new LeavesViewEvent( changedEntity );
						fireEvent( listener, event );
					}
				}
				wasInView.put( entity, IsInViewDetector.isThisInView( entity, camera ) );
			}
		} else {
			for( Object listener : map.get( changedEntity ) ) {
				if ( check( listener, changedEntity ) ) {
					ViewEvent event = listener instanceof EnterViewListener ? new ComesIntoViewEvent( changedEntity ) : new LeavesViewEvent( changedEntity );
					fireEvent( listener, event );
				}
			}
			wasInView.put( changedEntity, IsInViewDetector.isThisInView( changedEntity, camera ) );
		}
	}

	private boolean check( Object listener, Entity changedEntity ) {
		boolean rv = false;
		boolean thisInView = IsInViewDetector.isThisInView( changedEntity, camera );
		if (listener instanceof EnterViewListener) {
			if ( thisInView && !wasInView.get( changedEntity ) ) {
				rv = true;
			}
		} else if (listener instanceof ExitViewListener) {
			if ( !thisInView && wasInView.get( changedEntity ) ) {
				rv = true;
			}
		}
		return rv;
	}

	@Override
	protected void nameOfFireCall( Object listener, ViewEvent event) {
		if (listener instanceof EnterViewListener) {
			EnterViewListener intoViewEL = ( EnterViewListener ) listener;
			intoViewEL.cameIntoView( ( ComesIntoViewEvent ) event );
		} else if (listener instanceof ExitViewListener) {
			ExitViewListener outOfViewEL = ( ExitViewListener ) listener;
			outOfViewEL.leftView( ( LeavesViewEvent ) event );
		}
	}

	public void addViewEventListener( Object listener, Entity[] entities) {
		registerIsFiringMap( listener );
		registerPolicyMap( listener, MultipleEventPolicy.IGNORE );
		for( Entity m : entities ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
				if ( camera == null ) {
					camera = ImplementationAccessor.getImplementation( m ).getScene().findFirstCamera();
					camera.getSgComposite().addAbsoluteTransformationListener( this );
				}
				wasInView.put( m, false );
			}
		}
		for( Entity entity : entities ){
			if ( map.get( entity ) == null ){
				map.put( entity, new LinkedList< Object >() );
			}
			map.get( entity ).add( listener );
		}
	}
}
