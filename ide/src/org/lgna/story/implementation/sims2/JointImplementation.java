package org.lgna.story.implementation.sims2;

import edu.cmu.cs.dennisc.scenegraph.Composite;

public class JointImplementation extends org.lgna.story.implementation.JointImp {
	private final NebulousJoint sgJoint;
	public JointImplementation( org.lgna.story.implementation.JointedModelImp<?,?> jointedModelImplementation, NebulousJoint sgJoint ) {
		super( jointedModelImplementation );
		this.sgJoint = sgJoint;
		putInstance( this.sgJoint );
	}
	@Override
	public org.lgna.story.resources.JointId getJointId() {
		return this.sgJoint.getJointId();
	}
	@Override
	public NebulousJoint getSgComposite() {
		return this.sgJoint;
	}
	@Override
	public void setCustomJointSgParent(Composite sgParent) {
		sgJoint.setSgParent(sgParent);
	}
}
