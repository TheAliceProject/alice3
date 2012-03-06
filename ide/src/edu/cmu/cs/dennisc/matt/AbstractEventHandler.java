﻿package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.lgna.common.ComponentThread;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.Visual;
import org.lgna.story.event.AbstractEvent;

import edu.cmu.cs.dennisc.java.util.Collections;

public abstract class AbstractEventHandler< L, E extends AbstractEvent > {
	
	protected boolean shouldFire = true;
	protected Integer count = 0;
	protected Map<Object, MultipleEventPolicy> policyMap = Collections.newHashMap();
	protected Map<Object, HashMap< Object, Boolean >> isFiringMap = Collections.newHashMap();
	protected EventRecorder recorder = EventRecorder.getSingleton();
	private LinkedList<E> queue = new LinkedList<E>();

	protected void fireEvent(final L listener, final E event, final Object o){
		if(isFiringMap.get(listener) == null){
			isFiringMap.put(listener, new HashMap<Object, Boolean>());
		}
		if(isFiringMap.get(listener).get(o) == null){
			isFiringMap.get(listener).put(o, false);
		} 
		if(shouldFire){
			ComponentThread thread = new org.lgna.common.ComponentThread( new Runnable() {
				public void run() {
					fire(listener, event);
					if(policyMap.get(listener).equals(MultipleEventPolicy.ENQUEUE)){
						fireDequeue( listener );
					}
					isFiringMap.get(listener).put(o, false);
				}
			}, "eventThread" );
			if(isFiringMap.get(listener).get( o ).equals(false)){
				isFiringMap.get(listener).put(o, true);
				thread.start();
				return;
			}else if(policyMap.get(listener).equals(MultipleEventPolicy.COMBINE)){
				thread.start();
			}else if(policyMap.get(listener).equals(MultipleEventPolicy.ENQUEUE)){
				enqueue( event );
			}
		}
	}
	protected void enqueue( E event ) {
		synchronized ( queue ) {
			queue.addLast( event );
		}
	}

	protected void fireDequeue(L listener ) {
		LinkedList<E> internalQueue;
		synchronized ( queue ) {
			if(queue.size() == 0){
				return;
			}
			internalQueue = new LinkedList<E>( queue );
			queue.clear();
		}
		while( internalQueue.size() > 0 ){
			fire( listener, internalQueue.removeFirst() );
		}
		fireDequeue( listener );
	}

	private void fire(L listener, E event){
		recorder.recordEvent(event);
		nameOfFireCall(listener, event);
	}

	protected abstract void nameOfFireCall(L listener, E event);
	
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
	protected void registerPolicyMap(L listener, MultipleEventPolicy policy) {
		policyMap.put(listener, policy);
	}

	protected void fireEvent(L listener, E event) {
		fireEvent( listener, event, listener ); //used if policy is not constrained by anything else, such as selected model for mouse click events
	}

}
