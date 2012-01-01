package edu.cmu.cs.dennisc.matt;

public abstract class AbstractEvent {
	
	private boolean isFiring = false;
	
	protected void fireEvent(){
		isFiring = true;
		fire();
		isFiring = false;
	}
	protected abstract void fire();
	public boolean isFiring(){
		return isFiring;
	}
	public abstract void init();
	public abstract void cleanUp();

}
