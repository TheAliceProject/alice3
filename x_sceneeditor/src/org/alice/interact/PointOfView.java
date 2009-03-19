package org.alice.interact;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;

public class PointOfView {

	private AffineMatrix4x4 transform = new AffineMatrix4x4();
	private ReferenceFrame referenceFrame;
	
	public PointOfView(AffineMatrix4x4 transform, ReferenceFrame referenceFrame)
	{
		this.transform.set(transform);
		this.referenceFrame = referenceFrame;
	}

	public AffineMatrix4x4 getTransform() {
		return transform;
	}

	public void setTransform(AffineMatrix4x4 transform) {
		this.transform.set(transform);
	}

	public ReferenceFrame getReferenceFrame() {
		return referenceFrame;
	}

	public void setReferenceFrame(ReferenceFrame referenceFrame) {
		this.referenceFrame = referenceFrame;
	}
	
	
}
