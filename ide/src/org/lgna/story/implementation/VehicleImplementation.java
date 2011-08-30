package org.lgna.story.implementation;

import org.lgna.story.Entity;
import org.lgna.story.resources.JointId;

public abstract class VehicleImplementation extends JointedModelImplementation {
	
	private final org.lgna.story.Vehicle abstraction;
	public VehicleImplementation( org.lgna.story.Vehicle abstraction, edu.cmu.cs.dennisc.scenegraph.Visual sgVisual) {
		super( sgVisual );
		this.abstraction = abstraction;
	}
	@Override
	public org.lgna.story.Vehicle getAbstraction() {
		return this.abstraction;
	}
	@Override
	protected double getBoundingSphereRadius() {
		return 1.0;
	}
	@Override
	protected org.lgna.story.resources.JointId[] getRootJointIds() {
		return org.lgna.story.resources.VehicleResource.VehicleJointId.getRoots();
	}

}
