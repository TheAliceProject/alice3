package org.lgna.story.implementation.sims2;

import edu.cmu.cs.dennisc.nebulous.NebulousJoint;

public class JointImplementation extends org.lgna.story.implementation.JointImp {
	private NebulousJoint sgJoint;
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
	public edu.cmu.cs.dennisc.math.UnitQuaternion getOriginalOrientation() {
		return this.sgJoint.getOriginalLocalTransformation().orientation.createUnitQuaternion();
	}
	
	@Override
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getOriginalTransformation() {
		return this.sgJoint.getOriginalLocalTransformation();
	}
	
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans ) {
		edu.cmu.cs.dennisc.math.AxisAlignedBox jointBBox = this.sgJoint.getAxisAlignedBoundingBox();
		rv.addBoundingBox(jointBBox, trans);
		return rv;
	}
	
	@Override
	public void replaceWithJoint(org.lgna.story.implementation.JointImp newJoint, edu.cmu.cs.dennisc.math.AffineMatrix4x4 originalTransform) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 currentTransform = this.getLocalTransformation();
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 dif = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createMultiplication(edu.cmu.cs.dennisc.math.AffineMatrix4x4.createInverse(originalTransform), currentTransform);
		if (!dif.isWithinReasonableEpsilonOfIdentity()) {
			newJoint.setLocalTransformation(edu.cmu.cs.dennisc.math.AffineMatrix4x4.createMultiplication(newJoint.getLocalTransformation(), dif));
		}
		NebulousJoint originalSgJoint = this.sgJoint;
		this.sgJoint = (NebulousJoint)newJoint.getSgComposite();
		for (edu.cmu.cs.dennisc.scenegraph.Component child : originalSgJoint.getComponents()) {
			if (!(child instanceof NebulousJoint)) {
				child.setParent(this.sgJoint);
			}
		}
	}
}
