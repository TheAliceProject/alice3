package edu.cmu.cs.dennisc.matt;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SModel;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SThing;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionEvent;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.implementation.AbstractTransformableImp;
import org.lgna.story.implementation.CameraImp;

import edu.cmu.cs.dennisc.java.util.concurrent.Collections;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

public class OcclusionHandler extends TransformationChangedHandler<Object, OcclusionEvent> {

	private OcclusionEventHandler occlusionEventHandler = new OcclusionEventHandler();
	private CameraImp camera;

	public void addOcclusionEvent( Object occlusionEventListener, List<SModel> groupOne, List<SModel> groupTwo ) {
		registerIsFiringMap( occlusionEventListener );
		registerPolicyMap( occlusionEventListener, MultipleEventPolicy.IGNORE );
		List<SModel> allObserving = Collections.newCopyOnWriteArrayList( groupOne );
		allObserving.addAll( groupTwo );
		if( ( groupOne.size() > 0 ) && ( groupOne.get( 0 ) != null ) && ( camera == null ) ) {
			camera = ImplementationAccessor.getImplementation( groupOne.get( 0 ) ).getScene().findFirstCamera();
			camera.getSgComposite().addAbsoluteTransformationListener( this );
		}
		for( SThing m : allObserving ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
			}
		}
		occlusionEventHandler.register( occlusionEventListener, groupOne, groupTwo );
	}

	@Override
	protected void check( SThing changedEntity ) {
		occlusionEventHandler.check( changedEntity );
	}

	@Override
	protected void nameOfFireCall( Object listener, OcclusionEvent event ) {
		if( listener instanceof OcclusionStartListener ) {
			OcclusionStartListener start = (OcclusionStartListener)listener;
			start.occlusionStarted( (StartOcclusionEvent)event );
		} else if( listener instanceof OcclusionEndListener ) {
			OcclusionEndListener start = (OcclusionEndListener)listener;
			start.occlusionEnded( (EndOcclusionEvent)event );
		}
	}

	private class OcclusionEventHandler {

		private Map<SModel, CopyOnWriteArrayList<SModel>> checkMap = new ConcurrentHashMap<SModel, CopyOnWriteArrayList<SModel>>();
		private Map<SModel, Map<SModel, CopyOnWriteArrayList<Object>>> eventMap = new ConcurrentHashMap<SModel, Map<SModel, CopyOnWriteArrayList<Object>>>();
		private Map<SModel, Map<SModel, Boolean>> wereOccluded = new ConcurrentHashMap<SModel, Map<SModel, Boolean>>();

		public void check( SThing changedEntity ) {
			if( camera == null ) {
				camera = ImplementationAccessor.getImplementation( changedEntity ).getScene().findFirstCamera();
				if( camera == null ) {
					return;
				}
			}
			if( changedEntity.equals( camera.getAbstraction() ) ) {
				for( SModel t : checkMap.keySet() ) {
					for( SModel m : checkMap.get( t ) ) {
						for( Object occList : eventMap.get( t ).get( m ) ) {
							if( check( occList, t, m ) ) {
								CopyOnWriteArrayList<SMovableTurnable> models = new CopyOnWriteArrayList<SMovableTurnable>();
								if( camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( m ) ) < camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( t ) ) ) {
									models.add( (SMovableTurnable)m );
									models.add( (SMovableTurnable)t );
								} else {
									models.add( (SMovableTurnable)t );
									models.add( (SMovableTurnable)m );
								}
								if( occList instanceof OcclusionStartListener ) {
									fireEvent( occList, new StartOcclusionEvent( models.get( 0 ), models.get( models.size() - 1 ) ) );
								} else if( occList instanceof OcclusionEndListener ) {
									fireEvent( occList, new EndOcclusionEvent( models.get( 0 ), models.get( models.size() - 1 ) ) );
								}
							}
						}
						boolean doTheseOcclude = AabbOcclusionDetector.doesTheseOcclude( camera, changedEntity, m );
						wereOccluded.get( t ).put( m, doTheseOcclude );
						wereOccluded.get( m ).put( t, doTheseOcclude );
					}
				}
			} else {
				for( SModel m : checkMap.get( changedEntity ) ) {
					for( Object occList : eventMap.get( changedEntity ).get( m ) ) {
						if( check( occList, (SModel)changedEntity, m ) ) {
							CopyOnWriteArrayList<SMovableTurnable> models = new CopyOnWriteArrayList<SMovableTurnable>();
							if( camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( m ) ) < camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( changedEntity ) ) ) {
								models.add( (SMovableTurnable)m );
								models.add( (SMovableTurnable)changedEntity );
							} else {
								models.add( (SMovableTurnable)changedEntity );
								models.add( (SMovableTurnable)m );
							}
							if( occList instanceof OcclusionStartListener ) {
								fireEvent( occList, new StartOcclusionEvent( models.get( 0 ), models.get( models.size() - 1 ) ) );
							} else if( occList instanceof OcclusionEndListener ) {
								fireEvent( occList, new EndOcclusionEvent( models.get( 0 ), models.get( models.size() - 1 ) ) );
							}
						}
					}
					boolean doTheseOcclude = AabbOcclusionDetector.doesTheseOcclude( camera, changedEntity, m );
					wereOccluded.get( changedEntity ).put( m, doTheseOcclude );
					wereOccluded.get( m ).put( (SModel)changedEntity, doTheseOcclude );
				}
			}
		}

		private boolean check( Object occList, SModel changedEntity, SModel m ) {
			if( occList instanceof OcclusionStartListener ) {
				return !wereOccluded.get( m ).get( changedEntity ) && AabbOcclusionDetector.doesTheseOcclude( camera, m, changedEntity );
			} else if( occList instanceof OcclusionEndListener ) {
				return wereOccluded.get( m ).get( changedEntity ) && !AabbOcclusionDetector.doesTheseOcclude( camera, m, changedEntity );
			}
			Logger.errln( "UNHANDLED OcclusionListener TYPE", occList.getClass() );
			return false;
		}

		public void register( Object occlusionListener, List<SModel> groupOne, List<SModel> groupTwo ) {
			for( SModel m : groupOne ) {
				if( eventMap.get( m ) == null ) {
					eventMap.put( m, new ConcurrentHashMap<SModel, CopyOnWriteArrayList<Object>>() );
					wereOccluded.put( m, new ConcurrentHashMap<SModel, Boolean>() );
					checkMap.put( m, new CopyOnWriteArrayList<SModel>() );
				}
				for( SModel t : groupTwo ) {
					if( eventMap.get( m ).get( t ) == null ) {
						eventMap.get( m ).put( t, new CopyOnWriteArrayList<Object>() );
					}
					if( !m.equals( t ) ) {
						eventMap.get( m ).get( t ).add( occlusionListener );
						wereOccluded.get( m ).put( t, false );
						if( !checkMap.get( m ).contains( t ) ) {
							checkMap.get( m ).add( t );
						}
					}
				}
			}
			for( SModel m : groupTwo ) {
				if( eventMap.get( m ) == null ) {
					eventMap.put( m, new ConcurrentHashMap<SModel, CopyOnWriteArrayList<Object>>() );
					wereOccluded.put( m, new ConcurrentHashMap<SModel, Boolean>() );
					checkMap.put( m, new CopyOnWriteArrayList<SModel>() );
				}
				for( SModel t : groupOne ) {
					if( eventMap.get( m ).get( t ) == null ) {
						eventMap.get( m ).put( t, new CopyOnWriteArrayList<Object>() );
					}
					if( !m.equals( t ) ) {
						eventMap.get( m ).get( t ).add( occlusionListener );
						wereOccluded.get( m ).put( t, false );
						if( !checkMap.get( m ).contains( t ) ) {
							checkMap.get( m ).add( t );
						}
					}
				}
			}
		}
	}

}
