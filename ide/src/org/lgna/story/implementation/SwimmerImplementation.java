package org.lgna.story.implementation;

public abstract class SwimmerImplementation extends JointedModelImplementation {
	private final org.lgna.story.Swimmer abstraction;
	public SwimmerImplementation( org.lgna.story.Swimmer abstraction, edu.cmu.cs.dennisc.scenegraph.Visual sgVisual ) {
		super( sgVisual, org.lgna.story.resources.SwimmerResource.JOINT_ID_ROOTS );
		this.abstraction = abstraction;
	}
	@Override
	public org.lgna.story.Swimmer getAbstraction() {
		return this.abstraction;
	}
	@Override
	protected double getBoundingSphereRadius() {
		return 1.0;
	}
}
