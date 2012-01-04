package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;

import org.lgna.story.Model;

public abstract class AbstractListener {

	private boolean isFiring = false;
	private boolean shouldFire = true;

	protected void fireEvent(LinkedList<Model> targets){
		if(shouldFire){// && !isFiring){ //only if concurrent unwanted
			isFiring = true;
			fire(targets);
			isFiring = false;
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
