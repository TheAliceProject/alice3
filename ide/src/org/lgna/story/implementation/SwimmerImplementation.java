package org.lgna.story.implementation;

public final class SwimmerImplementation extends JointedModelImplementation< org.lgna.story.Swimmer, org.lgna.story.resources.SwimmerResource > {
	public SwimmerImplementation( org.lgna.story.Swimmer abstraction, JointImplementationAndVisualDataFactory< org.lgna.story.resources.SwimmerResource > factory ) {
		super( abstraction, factory );
	}
	@Override
	public org.lgna.story.resources.JointId[] getRootJointIds() {
		return org.lgna.story.resources.SwimmerResource.JOINT_ID_ROOTS;
	}
}
