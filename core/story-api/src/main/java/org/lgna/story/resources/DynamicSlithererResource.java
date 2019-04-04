package org.lgna.story.resources;

import org.alice.tweedle.file.ModelManifest;
import org.lgna.story.SSlitherer;
import org.lgna.story.implementation.SlithererImp;

public class DynamicSlithererResource extends DynamicResource<SlithererImp, SSlitherer> implements SlithererResource {
  public DynamicSlithererResource(ModelManifest modelManifest, ModelManifest.ModelVariant modelVariant) {
    super(modelManifest, modelVariant);
  }

  @Override
  public JointId[] getRootJointIds() {
    return SlithererResource.JOINT_ID_ROOTS;
  }

  @Override
  public SlithererImp createImplementation(SSlitherer abstraction) {
    return new SlithererImp(abstraction, this.getImplementationAndVisualFactory());
  }

  @Override
  public JointId[] getTailArray() {
    return DEFAULT_TAIL;
  }
}
