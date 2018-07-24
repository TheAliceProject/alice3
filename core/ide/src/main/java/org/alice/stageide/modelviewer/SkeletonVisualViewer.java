package org.alice.stageide.modelviewer;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import org.lgna.story.resourceutilities.*;

import javax.swing.*;
import java.io.File;
import java.util.logging.Logger;

public class SkeletonVisualViewer extends Viewer {

	private SkeletonVisual skeletonVisual = null;

	public SkeletonVisual getSkeletonVisual() {
		return this.skeletonVisual;
	}

	public void setSkeletonVisual(SkeletonVisual skeletonVisual) {
		if (skeletonVisual != this.skeletonVisual) {
			if (this.skeletonVisual != null) {
				this.skeletonVisual.setParent(null);
			}
			this.skeletonVisual = skeletonVisual;
			if (this.skeletonVisual != null) {
				this.skeletonVisual.setParent(this.getScene().getSgComposite());
			}
		}
	}

	private void positionAndOrientCamera(double height, double duration) {
		double xzFactor = 2.0 * .65 * height;
		double pointAtFactor = 0.5;
		double yFactor = 1.5 * 0.65;
		AffineMatrix4x4 prevPOV = this.getCamera().getLocalTransformation();
		this.getCamera().setTransformation(this.getScene().createOffsetStandIn(-0.3 * xzFactor, height * yFactor, -height * xzFactor));
		this.getCamera().setOrientationOnlyToPointAt(this.getScene().createOffsetStandIn(0, height * pointAtFactor, 0));
		Animator animator = this.getAnimator();
		if ((duration > 0.0) && (animator != null)) {
			AffineMatrix4x4 nextPOV = this.getCamera().getLocalTransformation();
			this.getCamera().setLocalTransformation(prevPOV);

			PointOfViewAnimation povAnimation = new PointOfViewAnimation(this.getCamera().getSgComposite(), AsSeenBy.PARENT, null, nextPOV);
			povAnimation.setDuration(duration);

			animator.completeAll(null);
			animator.invokeLater(povAnimation, null);
		}
	}

}
