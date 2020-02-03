package org.alice.stageide.gallerybrowser;

import org.alice.stageide.modelviewer.SkeletonVisualViewer;
import org.lgna.croquet.SimpleComposite;

import java.util.UUID;

public class SkeletonPreviewComposite extends SimpleComposite<SkeletonVisualViewer> {
  SkeletonPreviewComposite() {
    super(UUID.fromString("8ada1ef4-276b-42cb-970d-0e51cbc1e5a3"));
  }

  @Override
  protected SkeletonVisualViewer createView() {
    return new SkeletonVisualViewer();
  }
}
