package org.lgna.story.resources;

import org.alice.tweedle.file.ModelManifest;
import org.lgna.story.STransport;
import org.lgna.story.implementation.TransportImp;

public class DynamicTrainResource extends DynamicResource<TransportImp, STransport> implements TrainResource {

  public DynamicTrainResource(String modelName, String resourceName) {
    super(modelName, resourceName);
  }

  public DynamicTrainResource(ModelManifest modelManifest, ModelManifest.ModelVariant modelVariant) {
    super(modelManifest, modelVariant);
  }

  @Override
  public JointId[] getRootJointIds() {
    return TrainResource.JOINT_ID_ROOTS;
  }

  @Override
  public TransportImp createImplementation(STransport abstraction) {
    return new TransportImp(abstraction, this.getImplementationAndVisualFactory());
  }
}
