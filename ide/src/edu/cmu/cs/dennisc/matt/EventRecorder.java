package edu.cmu.cs.dennisc.matt;

import java.util.List;
import java.util.Map;

import org.lgna.story.event.AbstractEvent;
import org.lgna.story.event.SceneActivationEvent;

import edu.cmu.cs.dennisc.java.util.Collections;

public class EventRecorder {

	private Long startTime;
	private static EventRecorder instance = new EventRecorder();
	private Map<Long, AbstractEvent> eventMap = Collections.newHashMap();
	private List<Long> remainingEvents = Collections.newArrayList();

	private EventRecorder() {
		// make private
	}

	public static EventRecorder getSingleton() {
		return instance;
	}

	public void recordEvent(AbstractEvent e) {
		if (e instanceof SceneActivationEvent) {
			startTime = System.currentTimeMillis();
			eventMap.put(startTime - startTime, e);
		} else if (startTime != null) {
			eventMap.put(System.currentTimeMillis() - startTime, e);
		} else {
			System.out.println("WARNING EVENTS NOT BEING PROPERLY RECORDED");
		}
	}

	public AbstractEvent eventToFire(Long time) {
		if (remainingEvents.get(0) < time) {
			return eventMap.get(remainingEvents.remove(0));
		}
		return null;
	}
}
