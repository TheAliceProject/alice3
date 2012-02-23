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
	
	@Override
	protected edu.cmu.cs.dennisc.math.Vector4 getThoughtBubbleOffset() {
		return this.getTopOffsetForJoint(this.getJointImplementation(org.lgna.story.resources.SwimmerResource.HEAD));
	}
	
	@Override
	protected edu.cmu.cs.dennisc.math.Vector4 getSpeechBubbleOffset() {
		return this.getFrontOffsetForJoint(this.getJointImplementation(org.lgna.story.resources.SwimmerResource.MOUTH));
	}
}
