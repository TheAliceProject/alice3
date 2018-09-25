package org.alice.stageide.modelviewer;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;

public class SkeletonVisualViewer extends Viewer {

	private SkeletonVisual skeletonVisual = null;

	public void setSkeletonVisual(SkeletonVisual skeletonVisual) {
		if (skeletonVisual != this.skeletonVisual) {
			if (this.skeletonVisual != null) {
				this.skeletonVisual.setParent(null);
			}
			this.skeletonVisual = skeletonVisual;
			if (this.skeletonVisual != null) {
				this.skeletonVisual.setParent(this.getScene().getSgComposite());
//				this.dragAdapter.setSelectedImplementation( skeletonVisual );
			}
		}
	}

	public void positionAndOrientCamera() {
		final AxisAlignedBox boundingBox = skeletonVisual.getAxisAlignedMinimumBoundingBox();
		final Point3 center = boundingBox.getCenter();
		double height = boundingBox.getHeight();
		this.getCamera().setTransformation(this.getScene().createOffsetStandIn( -2 * height, height, 0));
		this.getCamera().setOrientationOnlyToPointAt(this.getScene().createOffsetStandIn(0, center.y, 0));
	}
}
