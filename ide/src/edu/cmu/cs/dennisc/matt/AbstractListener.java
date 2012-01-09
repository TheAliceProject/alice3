package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;

import org.lgna.story.Model;

public abstract class AbstractListener {

	protected boolean isFiring = false;
	protected boolean shouldFire = true;

	protected void fireEvent(final LinkedList<Model> targets){
		if(shouldFire && !isFiring()){ //need to check policy QUEUE, IGNORE, STACK
			isFiring = true;
			Thread thread = new Thread(){
				public void run(){
					fire(targets);
					isFiring = false;
				}
			};
			thread.start();
		}
	}
	protected abstract void fire(LinkedList<Model> targets);
	
	public boolean isFiring(){
		return isFiring;
	}
	public void init(){
		shouldFire  = true;
	}
	public void cleanUp(){
		shouldFire = false;
	}

}
