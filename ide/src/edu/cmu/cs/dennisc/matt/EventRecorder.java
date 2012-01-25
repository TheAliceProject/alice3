package edu.cmu.cs.dennisc.matt;

import java.util.Map;

import org.lgna.story.event.AbstractEvent;

import edu.cmu.cs.dennisc.java.util.Collections;

public class EventRecorder {

	private static EventRecorder instance = new EventRecorder();
	private Map<Object, AbstractEvent> eventMap = Collections.newHashMap();
	
	private EventRecorder(){
		//make private
	}
	
	public static EventRecorder getSingleton(){
		return instance;
	}
	
	public void recordEvent(AbstractEvent e){
		eventMap.put(new Object(), e);
	}
}
