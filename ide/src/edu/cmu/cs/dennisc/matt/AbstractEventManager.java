package edu.cmu.cs.dennisc.matt;

import org.lgna.story.Model;

public abstract class AbstractEventManager {
	
	protected boolean shouldFire = true;

	public void silenceListeners() {
		shouldFire  = false;
	}

	public void restoreListeners() {
		shouldFire = true;
	}
	
	public abstract void addListener(AbstractListener listener);
	public abstract void fireAllTargeted(Model changedEntity);

}
