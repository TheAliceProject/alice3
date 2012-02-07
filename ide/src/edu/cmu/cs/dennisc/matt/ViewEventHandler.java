package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.ComesIntoViewEventListener;
import org.lgna.story.event.ViewEvent;
import org.lgna.story.event.ViewEventListener;
import org.lgna.story.implementation.CameraImp;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.java.util.Collections;

public class ViewEventHandler extends TransformationChangedHandler < ViewEventListener, ViewEvent > {

	private SceneImp sceneImp;
	private CameraImp camera;
	private Map < Entity, List< ViewEventListener > > map = Collections.newHashMap();
	private Map<Entity, Boolean > wasInView = Collections.newHashMap();

	public ViewEventHandler(SceneImp sceneImp) {
		this.sceneImp = sceneImp;
	}

	@Override
	protected void check(Entity changedEntity) {
		if ( camera == null ){
			camera = sceneImp.findFirstCamera();
			if( camera == null ){
				return;
			}
		}
		for( ViewEventListener listener : map.get( changedEntity ) ) {
			if ( check( listener, changedEntity ) ) {
				fireEvent(listener, new ComesIntoViewEvent( changedEntity ) );
			}
		}
	}
	
	private boolean check( ViewEventListener listener, Entity changedEntity ) {
		boolean rv = false;
		if (listener instanceof ComesIntoViewEventListener) {
			boolean thisInView = IsInViewDetector.isThisInView( changedEntity, camera );
			if ( thisInView && !wasInView.get( changedEntity ) ) {
				rv = true;
			}
			wasInView.put( changedEntity, thisInView );
//		} else if (listener instanceof LeavesViewEventListener) {
//			LeavesViewEventListener leaveView = (LeavesViewEventListener) listener;
//			
		}
		return rv;
	}

	@Override
	protected void nameOfFireCall(ViewEventListener listener, ViewEvent event) {
		if (listener instanceof ComesIntoViewEvent) {
			ComesIntoViewEventListener intoViewEL = ( ComesIntoViewEventListener ) listener;
			intoViewEL.cameIntoView( ( ComesIntoViewEvent ) event );
		}
	}

	public void addComesIntoViewEventListener( ViewEventListener listener, Entity[] entities) {
		registerIsFiringMap( listener );
		registerPolicyMap( listener, MultipleEventPolicy.IGNORE );
		for( Entity m : entities ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
			}
		}
		for( Entity entity : entities ){
			if ( map.get( entity ) == null ){
				map.put( entity, new LinkedList<ViewEventListener>() );
			}
			map.get( entity ).add( listener );
		}
	}

}
