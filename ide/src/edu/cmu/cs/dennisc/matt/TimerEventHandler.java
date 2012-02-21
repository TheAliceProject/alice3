package edu.cmu.cs.dennisc.matt;

import java.util.List;
import java.util.Map;

import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.SceneActivationEvent;
import org.lgna.story.event.SceneActivationListener;
import org.lgna.story.event.TimeListener;
import org.lgna.story.event.TimerEvent;
import org.lgna.story.event.WhileCollisionListener;
import org.lgna.story.event.WhileContingencyListener;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent;
import edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener;
import edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory;

public class TimerEventHandler extends AbstractEventHandler<TimeListener, TimerEvent> implements SceneActivationListener {

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
	private boolean isActivated = false;
	private Map<TimeListener, Boolean> activationMap = Collections.newHashMap();

	public void enable() {
		isEnabled  = true;
		LookingGlassFactory.getInstance().addAutomaticDisplayListener( this.automaticDisplayListener );
	}
	public void disable() {
		isEnabled = false;
		LookingGlassFactory.getInstance().removeAutomaticDisplayListener( this.automaticDisplayListener );
	}

	public void addListener(TimeListener timerEventListener, Long frequency, MultipleEventPolicy policy) {
		activationMap.put(timerEventListener, true);
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

	private void trigger( TimeListener listener, TimerEvent timerEvent ) {
		mostRecentFire.put( listener, currentTime );
		if(isActivated) {
			fireEvent( listener, timerEvent );
		}
	}
	private boolean timeToFire(TimeListener listener) {
		return (currentTime - mostRecentFire.get(listener) > freqMap.get(listener) && activationMap.get(listener) );
	}

	private Long secondsToMills(Long frequency) {
		return 1000*frequency;
	}
	@Override
	protected void nameOfFireCall(TimeListener listener, TimerEvent event) {
		listener.timeElapsed(event);
	}
	public void sceneActivated(SceneActivationEvent e) {
		this.isActivated  = true;
	}
	
	public void deactivate( WhileContingencyListener listener) {
		activationMap.put(listener, false);
	}

	public void activate( WhileContingencyListener listener) {
		activationMap.put(listener, true);
	}
}
