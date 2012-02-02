package edu.cmu.cs.dennisc.matt;

import java.util.List;
import java.util.Map;

import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.TimeListener;
import org.lgna.story.event.TimerEvent;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent;
import edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener;
import edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory;

public class TimerEventHandler extends AbstractEventHandler<TimeListener, TimerEvent> {

	private Map<TimeListener, Long> freqMap = Collections.newHashMap();
	private List<TimeListener> timerList = Collections.newArrayList();
	private Long currentTime;
	private Map<TimeListener, Long> mostRecentFire = Collections.newHashMap();

	private final AutomaticDisplayListener automaticDisplayListener = new AutomaticDisplayListener() {
		public void automaticDisplayCompleted(AutomaticDisplayEvent e) {
			currentTime = System.currentTimeMillis();
			update();
		}
	};
	private boolean isEnabled = false;

	public void enable() {
		isEnabled  = true;
		LookingGlassFactory.getInstance().addAutomaticDisplayListener( this.automaticDisplayListener );
	}
	public void disable() {
		isEnabled = false;
		LookingGlassFactory.getInstance().removeAutomaticDisplayListener( this.automaticDisplayListener );
	}

	public void addListener(TimeListener timerEventListener, Long frequency, MultipleEventPolicy policy) {
		if(!isEnabled){
			enable();
		}
		registerPolicyMap(timerEventListener, policy);
		registerIsFiringMap(timerEventListener);
		Long a = secondsToMills(frequency);
		freqMap.put(timerEventListener, a);
		mostRecentFire.put(timerEventListener, Double.doubleToLongBits(0));
		timerList.add(timerEventListener);
	}
	
	private void update() {
		for(TimeListener listener: timerList){
			if(timeToFire(listener)){
				trigger(listener, new TimerEvent());
			}
		}
	}

	private void trigger(TimeListener listener, TimerEvent timerEvent) {
		mostRecentFire.put(listener, currentTime);
		fireEvent(listener, timerEvent, listener);
	}
	private boolean timeToFire(TimeListener listener) {
		return currentTime - mostRecentFire.get(listener) > freqMap.get(listener);
	}

	private Long secondsToMills(Long frequency) {
		return 1000*frequency;
	}

	@Override
	protected void fire(final TimeListener listener, TimerEvent event) {
		super.fire(listener, event);
		listener.timeElapsed(event);
	}
}
