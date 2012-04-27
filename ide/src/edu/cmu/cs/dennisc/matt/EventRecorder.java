package edu.cmu.cs.dennisc.matt;

import java.util.List;
import java.util.Map;

import org.lgna.story.event.AbstractEvent;
import org.lgna.story.event.SceneActivationEvent;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.java.util.Collections;

public class EventRecorder {

	private Double startTime = new Double( -1 );
	private static List<EventRecorder> instanceList = Collections.newLinkedList();
	private Map<Double,AbstractEvent> eventMap = Collections.newHashMap();
	private List<EventWithTime> remainingEvents = Collections.newArrayList();
	private SceneImp scene;

	private EventRecorder( SceneImp scene ) {
		this.scene = scene;
	}
	
	private class EventWithTime implements Comparable<EventWithTime> {
		AbstractEvent event;
		Double timeOfFire;
		public EventWithTime( double timeOfFire, AbstractEvent e ){
			this.event = e;
			this.timeOfFire = timeOfFire;
		}
		public int compareTo( EventWithTime o ) {
			return this.getTimeOfFire().compareTo( o.getTimeOfFire() );
		}
		
		public Double getTimeOfFire() {
			return this.timeOfFire;
		}
		
		public AbstractEvent getEvent() {
			return this.event;
		}
	}

	public void recordEvent( AbstractEvent e ) {
		synchronized( startTime ) {
			if( e instanceof SceneActivationEvent ) {
				if( startTime == -1 ) {
					startTime = scene.getProgram().getAnimator().getCurrentTime();
					register(startTime, e);
				}
			} else if( startTime != -1 ) {
				register( scene.getProgram().getAnimator().getCurrentTime(), e );
			} else {
				System.out.println( "WARNING EVENTS NOT BEING PROPERLY RECORDED " + e.getClass() );
				Thread.dumpStack();
			}
		}
	}

	private void register( Double currentTime, AbstractEvent e ) {
		eventMap.put( currentTime, e );
		remainingEvents.add( new EventWithTime( currentTime, e ) );
	}

	public List<AbstractEvent> eventToFire( Long time ) {
		List<AbstractEvent> rv = Collections.newLinkedList();
		while( remainingEvents.get( 0 ).getTimeOfFire() < time ) {
			rv.add( eventMap.get( remainingEvents.remove( 0 ).getEvent() ) );
		}
		return rv;
	}

	public static EventRecorder findRecorderForScene( SceneImp sceneImp ) {
		for(EventRecorder recorder : instanceList ) {
			if(recorder.scene.equals( sceneImp )){
				return recorder;
			}
		}
		EventRecorder rv = new EventRecorder( sceneImp );
		instanceList.add( rv );
		return rv;
	}
}
