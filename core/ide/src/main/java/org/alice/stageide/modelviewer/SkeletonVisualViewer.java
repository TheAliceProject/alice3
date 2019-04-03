package org.alice.stageide.modelviewer;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.util.BoundingBoxDecorator;
import edu.cmu.cs.dennisc.scenegraph.util.ExtravagantAxes;
import org.alice.interact.DragAdapter;

public class SkeletonVisualViewer extends Viewer {

  private SkeletonVisual skeletonVisual = null;
  private final SingleViewerDragAdapter dragAdapter = new SingleViewerDragAdapter();

  private final ExtravagantAxes fancyAxes;

  private final BoundingBoxDecorator unitBox = new BoundingBoxDecorator();
  // To shrink unit box and hide unitBox
  private final AxisAlignedBox zeroAAB = new AxisAlignedBox(0, 0, 0, 0, 0, 0);
  // To grow and display unitBox
  private final AxisAlignedBox unitAAB = new AxisAlignedBox(0, 0, 0, 1, 1, 1);

  public SkeletonVisualViewer() {
    super();
    unitBox.setParent(getScene().getSgComposite());
    fancyAxes = new ExtravagantAxes(1);
    for (Component cmp : fancyAxes.getComponents()) {
      cmp.setParent(getScene().getSgComposite());
    }
  }

  public void setSkeletonVisual(SkeletonVisual skeletonVisual) {
    if (skeletonVisual != this.skeletonVisual) {
      if (this.skeletonVisual != null) {
        this.skeletonVisual.setParent(null);
      }
      this.skeletonVisual = skeletonVisual;
      if (this.skeletonVisual != null) {
        this.skeletonVisual.setParent(getScene().getSgComposite());
        updateScale();
      }
    }
  }

  public void updateScale() {
    final AxisAlignedBox modelBounds = this.skeletonVisual.getAxisAlignedMinimumBoundingBox();
    // Scale to fit with skeletonVisual
    fancyAxes.resize(modelBounds.getDiagonal(), 1.5, 1);
    //  Position next to skeletonVisual
    unitAAB.setXMinimum(modelBounds.getXMaximum());
    unitAAB.setXMaximum(modelBounds.getXMaximum() + 1);
  }

  @Override
  protected void initialize() {
    super.initialize();
    dragAdapter.setOnscreenRenderTarget(getOnscreenRenderTarget());
    dragAdapter.addCameraView(DragAdapter.CameraView.MAIN, getCamera().getSgCamera(), null);
    dragAdapter.makeCameraActive(getCamera().getSgCamera());
  }

  public void positionAndOrientCamera() {
    final AxisAlignedBox boundingBox = skeletonVisual.getAxisAlignedMinimumBoundingBox();
    final Point3 center = boundingBox.getCenter();
    double diagonal = boundingBox.getDiagonal();
    getCamera().setTransformation(getScene().createOffsetStandIn(-2 * diagonal, diagonal, -diagonal));
    getCamera().setOrientationOnlyToPointAt(getScene().createOffsetStandIn(0, center.y, 0));
  }

  public void setShowUnitBox(Boolean showBox) {
    unitBox.setBox(showBox ? unitAAB : zeroAAB);
  }

  public void setShowAxes(Boolean showAxes) {
    fancyAxes.setOpacity(showAxes ? 0.4f : 0);
  }
}
