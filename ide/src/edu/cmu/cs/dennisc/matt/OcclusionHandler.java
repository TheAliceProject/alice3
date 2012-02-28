package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.Model;
import org.lgna.story.MovableTurnable;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionEvent;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.implementation.AbstractTransformableImp;
import org.lgna.story.implementation.CameraImp;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

public class OcclusionHandler extends TransformationChangedHandler<Object,OcclusionEvent> {

	private OcclusionEventHandler occlusionEventHandler = new OcclusionEventHandler();
	private CameraImp camera;

	public void addOcclusionEvent( Object occlusionEventListener, List<Model> groupOne, List<Model> groupTwo ) {
		registerIsFiringMap( occlusionEventListener );
		registerPolicyMap( occlusionEventListener, MultipleEventPolicy.IGNORE );
		List<Model> allObserving = Collections.newArrayList( groupOne );
		allObserving.addAll( groupTwo );
		if( groupOne.get( 0 ) != null && camera == null ) {
			camera = ImplementationAccessor.getImplementation( groupOne.get( 0 ) ).getScene().findFirstCamera();
			camera.getSgComposite().addAbsoluteTransformationListener( this );
		}
		for( Entity m : allObserving ) {
			if( !modelList.contains( m ) ) {
				modelList.add( m );
				ImplementationAccessor.getImplementation( m ).getSgComposite().addAbsoluteTransformationListener( this );
			}
		}
		occlusionEventHandler.register( occlusionEventListener, groupOne, groupTwo );
	}

	@Override
	protected void check( Entity changedEntity ) {
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

		private HashMap<Model,LinkedList<Model>> checkMap = new HashMap<Model,LinkedList<Model>>();
		private HashMap<Model,HashMap<Model,LinkedList<Object>>> eventMap = new HashMap<Model,HashMap<Model,LinkedList<Object>>>();
		private HashMap<Model,HashMap<Model,Boolean>> wereOccluded = new HashMap<Model,HashMap<Model,Boolean>>();

		public void check( Entity changedEntity ) {
			if( camera == null ) {
				camera = ImplementationAccessor.getImplementation( changedEntity ).getScene().findFirstCamera();
				if( camera == null ) {
					return;
				}
			}
			if( changedEntity.equals( camera.getAbstraction() ) ) {
				for( Model t : checkMap.keySet() ) {
					for( Model m : checkMap.get( t ) ) {
						for( Object occList : eventMap.get( t ).get( m ) ) {
							if( check( occList, t, m ) ) {
								LinkedList<MovableTurnable> models = new LinkedList<MovableTurnable>();
								if( camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( m ) ) < camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( t ) ) ) {
									models.add( (MovableTurnable)m );
									models.add( (MovableTurnable)t );
								} else {
									models.add( (MovableTurnable)t );
									models.add( (MovableTurnable)m );
								}
								if( occList instanceof OcclusionStartListener ) {
									fireEvent( occList, new StartOcclusionEvent( models.getFirst(), models.getLast() ) );
								} else if( occList instanceof OcclusionEndListener ) {
									fireEvent( occList, new EndOcclusionEvent( models.getFirst(), models.getLast() ) );
								}
							}
						}
						boolean doTheseOcclude = AabbOcclusionDetector.doesTheseOcclude( camera, changedEntity, m );
						wereOccluded.get( t ).put( m, doTheseOcclude );
						wereOccluded.get( m ).put( t, doTheseOcclude );
					}
				}
			} else {
				for( Model m : checkMap.get( changedEntity ) ) {
					for( Object occList : eventMap.get( changedEntity ).get( m ) ) {
						if( check( occList, (Model)changedEntity, m ) ) {
							LinkedList<MovableTurnable> models = new LinkedList<MovableTurnable>();
							if( camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( m ) ) < camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( changedEntity ) ) ) {
								models.add( (MovableTurnable)m );
								models.add( (MovableTurnable)changedEntity );
							} else {
								models.add( (MovableTurnable)changedEntity );
								models.add( (MovableTurnable)m );
							}
							if( occList instanceof OcclusionStartListener ) {
								fireEvent( occList, new StartOcclusionEvent( models.getFirst(), models.getLast() ) );
							} else if( occList instanceof OcclusionEndListener ) {
								fireEvent( occList, new EndOcclusionEvent( models.getFirst(), models.getLast() ) );
							}
						}
					}
					boolean doTheseOcclude = AabbOcclusionDetector.doesTheseOcclude( camera, changedEntity, m );
					wereOccluded.get( changedEntity ).put( m, doTheseOcclude );
					wereOccluded.get( m ).put( (Model)changedEntity, doTheseOcclude );
				}
			}
		}

		private boolean check( Object occList, Model changedEntity, Model m ) {
			if( occList instanceof OcclusionStartListener ) {
				return !wereOccluded.get( m ).get( changedEntity ) && AabbOcclusionDetector.doesTheseOcclude( camera, m, changedEntity );
			} else if( occList instanceof OcclusionEndListener ) {
				return wereOccluded.get( m ).get( changedEntity ) && !AabbOcclusionDetector.doesTheseOcclude( camera, m, changedEntity );
			}
			Logger.errln( "UNHANDLED CollisionListener TYPE " + occList.getClass() );
			return false;
		}

		public void register( Object occlusionListener, List<Model> groupOne, List<Model> groupTwo ) {
			for( Model m : groupOne ) {
				if( eventMap.get( m ) == null ) {
					eventMap.put( m, new HashMap<Model,LinkedList<Object>>() );
					wereOccluded.put( m, new HashMap<Model,Boolean>() );
					checkMap.put( m, new LinkedList<Model>() );
				}
				for( Model t : groupTwo ) {
					if( eventMap.get( m ).get( t ) == null ) {
						eventMap.get( m ).put( t, new LinkedList<Object>() );
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
			for( Model m : groupTwo ) {
				if( eventMap.get( m ) == null ) {
					eventMap.put( m, new HashMap<Model,LinkedList<Object>>() );
					wereOccluded.put( m, new HashMap<Model,Boolean>() );
					checkMap.put( m, new LinkedList<Model>() );
				}
				for( Model t : groupOne ) {
					if( eventMap.get( m ).get( t ) == null ) {
						eventMap.get( m ).put( t, new LinkedList<Object>() );
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
