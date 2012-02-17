package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.EndCollisionListener;
import org.lgna.story.event.EndOcclusionListener;
import org.lgna.story.event.OcclusionEvent;
import org.lgna.story.event.StartCollisionListener;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.event.StartOcclusionListener;
import org.lgna.story.implementation.AbstractTransformableImp;
import org.lgna.story.implementation.CameraImp;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

public class OcclusionHandler extends TransformationChangedHandler < OcclusionListener, OcclusionEvent > {
	

	private OcclusionEventHandler occlusionEventHandler = new OcclusionEventHandler();
	private CameraImp camera;
	
	public void addOcclusionEvent( OcclusionListener occlusionEventListener, 
			List< Entity > groupOne, List< Entity > groupTwo ) {
		registerIsFiringMap(occlusionEventListener);
		registerPolicyMap(occlusionEventListener, MultipleEventPolicy.IGNORE);
		List< Entity > allObserving = Collections.newArrayList( groupOne );
		allObserving.addAll( groupTwo );
		for( Entity m : allObserving ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
			}
		}
		occlusionEventHandler.register( occlusionEventListener, groupOne, groupTwo );
	}
	
	@Override
	protected void check(Entity changedEntity) {
		occlusionEventHandler.check(changedEntity);
	}

	@Override
	protected void nameOfFireCall(OcclusionListener listener, OcclusionEvent event) {
		if (listener instanceof StartOcclusionListener) {
			StartOcclusionListener start = (StartOcclusionListener) listener;
			start.whenTheseOcclude( (StartOcclusionEvent) event );
		} else if (listener instanceof EndOcclusionListener) {
			EndOcclusionListener start = (EndOcclusionListener) listener;
			start.theseNoLongerOcclude( (EndOcclusionEvent) event );
		}
	}
	
	private class OcclusionEventHandler {

		private HashMap< Entity, LinkedList< Entity >> checkMap = new HashMap< Entity, LinkedList< Entity >>();
		private HashMap< Entity, HashMap< Entity, LinkedList< OcclusionListener >>> eventMap = new HashMap< Entity, HashMap< Entity, LinkedList< OcclusionListener >>>();
		private HashMap< Entity, HashMap< Entity, Boolean>> wereOccluded = new HashMap< Entity, HashMap< Entity, Boolean>>();

		public void check( Entity changedEntity ) {
			if( camera == null ){
				camera = ImplementationAccessor.getImplementation(changedEntity).getScene().findFirstCamera();
				if( camera == null ) {
					return;
				}
			}
			for( Entity m : checkMap.get( changedEntity ) ) {
				for( OcclusionListener occList : eventMap.get( changedEntity ).get( m )){
					if( check( occList, changedEntity, m ) ) {
						LinkedList< Entity > models = new LinkedList< Entity >();
						if ( camera.getDistanceTo( (AbstractTransformableImp) ImplementationAccessor.getImplementation( m ) ) <
							camera.getDistanceTo( (AbstractTransformableImp) ImplementationAccessor.getImplementation( changedEntity ) ) ){
							models.add( m );
							models.add( changedEntity );
						} else {
							models.add( changedEntity );
							models.add( m );
						}
						if ( occList instanceof StartOcclusionListener ) {
							fireEvent( occList, new StartOcclusionEvent( models ) );
						} else if ( occList instanceof EndOcclusionListener ) {
							fireEvent( occList, new EndOcclusionEvent( models ) );
						}
					}
				}
				boolean doTheseOcclude = AabbOcclusionDetector.doesTheseOcclude( camera, changedEntity, m );
				wereOccluded.get( changedEntity ).put( m, doTheseOcclude );
				wereOccluded.get( m ).put( changedEntity, doTheseOcclude );
//				if( AabbOcclusionDetector.doesTheseOcclude( camera, m, changedEntity ) ) {
//					LinkedList< Entity > models = new LinkedList< Entity >();
//					if ( camera.getDistanceTo( (AbstractTransformableImp) ImplementationAccessor.getImplementation( m ) ) <
//							camera.getDistanceTo( (AbstractTransformableImp) ImplementationAccessor.getImplementation( changedEntity ) ) ){
//						models.add( m );
//						models.add( changedEntity );
//					} else {
//						models.add( changedEntity );
//						models.add( m );
//					}
//					for( OcclusionListener colList : eventMap.get( changedEntity ).get( m ) ) {
//						fireEvent( colList, new OcclusionEvent( models ) );
//					}
//				}
			}
		}

		private boolean check(OcclusionListener occList, Entity changedEntity, Entity m) {
			if ( occList instanceof StartOcclusionListener ) {
				return !wereOccluded.get( m ).get( changedEntity ) && AabbOcclusionDetector.doesTheseOcclude(camera, m, changedEntity );
			}else if ( occList instanceof EndOcclusionListener ) {
				return wereOccluded.get( m ).get( changedEntity ) && !AabbOcclusionDetector.doesTheseOcclude(camera, m, changedEntity );
			}
			Logger.errln( "UNHANDLED CollisionListener TYPE " + occList.getClass() );
			return false;
		}

		public void register( OcclusionListener occlusionListener, List< Entity > groupOne,
				List< Entity > groupTwo ) {
			for( Entity m : groupOne ) {
				if( eventMap.get( m ) == null ) {
					eventMap.put( m, new HashMap< Entity, LinkedList< OcclusionListener >>() );
					wereOccluded.put( m, new HashMap< Entity, Boolean>() );
					checkMap.put( m, new LinkedList< Entity >() );
				}
				for( Entity t : groupTwo ) {
					if( eventMap.get( m ).get( t ) == null ) {
						eventMap.get( m ).put( t, new LinkedList< OcclusionListener >() );
					}
					if( !m.equals( t ) ) {
						eventMap.get( m ).get( t ).add( occlusionListener );
						wereOccluded.get( m ).put( t, false );
						if(!checkMap.get( m ).contains( t )){
							checkMap.get( m ).add( t );
						}
					}
				}
			}
			for( Entity m : groupTwo ) {
				if( eventMap.get( m ) == null ) {
					eventMap.put( m, new HashMap< Entity, LinkedList< OcclusionListener >>() );
					wereOccluded.put( m, new HashMap< Entity, Boolean>() );
					checkMap.put( m, new LinkedList< Entity >() );
				}
				for( Entity t : groupOne ) {
					if( eventMap.get( m ).get( t ) == null ) {
						eventMap.get( m ).put( t, new LinkedList< OcclusionListener >() );
					}
					if( !m.equals( t ) ) {
						eventMap.get( m ).get( t ).add( occlusionListener );
						wereOccluded.get( m ).put( t, false );
						if(!checkMap.get( m ).contains( t )){
							checkMap.get( m ).add( t );
						}
					}
				}
			}
		}
	}

}
