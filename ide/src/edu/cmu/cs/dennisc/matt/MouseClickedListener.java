package edu.cmu.cs.dennisc.matt;

import org.lgna.story.Model;

public abstract class MouseClickedListener {
	
	public abstract void mouseButtonClicked(Model targets);

//	private LinkedList<Model> targets;
//	
//	public abstract void mouseButtonClicked(Model targets);
//		
//	@Override
//	public void fire(LinkedList<Model> targets){
//		mouseButtonClicked(targets.getFirst());
//	}
//
//	public LinkedList<Model> getTargets() {
//		return targets;
//	}
}