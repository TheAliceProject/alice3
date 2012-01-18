package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.List;

import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.KeyEvent;
import org.lgna.story.event.KeyListener;

import edu.cmu.cs.dennisc.java.util.Collections;

public class KeyPressedHandler extends AbstractEventHandler<KeyListener, KeyEvent> {

	private final List<KeyListener> list = Collections.newLinkedList();

	public void addListener(KeyListener keyList, MultipleEventPolicy policy) {
//		isFiringMap.put(keyList, false);
		isFiringMap.put(keyList, new HashMap< Object, Boolean >());
		isFiringMap.get( keyList ).put( keyList, false );//TODO: @mmay make key list discriminate based on key pressed
		policyMap.put(keyList, policy);
		if(!list.contains(keyList)){
			list.add(keyList);
		}
	}

	public void fireAllTargeted(KeyEvent e) {
		if(shouldFire){
			for(KeyListener listener: list){
				fireEvent(listener, e, listener);//TODO
			}
		}
	}

	@Override
	protected void fire(KeyListener listener, KeyEvent event) {
		listener.keyPressed(event);
	}
}
