package org.lgna.story.implementation.alice;

public class AliceJointedModelImplementation extends org.lgna.story.implementation.JointedModelImplementation {
	private final org.lgna.story.Model abstraction;

	public AliceJointedModelImplementation( org.lgna.story.Model abstraction, edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgSkeletonVisual, edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] texturedAppearances, org.lgna.story.resources.JointId[] rootJointIds ) {
		super( sgSkeletonVisual, rootJointIds );
		this.abstraction = abstraction;
		sgSkeletonVisual.textures.setValue(texturedAppearances);
	}
	
	@Override
	public org.lgna.story.Model getAbstraction() {
		return this.abstraction;
	}
	
}
