package org.lgna.story.implementation;

import org.lgna.story.resources.JointId;

public final class SwimmerImp extends JointedModelImp< org.lgna.story.Swimmer, org.lgna.story.resources.SwimmerResource > {
	public SwimmerImp( org.lgna.story.Swimmer abstraction, JointImplementationAndVisualDataFactory< org.lgna.story.resources.SwimmerResource > factory ) {
		super( abstraction, factory );
	}
	
	@Override
	public JointId[] getRootJointIds() {
		return org.lgna.story.resources.SwimmerResource.JOINT_ID_ROOTS;
	}
}
