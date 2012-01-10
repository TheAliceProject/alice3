package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;

import org.lgna.story.event.KeyEvent;

public class KeyPressedManager {

	protected boolean shouldFire = true;
	private LinkedList<KeyPressedListener> list = new LinkedList<KeyPressedListener>();

	public void silenceListeners(){
		shouldFire = false;
	}
	public void restoreListeners(){
		shouldFire = true;
	}

	public void addListener(AbstractListener listener) {
		if(listener instanceof KeyPressedListener){
			KeyPressedListener keyList = (KeyPressedListener) listener;
			if(!list.contains(listener)){
				list.add(keyList);
			}
		}
	}

	public void fireAllTargeted(KeyEvent e) {
		if(shouldFire){
			for(KeyPressedListener listener: list){
				listener.fireEvent(e);
			}
		}
	}
}
