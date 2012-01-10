package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;

import org.lgna.story.Model;

public abstract class MouseClickedListener extends AbstractListener {

	private LinkedList<Model> targets;

	public MouseClickedListener(LinkedList<Model> targets, EventPolicy policy){
		super(policy);
		this.targets = targets;
	}

	public MouseClickedListener(Model target, EventPolicy policy){
		super(policy);
		this.targets = new LinkedList<Model>();
		targets.add(target);
	}
	
	public abstract void mouseButtonClicked(Model targets);
		
	@Override
	public void fire(LinkedList<Model> targets){
		mouseButtonClicked(targets.getFirst());
	}

	public LinkedList<Model> getTargets() {
		return targets;
	}
}