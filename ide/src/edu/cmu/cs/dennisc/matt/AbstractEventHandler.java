package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;

import org.lgna.story.Model;

public abstract class AbstractEventHandler {
	
	protected boolean shouldFire = true;
	protected Integer count = 0;
	protected HashMap<Object, Boolean> isFiringMap = new HashMap<Object, Boolean>();
	protected HashMap<Object, EventPolicy> policyMap = new HashMap<Object, EventPolicy>();

	protected void fireEvent(final Object event, final LinkedList<Model> targets){
		if(shouldFire){
			Thread thread = new Thread(){
				@Override
				public void run(){
					fire(event, targets);
					if(policyMap.get(event).equals(EventPolicy.ENQUEUE)){
						fireDequeue(event, targets);
					}
					isFiringMap.put(event, false);
				}
			};
			if(isFiringMap.get(event).equals(false)){
				isFiringMap.put(event, true);
				thread.start();
				return;
			}else if(policyMap.get(event).equals(EventPolicy.COMBINE)){
				thread.start();
			}else if(policyMap.get(event).equals(EventPolicy.ENQUEUE)){
				enqueue();
			}
		}
	}
	protected void enqueue() {
		synchronized (count) {
			++count;
		}
	}

	protected void fireDequeue(Object event, LinkedList<Model> targets) {
		int subCount;
		synchronized (count) {
			if(count == 0){
				return;
			}
			subCount = count;
			count = 0;
		}
		while(subCount > 0){
			fire(event, targets);
			--subCount;
		}
		fireDequeue(event, targets);
	}

	protected abstract void fire(Object event, LinkedList<Model> targets);

	public void silenceListeners(){
		shouldFire = false;
	}
	public void restoreListeners(){
		shouldFire = true;
	}

}
