package org.alice.stageide.modelviewer;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import org.alice.interact.DragAdapter;

public class SkeletonVisualViewer extends Viewer {

	private SkeletonVisual skeletonVisual = null;
	private final SingleViewerDragAdapter dragAdapter = new SingleViewerDragAdapter();

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

	@Override
	protected void initialize() {
		super.initialize();
		dragAdapter.setOnscreenRenderTarget( getOnscreenRenderTarget() );
		dragAdapter.addCameraView( DragAdapter.CameraView.MAIN, getCamera().getSgCamera(), null );
		dragAdapter.makeCameraActive( getCamera().getSgCamera() );
	}

	public void positionAndOrientCamera() {
		final AxisAlignedBox boundingBox = skeletonVisual.getAxisAlignedMinimumBoundingBox();
		final Point3 center = boundingBox.getCenter();
		double height = boundingBox.getHeight();
		getCamera().setTransformation(getScene().createOffsetStandIn( -2 * height, height, 0));
		getCamera().setOrientationOnlyToPointAt(getScene().createOffsetStandIn(0, center.y, 0));
	}
}
