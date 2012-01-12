package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;

import org.lgna.story.Key;
import org.lgna.story.Model;
import org.lgna.story.event.KeyEvent;

public class KeyPressedHandler extends AbstractEventHandler {

	protected boolean shouldFire = true;
	private LinkedList<KeyPressedListener> list = new LinkedList<KeyPressedListener>();

	public void silenceListeners(){
		shouldFire = false;
	}
	public void restoreListeners(){
		shouldFire = true;
	}

	public void addKeyPressedListener(KeyPressedListener keyList, EventPolicy policy) {
		isFiringMap.put(keyList, false);
		policyMap.put(keyList, policy);
		if(!list.contains(keyList)){
			list.add(keyList);
		}
	}

	public void fireAllTargeted(KeyEvent e) {
		if(shouldFire){
			for(KeyPressedListener listener: list){
				fireEvent(listener, e.getKey());
			}
		}
	}

	protected void fireEvent(final KeyPressedListener event, final Key key){
		if(shouldFire){
			Thread thread = new Thread(){
				@Override
				public void run(){
					isFiringMap.put(event, true);
					fire(event, key);
					if(policyMap.get(event).equals(EventPolicy.ENQUEUE)){
						fireDequeue(event, key);
					}
					isFiringMap.put(event, false);
				}
			};
			if(isFiringMap.get(event).equals(false)){
				thread.start();
				System.out.println(isFiringMap.get(event));
			}else if(policyMap.get(event).equals(EventPolicy.COMBINE)){
				thread.start();
			}else if(policyMap.get(event).equals(EventPolicy.ENQUEUE)){
				enqueue();
			}
		}
	}

	protected void fireDequeue(KeyPressedListener event, Key key) {
		int subCount;
		synchronized (count) {
			if(count == 0){
				return;
			}
			subCount = count;
			count = 0;
		}
		while(subCount > 0){
			fire(event, key);
			--subCount;
		}
		fireDequeue(event, key);
	}

	@Override
	protected void fire(Object event, LinkedList<Model> targets) {}

	protected void fire(KeyPressedListener event, Key key){
		event.keyPressed(key);
	}
}
