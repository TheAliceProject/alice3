package org.lgna.story.implementation.sims2;

import edu.cmu.cs.dennisc.nebulous.NebulousJoint;
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
	public boolean isFreeInX() {
		//todo
		return true;
	}
	@Override
	public boolean isFreeInY() {
		//todo
		return true;
	}
	@Override
	public boolean isFreeInZ() {
		//todo
		return true;
	}
	
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, org.lgna.story.implementation.ReferenceFrame asSeenBy ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 transform = this.getTransformation(asSeenBy);
		edu.cmu.cs.dennisc.math.AxisAlignedBox jointBBox = this.sgJoint.getAxisAlignedBoundingBox();
		rv.addBoundingBox(jointBBox, transform);
		return rv;
	}
}
