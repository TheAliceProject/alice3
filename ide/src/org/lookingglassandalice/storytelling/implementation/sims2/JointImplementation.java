package org.lookingglassandalice.storytelling.implementation.sims2;

public class JointImplementation extends org.lookingglassandalice.storytelling.implementation.JointImplementation {
	private final SgJoint sgJoint;
	public JointImplementation( SgJoint sgJoint ) {
		this.sgJoint = sgJoint;
	}
	@Override
	public org.lookingglassandalice.storytelling.resources.JointId getJointId() {
		return this.sgJoint.getJointId();
	}
	@Override
	public edu.cmu.cs.dennisc.scenegraph.Composite getSgComposite() {
		return this.sgJoint;
	}
}
