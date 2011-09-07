package org.lgna.story.implementation.sims2;

public class JointImplementation extends org.lgna.story.implementation.JointImplementation {
	private final NebulousJoint sgJoint;
	public JointImplementation( org.lgna.story.implementation.JointedModelImplementation<?,?> jointedModelImplementation, NebulousJoint sgJoint ) {
		super( jointedModelImplementation );
		this.sgJoint = sgJoint;
	}
	@Override
	public org.lgna.story.resources.JointId getJointId() {
		return this.sgJoint.getJointId();
	}
	@Override
	public NebulousJoint getSgComposite() {
		return this.sgJoint;
	}
}
