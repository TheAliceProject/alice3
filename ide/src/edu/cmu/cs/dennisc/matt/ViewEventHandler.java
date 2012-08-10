package edu.cmu.cs.dennisc.matt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lgna.story.SThing;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.SModel;
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
	private Map<SModel,List<Object>> map = Collections.newHashMap();
	private Map<SModel,Boolean> wasInView = Collections.newHashMap();

	@Override
	protected void check( SThing changedThing ) {
		if( camera == null ) { //should not really be hit
			camera = ImplementationAccessor.getImplementation( changedThing ).getScene().findFirstCamera();
			if( camera == null ) {
				return;
			} else {
				camera.getSgComposite().addAbsoluteTransformationListener( this );
			}
		}
		if( changedThing == camera.getAbstraction() ) {
			for( SModel model : map.keySet() ) {
				for( Object listener : map.get( model ) ) {
					if( check( listener, model ) ) {
						ViewEvent event = listener instanceof ViewEnterListener ? new ComesIntoViewEvent( model ) : new LeavesViewEvent( model );
						fireEvent( listener, event );
					}
				}
				wasInView.put( model, IsInViewDetector.isThisInView( model, camera ) );
			}
		} else {
			for( Object listener : map.get( changedThing ) ) {
				if( check( listener, changedThing ) ) {
					ViewEvent event = listener instanceof ViewEnterListener ? new ComesIntoViewEvent( (SModel)changedThing ) : new LeavesViewEvent( (SModel)changedThing );
					fireEvent( listener, event );
				}
			}
			wasInView.put( (SModel)changedThing, IsInViewDetector.isThisInView( changedThing, camera ) );
		}
	}

	@Override
	protected void ammend( Object key, int i, SThing newObject ) {
	}

	private boolean check( Object listener, SThing changedThing ) {
		boolean rv = false;
		boolean thisInView = IsInViewDetector.isThisInView( changedThing, camera );
		if( wasInView.get( changedThing ) == null ) {
			wasInView.put( (SModel)changedThing, thisInView );
			return false;
		}
		if( listener instanceof ViewEnterListener ) {
			if( thisInView && !wasInView.get( changedThing ) ) {
				rv = true;
			}
		} else if( listener instanceof ViewExitListener ) {
			if( !thisInView && wasInView.get( changedThing ) ) {
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

	public <A extends SModel> void addViewEventListener( Object listener, ArrayList<A> models, Class<A> a, MultipleEventPolicy policy ) {
		ArrayList<A> handledModels = super.addSoloListener( listener, models, a, policy );
		registerIsFiringMap( listener );
		registerPolicyMap( listener, MultipleEventPolicy.IGNORE );
		for( SModel m : handledModels ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
				if( camera == null ) {
					camera = ImplementationAccessor.getImplementation( m ).getScene().findFirstCamera();
					camera.getSgComposite().addAbsoluteTransformationListener( this );
				}
			}
		}
		for( SModel model : handledModels ) {
			if( map.get( model ) == null ) {
				map.put( model, new LinkedList<Object>() );
			}
			map.get( model ).add( listener );
		}
	}
}
