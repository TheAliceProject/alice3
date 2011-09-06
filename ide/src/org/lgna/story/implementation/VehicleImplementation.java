package org.lgna.story.implementation;

public abstract class VehicleImplementation extends JointedModelImplementation {
	private final org.lgna.story.Vehicle abstraction;
	public VehicleImplementation( org.lgna.story.Vehicle abstraction, edu.cmu.cs.dennisc.scenegraph.Visual sgVisual ) {
		super( sgVisual, org.lgna.story.resources.VehicleResource.JOINT_ID_ROOTS );
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
}
