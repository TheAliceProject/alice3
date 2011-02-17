package org.lookingglassandalice.storytelling.implementation.sims2;

public class JointImplementation extends org.lookingglassandalice.storytelling.implementation.JointImplementation {
	private final SgJoint sgJoint;
	public JointImplementation( org.lookingglassandalice.storytelling.implementation.JointedModelImplementation jointedModelImplementation, SgJoint sgJoint ) {
		super( jointedModelImplementation );
		this.sgJoint = sgJoint;
	}
	@Override
	public org.lookingglassandalice.storytelling.resources.JointId getJointId() {
		return this.sgJoint.getJointId();
	}
	@Override
	public SgJoint getSgComposite() {
		return this.sgJoint;
	}
}
