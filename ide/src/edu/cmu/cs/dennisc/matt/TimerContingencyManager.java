package edu.cmu.cs.dennisc.matt;

import java.util.ArrayList;

import org.lgna.story.Entity;
import org.lgna.story.EventCollection;
import org.lgna.story.Model;
import org.lgna.story.MovableTurnable;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.Scene;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.EnterProximityEvent;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.LeavesViewEvent;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityExitListener;
import org.lgna.story.event.StartCollisionEvent;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewExitListener;
import org.lgna.story.event.WhileCollisionListener;
import org.lgna.story.event.WhileInViewListener;
import org.lgna.story.event.WhileOcclusionListener;
import org.lgna.story.event.WhileProximityListener;
import org.lgna.story.implementation.SceneImp;

public class TimerContingencyManager {

	private TimerEventHandler timer;
	private Scene scene;

	public TimerContingencyManager( TimerEventHandler timer ) {
		this.timer = timer;
	}

	public <A extends MovableTurnable, B extends MovableTurnable> void register( WhileCollisionListener listener, ArrayList<A> groupOne, Class<A> a, ArrayList<B> groupTwo, Class<B> b, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		EventCollection collectionOne = null;
		EventCollection collectionTwo = null;
		if( groupOne != null ) {
			collectionOne = EventCollection.makeNew( a, groupOne );
		}
		if( groupTwo != null ) {
			collectionTwo = EventCollection.makeNew( b, groupTwo );
		}
		scene.addCollisionStartListener( newStartCollisionAdapter( listener ), a, b, collectionOne, collectionTwo );
		scene.addCollisionEndListener( newEndCollisionAdapter( listener ), a, b, collectionOne, collectionTwo );
	}
	public <A extends MovableTurnable, B extends MovableTurnable> void register( WhileProximityListener listener, ArrayList<A> groupOne, Class<A> a, ArrayList<B> groupTwo, Class<B> b, Double dist, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		EventCollection collectionOne = null;
		EventCollection collectionTwo = null;
		if( groupOne != null ) {
			collectionOne = EventCollection.makeNew( a, groupOne );
		}
		if( groupTwo != null ) {
			collectionTwo = EventCollection.makeNew( b, groupTwo );
		}
		scene.addProximityEnterListener( newEnterProximityAdapter( listener ), a, b, dist, collectionOne, collectionTwo );
		scene.addProximityExitListener( newExitProximityAdapter( listener ), a, b, dist, collectionOne, collectionTwo );
	}
	public <A extends Model, B extends Model> void register( WhileOcclusionListener listener, ArrayList<A> groupOne, Class<A> a, ArrayList<B> groupTwo, Class<B> b, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		EventCollection collectionOne = null;
		EventCollection collectionTwo = null;
		if( groupOne != null ) {
			collectionOne = EventCollection.makeNew( a, groupOne );
		}
		if( groupTwo != null ) {
			collectionTwo = EventCollection.makeNew( b, groupTwo );
		}
		scene.addOcclusionStartListener( newEnterOcclusionAdapter( listener ), a, b, collectionOne, collectionTwo );
		scene.addOcclusionEndListener( newExitOcclusionAdapter( listener ), a, b, collectionOne, collectionTwo );
	}

	public <A extends Model> void register( WhileInViewListener listener, Class<A> a, ArrayList group, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		EventCollection collection = null;
		if( group != null ) {
			collection = EventCollection.makeNew( a, group );
		}
		scene.addViewEnterListener( newEnterViewAdapter( listener ), a, collection );
		scene.addViewExitListener( newExitViewAdapter( listener ), a, collection );
	}

	private ViewExitListener newExitViewAdapter( final WhileInViewListener listener ) {
		return new ViewExitListener() {
			public void leftView( LeavesViewEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

	private ViewEnterListener newEnterViewAdapter( final WhileInViewListener listener ) {
		return new ViewEnterListener() {
			public void viewEntered( ComesIntoViewEvent e ) {
				timer.activate( listener );
			}
		};
	}

	private OcclusionStartListener newEnterOcclusionAdapter( final WhileOcclusionListener listener ) {
		return new OcclusionStartListener() {
			public void occlusionStarted( StartOcclusionEvent e ) {
				timer.activate( listener );
			}
		};
	}

	private OcclusionEndListener newExitOcclusionAdapter( final WhileOcclusionListener listener ) {
		return new OcclusionEndListener() {
			public void occlusionEnded( EndOcclusionEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

	private ProximityEnterListener newEnterProximityAdapter( final WhileProximityListener listener ) {
		return new ProximityEnterListener() {
			public void proximityEntered( EnterProximityEvent e ) {
				timer.activate( listener );
			}
		};
	}

	private ProximityExitListener newExitProximityAdapter( final WhileProximityListener listener ) {
		return new ProximityExitListener() {
			public void proximityExited( ExitProximityEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

	@SuppressWarnings("rawtypes")
	private CollisionEndListener newEndCollisionAdapter( final WhileCollisionListener listener ) {
		return new CollisionEndListener() {
			public void collisionEnded( EndCollisionEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

	@SuppressWarnings("rawtypes")
	private CollisionStartListener newStartCollisionAdapter( final WhileCollisionListener listener ) {
		return new CollisionStartListener() {
			public void collisionStarted( StartCollisionEvent e ) {
				timer.activate( listener );
			}
		};
	}

	private Entity[] toArray( ArrayList<? extends Entity> arr ) {
		Entity[] rv = new Entity[ arr.size() ];
		for( int i = 0; i != arr.size(); ++i ) {
			rv[ i ] = arr.get( i );
		}
		return rv;
	}

	public void setScene( SceneImp scene ) {
		this.scene = scene.getAbstraction();
	}
}
