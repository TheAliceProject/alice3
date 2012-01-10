package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;

import org.lgna.story.Model;

public abstract class AbstractListener {

	protected boolean isFiring = false;
	protected boolean shouldFire = true;
	protected EventPolicy policy;
	protected Integer count = 0;

	public AbstractListener(EventPolicy policy) {
		this.policy = policy;
	}

	protected void fireEvent(final LinkedList<Model> targets){
		if(shouldFire){
			Thread thread = new Thread(){
				@Override
				public void run(){
					isFiring = true;
					fire(targets);
					if(policy.equals(EventPolicy.ENQUEUE)){
						fireDequeue(targets);
					}
					isFiring = false;
				}
			};
			if(!isFiring()){
				thread.start();
			}else if(policy.equals(EventPolicy.COMBINE)){
				thread.start();
			}else if(policy.equals(EventPolicy.ENQUEUE)){
				enqueue();
			}
		}
	}
	protected void enqueue() {
		synchronized (count) {
			++count;
		}
	}

	protected void fireDequeue(LinkedList<Model> targets) {
		int subCount;
		synchronized (count) {
			if(count == 0){
				return;
			}
			subCount = count;
			count = 0;
		}
		while(subCount > 0){
			fire(targets);
			--subCount;
		}
		fireDequeue(targets);
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
