package edu.cmu.cs.dennisc.matt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lgna.story.ImplementationAccessor;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SModel;
import org.lgna.story.SThing;
import org.lgna.story.event.EndOcclusionEvent;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionEvent;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.implementation.AbstractTransformableImp;
import org.lgna.story.implementation.CameraImp;

import edu.cmu.cs.dennisc.java.util.logging.Logger;

public class OcclusionHandler extends TransformationChangedHandler<Object, OcclusionEvent> {

	private OcclusionEventHandler occlusionEventHandler = new OcclusionEventHandler();
	private CameraImp camera;

	public <A extends SModel, B extends SModel> void addOcclusionEvent( Object occlusionEventListener, ArrayList<A> groupOne, Class<A> a, ArrayList<B> groupTwo, Class<B> b, MultipleEventPolicy policy ) {
		ArrayList[] listArr = super.addPairedListener( occlusionEventListener, groupOne, a, groupTwo, b, policy );
		occlusionEventHandler.register( occlusionEventListener, listArr[ 0 ], listArr[ 1 ] );
	}

	@Override
	protected void ammend( Object key, int i, SThing newObject ) {
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
			OcclusionEndListener end = (OcclusionEndListener)listener;
			end.occlusionEnded( (EndOcclusionEvent)event );
		}
	}

	private class OcclusionEventHandler {

		private HashMap<SModel, CopyOnWriteArrayList<SModel>> checkMap = new HashMap<SModel, CopyOnWriteArrayList<SModel>>();
		private HashMap<SModel, HashMap<SModel, CopyOnWriteArrayList<Object>>> eventMap = new HashMap<SModel, HashMap<SModel, CopyOnWriteArrayList<Object>>>();
		private HashMap<SModel, HashMap<SModel, Boolean>> wereOccluded = new HashMap<SModel, HashMap<SModel, Boolean>>();

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
								LinkedList<SModel> models = new LinkedList<SModel>();
								if( camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( m ) ) < camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( t ) ) ) {
									models.add( (SModel)m );
									models.add( (SModel)t );
								} else {
									models.add( (SModel)t );
									models.add( (SModel)m );
								}
								if( occList instanceof OcclusionStartListener ) {
									StartOcclusionEvent event = EventBuilder.buildCollisionEvent( StartOcclusionEvent.class, occList, models.toArray( new SModel[ 0 ] ) );
									event.setForeground( models.getFirst() );
									fireEvent( occList, event, event );
								} else if( occList instanceof OcclusionEndListener ) {
									EndOcclusionEvent event = EventBuilder.buildCollisionEvent( EndOcclusionEvent.class, occList, models.toArray( new SModel[ 0 ] ) );
									event.setForeground( models.getFirst() );
									fireEvent( occList, event, event );
								}
							}
						}
						boolean doTheseOcclude = AabbOcclusionDetector.doesTheseOcclude( camera, m, changedEntity );
						wereOccluded.get( t ).put( m, doTheseOcclude );
						wereOccluded.get( m ).put( t, doTheseOcclude );
					}
				}
			} else {
				for( SModel m : checkMap.get( changedEntity ) ) {
					for( Object occList : eventMap.get( changedEntity ).get( m ) ) {
						if( check( occList, (SModel)changedEntity, m ) ) {
							LinkedList<SModel> models = new LinkedList<SModel>();
							if( camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( m ) ) < camera.getDistanceTo( (AbstractTransformableImp)ImplementationAccessor.getImplementation( changedEntity ) ) ) {
								models.add( (SModel)m );
								models.add( (SModel)changedEntity );
							} else {
								models.add( (SModel)changedEntity );
								models.add( (SModel)m );
							}
							if( occList instanceof OcclusionStartListener ) {
								StartOcclusionEvent event = EventBuilder.buildOcclusionEvent( StartOcclusionEvent.class, (OcclusionStartListener)occList, models.toArray( new SModel[ 0 ] ) );
								event.setForeground( models.getFirst() );
								fireEvent( occList, event, event );
							} else if( occList instanceof OcclusionEndListener ) {
								EndOcclusionEvent event = EventBuilder.buildOcclusionEvent( EndOcclusionEvent.class, (OcclusionEndListener)occList, models.toArray( new SModel[ 0 ] ) );
								event.setForeground( models.getFirst() );
								fireEvent( occList, event, event );
							}
						}
					}
					boolean doTheseOcclude = AabbOcclusionDetector.doesTheseOcclude( camera, m, changedEntity );
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
					eventMap.put( m, new HashMap<SModel, CopyOnWriteArrayList<Object>>() );
					wereOccluded.put( m, new HashMap<SModel, Boolean>() );
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
					eventMap.put( m, new HashMap<SModel, CopyOnWriteArrayList<Object>>() );
					wereOccluded.put( m, new HashMap<SModel, Boolean>() );
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
			System.out.println( eventMap );
		}
	}
}
