package edu.cmu.cs.dennisc.matt;

import java.util.ArrayList;

import org.lgna.story.AddEnterViewListener;
import org.lgna.story.AddTimerEventListener;
import org.lgna.story.Entity;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.Scene;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.EndCollisionListener;
import org.lgna.story.event.EndOcclusionListener;
import org.lgna.story.event.EnterProximityEvent;
import org.lgna.story.event.EnterProximityListener;
import org.lgna.story.event.EnterViewListener;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.ExitProximityListener;
import org.lgna.story.event.ExitViewListener;
import org.lgna.story.event.LeavesViewEvent;
import org.lgna.story.event.StartCollisionEvent;
import org.lgna.story.event.StartCollisionListener;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.event.StartOcclusionListener;
import org.lgna.story.event.WhileCollisionListener;
import org.lgna.story.event.WhileInViewListener;
import org.lgna.story.event.WhileOcclusionListener;
import org.lgna.story.event.WhileProximityListener;
import org.lgna.story.implementation.SceneImp;

public class TimerContingencyManager {

	private TimerEventHandler timer;
	private Scene scene;

	public TimerContingencyManager(TimerEventHandler timer) {
		this.timer = timer;
	}

	public void register( WhileCollisionListener listener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener(listener, frequency, policy);
		timer.deactivate( listener );
		scene.addStartCollisionListener( newStartCollisionAdapter( listener ) , toArray( groupOne ), toArray( groupTwo ) );
		scene.addEndCollisionListener( newEndCollisionAdapter( listener ), toArray( groupOne ), toArray( groupTwo ) );
	}
	public void register( WhileProximityListener listener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo, Double dist, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener(listener, frequency, policy);
		timer.deactivate( listener );
		scene.addEnterProximityEventListener( newEnterProximityAdapter( listener ) , toArray( groupOne ), toArray( groupTwo ), dist );
		scene.addExitProximityEventListener( newExitProximityAdapter( listener ), toArray( groupOne ), toArray( groupTwo ), dist );
	}
	public void register( WhileOcclusionListener listener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener(listener, frequency, policy);
		timer.deactivate( listener );
		scene.addStartOcclusionListener( newEnterOcclusionAdapter( listener ) , toArray( groupOne ), toArray( groupTwo ) );
		scene.addEndOcclusionListener( newExitOcclusionAdapter( listener ), toArray( groupOne ), toArray( groupTwo ) );
	}

	public void register(WhileInViewListener listener, ArrayList<Entity> group, Long frequency, MultipleEventPolicy policy) {
		timer.addListener( listener, frequency, policy );
		timer.deactivate( listener );
		scene.addEnterViewListener( newEnterViewAdapter( listener ), toArray( group ) );
		scene.addExitViewListener( newExitViewAdapter( listener ), toArray( group ) );
	}

	private ExitViewListener newExitViewAdapter( final WhileInViewListener listener) {
		return new ExitViewListener() {
			public void leftView(LeavesViewEvent e) {
				timer.deactivate( listener );
			}
		};
	}

	private EnterViewListener newEnterViewAdapter( final WhileInViewListener listener ) {
		return new EnterViewListener() {
			public void cameIntoView(ComesIntoViewEvent e) {
				timer.activate( listener );
			}
		};
	}

	private StartOcclusionListener newEnterOcclusionAdapter( final WhileOcclusionListener listener) {
		return new StartOcclusionListener() {
			public void whenTheseOcclude(StartOcclusionEvent e) {
				timer.activate( listener );
			}
		};
	}

	private EndOcclusionListener newExitOcclusionAdapter( final WhileOcclusionListener listener) {
		return new EndOcclusionListener() {
			public void theseNoLongerOcclude(EndOcclusionEvent e) {
				timer.deactivate( listener );
			}
		};
	}

	private EnterProximityListener newEnterProximityAdapter( final WhileProximityListener listener) {
		return new EnterProximityListener() {
			public void whenTheseGetClose(EnterProximityEvent e) {
				timer.activate( listener );
			}
		};
	}

	private ExitProximityListener newExitProximityAdapter( final WhileProximityListener listener) {
		return new ExitProximityListener() {
			public void whenTheseMoveApart(ExitProximityEvent e) {
				timer.deactivate( listener );
			}
		};
	}

	private EndCollisionListener newEndCollisionAdapter( final WhileCollisionListener listener ) {
		return new EndCollisionListener() {
			public void whenTheseStopColliding(EndCollisionEvent e) {
				timer.deactivate( listener );
			}
		};
	}

	private StartCollisionListener newStartCollisionAdapter( final WhileCollisionListener listener ) {
		return new StartCollisionListener() {
			public void collisionStarted(StartCollisionEvent e) {
				timer.activate( listener );
			}
		};
	}

	private Entity[] toArray(ArrayList<Entity> arr) {
		Entity[] rv = new Entity[arr.size()];
		for( int i = 0; i != arr.size(); ++i ) {
			rv[i] = arr.get(i);
		}
		return rv;
	}

	public void setScene(SceneImp scene) {
		this.scene = scene.getAbstraction();
	}
}
