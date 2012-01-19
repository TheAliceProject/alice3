package edu.cmu.cs.dennisc.matt;

import java.util.List;
import java.util.Map;

import org.lgna.story.event.TimerEvent;
import org.lgna.story.event.TimerEventListener;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassDisplayChangeEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassInitializeEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassResizeEvent;

public class TimerEventHandler extends AbstractEventHandler<TimerEventListener, TimerEvent> implements LookingGlassListener{

	private Map<TimerEventListener, Long> freqMap = Collections.newHashMap();
	private List<TimerEventListener> timerList = Collections.newArrayList();
	private List<TimerEventListener> fireList = Collections.newArrayList();
	private Long currentTime;
	private Map<TimerEventListener, Long> mostRecentFire = Collections.newHashMap();


	public void addListener(TimerEventListener timerEventListener, Long frequency) {
		freqMap.put(timerEventListener, secondsToMills(frequency));
		mostRecentFire.put(timerEventListener, Double.doubleToLongBits(0));
		if(frequency > 0){
			timerList.add(timerEventListener);
		}
		if(frequency == 0){
			fireList.add(timerEventListener);
		}
	}

	private void update() {
		for(TimerEventListener listener: timerList){
			if(timeToFire(listener))
				fireList.add(listener);
		}
	}

	private boolean timeToFire(TimerEventListener listener) {
		return currentTime - mostRecentFire.get(listener) > freqMap.get(listener);
	}

	private Long secondsToMills(Long frequency) {
		return frequency*1000;
	}

	private void fireAllTargeted(){
		for(TimerEventListener listener: fireList){
			fire(listener, new TimerEvent());
			if(freqMap.get(listener) != 0){
				fireList.remove(listener);
				mostRecentFire .put(listener, currentTime);
			}
		}
	}

	@Override
	protected void fire(final TimerEventListener listener, final TimerEvent event) {
		Thread thread = new Thread(){
			@Override
			public void run(){
				listener.timeElapsed(event);
				if(freqMap.get(listener) == -1){
					fireList.add(listener);
				}
			}
		};
		thread.start();
	}

	public void initialized(LookingGlassInitializeEvent e) {}

	public void cleared(LookingGlassRenderEvent e) {}

	public void rendered(LookingGlassRenderEvent e) {}

	public void resized(LookingGlassResizeEvent e) {}

	public void displayChanged(LookingGlassDisplayChangeEvent e) {
		currentTime = System.currentTimeMillis();
		update();
		fireAllTargeted();
	}
}
