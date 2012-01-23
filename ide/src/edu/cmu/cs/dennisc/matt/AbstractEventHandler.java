package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.Map;

import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.Visual;
import org.lgna.story.event.AbstractEvent;

import edu.cmu.cs.dennisc.java.util.Collections;

public abstract class AbstractEventHandler< L, E extends AbstractEvent > {
	
	protected boolean shouldFire = true;
	protected Integer count = 0;
	protected Map<Object, MultipleEventPolicy> policyMap = Collections.newHashMap();
	protected Map<Object, HashMap< Object, Boolean >> isFiringMap = Collections.newHashMap();

	protected void fireEvent(final L listener, final E event, final Object o){
		if(isFiringMap.get(listener) == null){
			isFiringMap.put(listener, new HashMap<Object, Boolean>());
		}
		if(isFiringMap.get(listener).get(o) == null){
			isFiringMap.get(listener).put(o, false);
		}
		if(shouldFire){
			Thread thread = new Thread(){
				@Override
				public void run(){
					fire(listener, event);
					if(policyMap.get(listener).equals(MultipleEventPolicy.ENQUEUE)){
						fireDequeue(listener, event);
					}
					isFiringMap.get(listener).put(o, false);
				}
			};
			if(isFiringMap.get(listener).get( o ).equals(false)){
				isFiringMap.get(listener).put(o, true);
				thread.start();
				return;
			}else if(policyMap.get(listener).equals(MultipleEventPolicy.COMBINE)){
				thread.start();
			}else if(policyMap.get(listener).equals(MultipleEventPolicy.ENQUEUE)){
				enqueue();
			}
		}
	}
	protected void enqueue() {
		synchronized (count) {
			++count;
		}
	}

	protected void fireDequeue(L listener, E event) {
		int subCount;
		synchronized (count) {
			if(count == 0){
				return;
			}
			subCount = count;
			count = 0;
		}
		while(subCount > 0){
			fire(listener, event);
			--subCount;
		}
		fireDequeue(listener, event);
	}

	protected abstract void fire(L listener, E event);

	public final void silenceListeners(){
		shouldFire = false;
	}
	public final void restoreListeners(){
		shouldFire = true;
	}

	protected void registerIsFiringMap(Object eventListener) {
		isFiringMap.put(eventListener, new HashMap<Object, Boolean>());
		isFiringMap.get(eventListener).put(eventListener, false);
	}
	protected void registerIsFiringMap(Object eventListener, Visual[] targets) {
		isFiringMap.put(eventListener, new HashMap<Object, Boolean>());
		if(targets != null && targets.length > 0){
			for(Visual target: targets){
				isFiringMap.get(eventListener).put(target, false);
			}
		}
	}
	protected void registerPolicyMap(Object timerEventListener, MultipleEventPolicy policy) {
		policyMap.put(timerEventListener, policy);
	}

}
