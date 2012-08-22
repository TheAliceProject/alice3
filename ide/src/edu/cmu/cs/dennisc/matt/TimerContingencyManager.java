package edu.cmu.cs.dennisc.matt;

import java.util.ArrayList;
import java.util.HashMap;

import org.lgna.story.AddTimeListener;
import org.lgna.story.EventCollection;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SModel;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SScene;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.EndOcclusionEvent;
import org.lgna.story.event.EnterProximityEvent;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.LeavesViewEvent;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityExitListener;
import org.lgna.story.event.StartCollisionEvent;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.event.TimeEvent;
import org.lgna.story.event.TimeListener;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewExitListener;
import org.lgna.story.event.WhileCollisionEvent;
import org.lgna.story.event.WhileCollisionListener;
import org.lgna.story.event.WhileInViewEvent;
import org.lgna.story.event.WhileInViewListener;
import org.lgna.story.event.WhileOccludingEvent;
import org.lgna.story.event.WhileOcclusionListener;
import org.lgna.story.event.WhileProximityEvent;
import org.lgna.story.event.WhileProximityListener;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.java.util.Collections;

public class TimerContingencyManager {

	private TimerEventHandler timer;
	private SScene scene;

	public TimerContingencyManager( TimerEventHandler timer ) {
		this.timer = timer;
	}

	private class WhileCollisionAdapter<A extends SMovableTurnable, B extends SMovableTurnable> {

		private WhileCollisionListener<A,B> whileListener;
		private AddTimeListener.Detail[] timerDetails = new AddTimeListener.Detail[ 2 ];
		ArrayList<A> groupOne;
		Class<A> clsOne;
		ArrayList<B> groupTwo;
		Class<B> clsTwo;
		HashMap<A,HashMap<B,TimeListener>> map = Collections.newHashMap();

		public WhileCollisionAdapter( WhileCollisionListener<A,B> listener, ArrayList<A> groupOne, Class<A> a, ArrayList<B> groupTwo, Class<B> b, Long frequency, MultipleEventPolicy policy ) {
			this.whileListener = listener;
			this.timerDetails[ 0 ] = AddTimeListener.timerFrequency( frequency );
			this.timerDetails[ 1 ] = AddTimeListener.multipleEventPolicy( policy );
			this.groupOne = groupOne;
			this.groupTwo = groupTwo;
			this.clsOne = a;
			this.clsTwo = b;
		}

		private void init() {
			scene.addCollisionStartListener( collisionListener, clsOne, clsTwo );
		}

		CollisionStartListener<A,B> collisionListener = new CollisionStartListener<A,B>() {

			public void collisionStarted( StartCollisionEvent<A,B> e ) {
				if( alreadyInit( e ) ) {
					timer.activate( map.get( e.getCollidingFromGroupA() ).get( e.getCollidingFromGroupB()) );
				} else {
					startListenerToCollision( e );
				}
			}
			private boolean alreadyInit( StartCollisionEvent<A,B> e ) {
				return map.get( e.getCollidingFromGroupA() ) != null && map.get( e.getCollidingFromGroupA() ).get( e.getCollidingFromGroupB() ) != null;
			}

			private void startListenerToCollision( final StartCollisionEvent<A,B> event ) {
				final TimeListener timeListener = new TimeListener() {

					public void timeElapsed( TimeEvent e ) {
						whileListener.whileColliding( new WhileCollisionEvent<A,B>( clsOne, clsTwo, e.getTimeSinceLastFire(), event.getCollidingFromGroupA(), event.getCollidingFromGroupB() ) );
					}
				};
				CollisionEndListener<A,B> stopListeningListener = new CollisionEndListener<A,B>() {

					public void collisionEnded( EndCollisionEvent<A,B> e ) {
						timer.deactivate( timeListener );

						System.out.println( "stop!!!!!c" );
					}
				};
				if( map.get( event.getCollidingFromGroupA() ) == null ) {
					map.put( event.getCollidingFromGroupA(), new HashMap<B,TimeListener>() );
				}
				map.get( event.getCollidingFromGroupA() ).put( event.getCollidingFromGroupB(), timeListener );
				scene.addTimeListener( timeListener, timerDetails );
				scene.addCollisionEndListener( stopListeningListener, clsOne, clsTwo, new EventCollection( clsOne, event.getCollidingFromGroupA() ), new EventCollection( clsTwo, event.getCollidingFromGroupB() ) );
			}
		};
	}

	private class WhileProximityAdapter<A extends SMovableTurnable, B extends SMovableTurnable> {

		private WhileProximityListener<A,B> whileListener;
		private AddTimeListener.Detail[] timerDetails = new AddTimeListener.Detail[ 2 ];
		ArrayList<A> groupOne;
		Class<A> clsOne;
		ArrayList<B> groupTwo;
		Class<B> clsTwo;
		Double dist;
		HashMap<A,HashMap<B,TimeListener>> map = Collections.newHashMap();

		public WhileProximityAdapter( WhileProximityListener<A,B> listener, ArrayList<A> groupOne, Class<A> a, ArrayList<B> groupTwo, Class<B> b, Double dist, Long frequency, MultipleEventPolicy policy ) {
			this.whileListener = listener;
			this.timerDetails[ 0 ] = AddTimeListener.timerFrequency( frequency );
			this.timerDetails[ 1 ] = AddTimeListener.multipleEventPolicy( policy );
			this.dist = dist;
			this.groupOne = groupOne;
			this.groupTwo = groupTwo;
			this.clsOne = a;
			this.clsTwo = b;
		}

		private void init() {
			scene.addProximityEnterListener( proximityListener, clsOne, clsTwo, dist );
		}

		ProximityEnterListener<A,B> proximityListener = new ProximityEnterListener<A,B>() {

			public void proximityEntered( EnterProximityEvent<A,B> e ) {
				if( alreadyInit( e ) ) {
					timer.activate( map.get( e.getCollidingFromGroupA() ).get( e.getCollidingFromGroupB() ) );
				} else {
					startListenerToProximity( e );
				}
			}
			private boolean alreadyInit( EnterProximityEvent<A,B> e ) {
				return map.get( e.getCollidingFromGroupA() ) != null && map.get( e.getCollidingFromGroupA() ).get( e.getCollidingFromGroupB() ) != null;
			}

			private void startListenerToProximity( final EnterProximityEvent<A,B> event ) {
				final TimeListener timeListener = new TimeListener() {

					public void timeElapsed( TimeEvent e ) {
						whileListener.whileClose( new WhileProximityEvent<A,B>( clsOne, clsTwo, e.getTimeSinceLastFire(), event.getCollidingFromGroupA(), event.getCollidingFromGroupB() ) );
					}
				};
				ProximityExitListener<A,B> stopListeningListener = new ProximityExitListener<A,B>() {

					public void proximityExited( ExitProximityEvent<A,B> e ) {
						System.out.println( "stop!!!!!p" );
						timer.deactivate( timeListener );
					}
				};
				if( map.get( event.getCollidingFromGroupA() ) == null ) {
					map.put( event.getCollidingFromGroupA(), new HashMap<B,TimeListener>() );
				}
				map.get( event.getCollidingFromGroupA() ).put( event.getCollidingFromGroupB(), timeListener );
				scene.addTimeListener( timeListener, timerDetails );
				scene.addProximityExitListener( stopListeningListener, clsOne, clsTwo, dist, new EventCollection( clsOne, event.getCollidingFromGroupA() ), new EventCollection( clsTwo, event.getCollidingFromGroupB() ) );
			}
		};
	}

	private class WhileOccludingAdapter<A extends SModel, B extends SModel> {

		private WhileOcclusionListener<A,B> whileListener;
		private AddTimeListener.Detail[] timerDetails = new AddTimeListener.Detail[ 2 ];
		ArrayList<A> groupOne;
		Class<A> clsOne;
		ArrayList<B> groupTwo;
		Class<B> clsTwo;
		HashMap<A,HashMap<B,TimeListener>> map = Collections.newHashMap();

		public WhileOccludingAdapter( WhileOcclusionListener<A,B> listener, ArrayList<A> groupOne, Class<A> a, ArrayList<B> groupTwo, Class<B> b, Long frequency, MultipleEventPolicy policy ) {
			this.whileListener = listener;
			this.timerDetails[ 0 ] = AddTimeListener.timerFrequency( frequency );
			this.timerDetails[ 1 ] = AddTimeListener.multipleEventPolicy( policy );
			this.groupOne = groupOne;
			this.groupTwo = groupTwo;
			this.clsOne = a;
			this.clsTwo = b;
		}

		private void init() {
			scene.addOcclusionStartListener( occlusionListener, clsOne, clsTwo );
		}

		OcclusionStartListener<A,B> occlusionListener = new OcclusionStartListener<A,B>() {

			public void occlusionStarted( StartOcclusionEvent<A,B> e ) {
				if( alreadyInit( e ) ) {
					timer.activate( map.get( e.getFromA() ).get( e.getFromB() ) );
				} else {
					startListenerToOcclusion( e );
				}
			}

			private boolean alreadyInit( StartOcclusionEvent<A,B> e ) {
				return map.get( e.getFromA() ) != null && map.get( e.getFromA() ).get( e.getFromB() ) != null;
			}

			private void startListenerToOcclusion( final StartOcclusionEvent<A,B> event ) {

				final TimeListener timeListener = new TimeListener() {

					public void timeElapsed( TimeEvent e ) {
						WhileOccludingEvent<A,B> whileOccludingEvent = new WhileOccludingEvent<A,B>( clsOne, clsTwo, e.getTimeSinceLastFire(), event.getFromA(), event.getFromB() );
						whileOccludingEvent.setForeground( event.getForeground() );
						whileListener.whileOccluding( whileOccludingEvent );
					}
				};
				OcclusionEndListener<A,B> stopListeningListener = new OcclusionEndListener<A,B>() {

					public void occlusionEnded( EndOcclusionEvent<A,B> e ) {
						System.out.println( "stop!!!!!o" );
						timer.deactivate( timeListener );
					}
				};
				if( map.get( event.getFromA() ) == null ) {
					map.put( event.getFromA(), new HashMap<B,TimeListener>() );
				}
				map.get( event.getFromA() ).put( event.getFromB(), timeListener );
				scene.addTimeListener( timeListener, timerDetails );
				scene.addOcclusionEndListener( stopListeningListener, clsOne, clsTwo, new EventCollection( clsOne, event.getFromA() ), new EventCollection( clsTwo, event.getFromB() ) );
			}
		};
	}

	private class WhileInViewAdapter<A extends SModel> {

		private WhileInViewListener<A> whileListener;
		private AddTimeListener.Detail[] timerDetails = new AddTimeListener.Detail[ 2 ];
		ArrayList<A> groupOne;
		Class<A> clsOne;
		HashMap<A,TimeListener> map = Collections.newHashMap();

		public WhileInViewAdapter( WhileInViewListener<A> listener, ArrayList<A> groupOne, Class<A> a, Long frequency, MultipleEventPolicy policy ) {
			this.whileListener = listener;
			this.timerDetails[ 0 ] = AddTimeListener.timerFrequency( frequency );
			this.timerDetails[ 1 ] = AddTimeListener.multipleEventPolicy( policy );
			this.groupOne = groupOne;
			this.clsOne = a;
		}

		private void init() {
			scene.addViewEnterListener( occlusionListener, clsOne );
		}

		ViewEnterListener<A> occlusionListener = new ViewEnterListener<A>() {

			public void viewEntered( ComesIntoViewEvent<A> e ) {
				if( alreadyInit( e ) ) {
					timer.activate( map.get( e.getEnteringView() ) );
				} else {
					startListenerToInView( e );
				}
			}
			private boolean alreadyInit( ComesIntoViewEvent<A> e ) {
				return map.get( e.getEnteringView() ) != null;
			}

			private void startListenerToInView( final ComesIntoViewEvent<A> event ) {
				final TimeListener timeListener = new TimeListener() {

					public void timeElapsed( TimeEvent e ) {
						whileListener.whileInView( new WhileInViewEvent<A>( clsOne, e.getTimeSinceLastFire(), event.getEnteringView() ) );
					}
				};
				ViewExitListener<A> stopListeningListener = new ViewExitListener<A>() {

					public void leftView( LeavesViewEvent<A> e ) {
						System.out.println( "stop!!!!!v" );
						timer.deactivate( timeListener );
					}
				};
				map.get( event.getEnteringView() );
				scene.addTimeListener( timeListener, timerDetails );
				scene.addViewExitListener( stopListeningListener, clsOne, new EventCollection( clsOne, event.getEnteringView() ) );
			}
		};
	}

	public <A extends SMovableTurnable, B extends SMovableTurnable> void register( WhileCollisionListener<A,B> listener, ArrayList<A> groupOne, Class<A> a, ArrayList<B> groupTwo, Class<B> b, Long frequency, MultipleEventPolicy policy ) {
		WhileCollisionAdapter<A,B> adapter = new WhileCollisionAdapter<A,B>( listener, groupOne, a, groupTwo, b, frequency, policy );
		adapter.init();
	}
	public <A extends SMovableTurnable, B extends SMovableTurnable> void register( WhileProximityListener<A,B> listener, ArrayList<A> groupOne, Class<A> a, ArrayList<B> groupTwo, Class<B> b, Double dist, Long frequency, MultipleEventPolicy policy ) {
		WhileProximityAdapter<A,B> adapter = new WhileProximityAdapter<A,B>( listener, groupOne, a, groupTwo, b, dist, frequency, policy );
		adapter.init();
		//		timer.addListener( listener, frequency, policy );
		//		timer.deactivate( listener );
		//		EventCollection collectionOne = null;
		//		EventCollection collectionTwo = null;
		//		if( groupOne != null ) {
		//			collectionOne = EventCollection.makeNew( a, groupOne );
		//		}
		//		if( groupTwo != null ) {
		//			collectionTwo = EventCollection.makeNew( b, groupTwo );
		//		}
		//		scene.addProximityEnterListener( newEnterProximityAdapter( listener ), a, b, dist, collectionOne, collectionTwo );
		//		scene.addProximityExitListener( newExitProximityAdapter( listener ), a, b, dist, collectionOne, collectionTwo );
	}
	public <A extends SModel, B extends SModel> void register( WhileOcclusionListener listener, ArrayList<A> groupOne, Class<A> a, ArrayList<B> groupTwo, Class<B> b, Long frequency, MultipleEventPolicy policy ) {
		WhileOccludingAdapter<A,B> adapter = new WhileOccludingAdapter<A,B>( listener, groupOne, a, groupTwo, b, frequency, policy );
		adapter.init();
		//		timer.addListener( listener, frequency, policy );
		//		timer.deactivate( listener );
		//		EventCollection collectionOne = null;
		//		EventCollection collectionTwo = null;
		//		if( groupOne != null ) {
		//			collectionOne = EventCollection.makeNew( a, groupOne );
		//		}
		//		if( groupTwo != null ) {
		//			collectionTwo = EventCollection.makeNew( b, groupTwo );
		//		}
		//		scene.addOcclusionStartListener( newEnterOcclusionAdapter( listener ), a, b, collectionOne, collectionTwo );
		//		scene.addOcclusionEndListener( newExitOcclusionAdapter( listener ), a, b, collectionOne, collectionTwo );
	}

	public <A extends SModel> void register( WhileInViewListener<A> listener, Class<A> a, ArrayList group, Long frequency, MultipleEventPolicy policy ) {
		WhileInViewAdapter<A> adapter = new WhileInViewAdapter<A>( listener, group, a, frequency, policy );
		adapter.init();
		//		timer.addListener( listener, frequency, policy );
		//		timer.deactivate( listener );
		//		EventCollection collection = null;
		//		if( group != null ) {
		//			collection = EventCollection.makeNew( a, group );
		//		}
		//		scene.addViewEnterListener( newEnterViewAdapter( listener ), a, collection );
		//		scene.addViewExitListener( newExitViewAdapter( listener ), a, collection );
	}
	//
	//	private ViewExitListener newExitViewAdapter( final WhileInViewListener listener ) {
	//		return new ViewExitListener() {
	//			public void leftView( LeavesViewEvent e ) {
	//				timer.deactivate( listener );
	//			}
	//		};
	//	}
	//
	//	private ViewEnterListener newEnterViewAdapter( final WhileInViewListener listener ) {
	//		return new ViewEnterListener() {
	//			public void viewEntered( ComesIntoViewEvent e ) {
	//				timer.activate( listener );
	//			}
	//		};
	//	}
	//
	//	private OcclusionStartListener newEnterOcclusionAdapter( final WhileOcclusionListener listener ) {
	//		return new OcclusionStartListener() {
	//			public void occlusionStarted( StartOcclusionEvent e ) {
	//				timer.activate( listener );
	//			}
	//		};
	//	}
	//
	//	private OcclusionEndListener newExitOcclusionAdapter( final WhileOcclusionListener listener ) {
	//		return new OcclusionEndListener() {
	//			public void occlusionEnded( EndOcclusionEvent e ) {
	//				timer.deactivate( listener );
	//			}
	//		};
	//	}
	//
	//	private ProximityEnterListener newEnterProximityAdapter( final WhileProximityListener listener ) {
	//		return new ProximityEnterListener() {
	//			public void proximityEntered( EnterProximityEvent e ) {
	//				timer.activate( listener );
	//			}
	//		};
	//	}
	//
	//	private ProximityExitListener newExitProximityAdapter( final WhileProximityListener listener ) {
	//		return new ProximityExitListener() {
	//			public void proximityExited( ExitProximityEvent e ) {
	//				timer.deactivate( listener );
	//			}
	//		};
	//	}
	//
	//	@SuppressWarnings("rawtypes")
	//	private CollisionEndListener newEndCollisionAdapter( final WhileCollisionListener listener ) {
	//		return new CollisionEndListener() {
	//			public void collisionEnded( EndCollisionEvent e ) {
	//				timer.deactivate( listener );
	//			}
	//		};
	//	}
	//
	//	@SuppressWarnings("rawtypes")
	//	private CollisionStartListener newStartCollisionAdapter( final WhileCollisionListener listener ) {
	//		return new CollisionStartListener() {
	//			public void collisionStarted( StartCollisionEvent e ) {
	//				timer.activate( listener );
	//			}
	//		};
	//	}
	//
	//	private SThing[] toArray( ArrayList<? extends SThing> arr ) {
	//		SThing[] rv = new SThing[ arr.size() ];
	//		for( int i = 0; i != arr.size(); ++i ) {
	//			rv[ i ] = arr.get( i );
	//		}
	//		return rv;
	//	}

	public void setScene( SceneImp scene ) {
		this.scene = scene.getAbstraction();
	}
}
