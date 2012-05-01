package edu.cmu.cs.dennisc.matt;

import java.util.List;

import org.lgna.story.event.SceneActivationEvent;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.java.util.Collections;

public class EventRecorder {

	private Double startTime = new Double( -1 );
	private static List<EventRecorder> instanceList = Collections.newLinkedList();
	private SceneImp scene;
	private EventTranscript transcript = new EventTranscript();

	private EventRecorder( SceneImp scene ) {
		this.scene = scene;
	}

	public void recordEvent( EventRecord e ) {
		synchronized( startTime ) {
			if( e.getEvent() instanceof SceneActivationEvent ) {
				if( startTime == -1 ) {
					startTime = scene.getProgram().getAnimator().getCurrentTime();
					transcript.register( e );
				}
			} else if( startTime != -1 ) {
				transcript.register( e );
			} else {
				System.out.println( "WARNING EVENTS NOT BEING PROPERLY RECORDED " + e.getClass() );
				Thread.dumpStack();
			}
		}
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

	public EventTranscript getEventTranscript() {
		return transcript;
	}
}
