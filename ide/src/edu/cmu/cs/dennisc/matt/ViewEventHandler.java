package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.Model;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.LeavesViewEvent;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewEvent;
import org.lgna.story.event.ViewExitListener;
import org.lgna.story.implementation.CameraImp;

import edu.cmu.cs.dennisc.java.util.Collections;

public class ViewEventHandler extends TransformationChangedHandler<Object,ViewEvent> {

	private CameraImp camera;
	private Map<Model,List<Object>> map = Collections.newHashMap();
	private Map<Model,Boolean> wasInView = Collections.newHashMap();

	@Override
	protected void check( Entity changedEntity ) {
		if( camera == null ) { //should not really be hit
			camera = ImplementationAccessor.getImplementation( changedEntity ).getScene().findFirstCamera();
			if( camera == null ) {
				return;
			} else {
				camera.getSgComposite().addAbsoluteTransformationListener( this );
			}
		}
		if( changedEntity == camera.getAbstraction() ) {
			for( Model model : map.keySet() ) {
				for( Object listener : map.get( model ) ) {
					if( check( listener, model ) ) {
						ViewEvent event = listener instanceof ViewEnterListener ? new ComesIntoViewEvent( model ) : new LeavesViewEvent( model );
						fireEvent( listener, event );
					}
				}
				wasInView.put( model, IsInViewDetector.isThisInView( model, camera ) );
			}
		} else {
			for( Object listener : map.get( changedEntity ) ) {
				if( check( listener, changedEntity ) ) {
					ViewEvent event = listener instanceof ViewEnterListener ? new ComesIntoViewEvent( (Model)changedEntity ) : new LeavesViewEvent( (Model)changedEntity );
					fireEvent( listener, event );
				}
			}
			wasInView.put( (Model)changedEntity, IsInViewDetector.isThisInView( changedEntity, camera ) );
		}
	}

	private boolean check( Object listener, Entity changedEntity ) {
		boolean rv = false;
		boolean thisInView = IsInViewDetector.isThisInView( changedEntity, camera );
		if( wasInView.get( changedEntity ) == null ) {
			wasInView.put( (Model)changedEntity, thisInView );
			return false;
		}
		if( listener instanceof ViewEnterListener ) {
			if( thisInView && !wasInView.get( changedEntity ) ) {
				rv = true;
			}
		} else if( listener instanceof ViewExitListener ) {
			if( !thisInView && wasInView.get( changedEntity ) ) {
				rv = true;
			}
		}
		return rv;
	}

	@Override
	protected void nameOfFireCall( Object listener, ViewEvent event ) {
		if( listener instanceof ViewEnterListener ) {
			ViewEnterListener intoViewEL = (ViewEnterListener)listener;
			intoViewEL.viewEntered( (ComesIntoViewEvent)event );
		} else if( listener instanceof ViewExitListener ) {
			ViewExitListener outOfViewEL = (ViewExitListener)listener;
			outOfViewEL.leftView( (LeavesViewEvent)event );
		}
	}

	public void addViewEventListener( Object listener, Model[] models ) {
		registerIsFiringMap( listener );
		registerPolicyMap( listener, MultipleEventPolicy.IGNORE );
		for( Model m : models ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
				if( camera == null ) {
					camera = ImplementationAccessor.getImplementation( m ).getScene().findFirstCamera();
					camera.getSgComposite().addAbsoluteTransformationListener( this );
				}
			}
		}
		for( Model model : models ) {
			if( map.get( model ) == null ) {
				map.put( model, new LinkedList<Object>() );
			}
			map.get( model ).add( listener );
		}
	}
}
