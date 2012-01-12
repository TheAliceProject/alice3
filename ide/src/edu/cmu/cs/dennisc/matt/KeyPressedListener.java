package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;

import org.lgna.story.Key;
import org.lgna.story.Model;
import org.lgna.story.event.KeyEvent;

public abstract class KeyPressedListener {

//	public KeyPressedListener() {
////		super(policy);
//	}
//
//
//
//	protected void fire(KeyEvent e) {
//		keyPressed(e.getKey());
//	}
//
//

	public abstract void keyPressed(Key key);
//
//
//	@Override //fire(model) not applicable to keyPressed
//	protected void fire(LinkedList<Model> targets) {}
//
//
//	protected void fireDequeue(KeyEvent event) {
//		int subCount;
//		synchronized (count) {
//			if(count == 0)
//				return;
//			subCount = count;
//			count = 0;
//		}
//		while(subCount > 0){
//			fire(event);
//			--subCount;
//		}
//		fireDequeue(event);
//	}
//
//	public void fireEvent(final KeyEvent e) {
//		if(shouldFire){
//			Thread thread = new Thread(){
//				@Override
//				public void run(){
//					isFiring = true;
//					fire(e);
//					if(policy.equals(EventPolicy.ENQUEUE)){
//						fireDequeue(e);
//					}
//					isFiring = false;
//				}
//			};
//			if(!isFiring()){
//				thread.start();
//			}else if(policy.equals(EventPolicy.COMBINE)){ //need to check policy QUEUE, IGNORE, STACK
//				thread.start();
//			}else if(policy.equals(EventPolicy.ENQUEUE)){
//				enqueue();
//			}
//		}
//	}
}
