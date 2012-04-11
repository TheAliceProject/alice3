package edu.cmu.cs.dennisc.matt;

import java.util.ArrayList;

import org.lgna.story.Entity;
import org.lgna.story.Model;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.Scene;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.EnterProximityEvent;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.ViewExitListener;
import org.lgna.story.event.LeavesViewEvent;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityExitListener;
import org.lgna.story.event.StartCollisionEvent;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.event.ViewEnterListener;
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

	public void register( WhileCollisionListener listener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addCollisionStartListener( newStartCollisionAdapter( listener ), toArray( groupOne ), toArray( groupTwo ) );
		scene.addCollisionEndListener( newEndCollisionAdapter( listener ), toArray( groupOne ), toArray( groupTwo ) );
	}
	public void register( WhileProximityListener listener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo, Double dist, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addProximityEnterListener( newEnterProximityAdapter( listener ), toArray( groupOne ), toArray( groupTwo ), dist );
		scene.addProximityExitListener( newExitProximityAdapter( listener ), toArray( groupOne ), toArray( groupTwo ), dist );
	}
	public void register( WhileOcclusionListener listener, ArrayList<Model> groupOne, ArrayList<Model> groupTwo, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addOcclusionStartListener( newEnterOcclusionAdapter( listener ), (Model[])toArray( groupOne ), (Model[])toArray( groupTwo ) );
		scene.addOcclusionEndListener( newExitOcclusionAdapter( listener ), (Model[])toArray( groupOne ), (Model[])toArray( groupTwo ) );
	}

	public void register( WhileInViewListener listener, ArrayList<Model> group, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addViewEnterListener( newEnterViewAdapter( listener ), (Model[])toArray( group ) );
		scene.addViewExitListener( newExitViewAdapter( listener ), (Model[])toArray( group ) );
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

	private CollisionEndListener newEndCollisionAdapter( final WhileCollisionListener listener ) {
		return new CollisionEndListener() {
			public void collisionEnded( EndCollisionEvent e ) {
				timer.deactivate( listener );
			}
		};
	}

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
