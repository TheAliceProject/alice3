package org.lgna.story.resources;

import org.alice.tweedle.file.ModelManifest;
import org.lgna.story.STransport;
import org.lgna.story.implementation.TransportImp;

public class DynamicAutomobileResource extends DynamicResource<TransportImp, STransport> implements AutomobileResource {

  public DynamicAutomobileResource(String modelName, String resourceName) {
    super(modelName, resourceName);
  }

  public DynamicAutomobileResource(ModelManifest modelManifest, ModelManifest.ModelVariant modelVariant) {
    super(modelManifest, modelVariant);
  }

  @Override
  public JointId[] getRootJointIds() {
    return AutomobileResource.JOINT_ID_ROOTS;
  }

  @Override
  public TransportImp createImplementation(STransport abstraction) {
    return new TransportImp(abstraction, this.getImplementationAndVisualFactory());
  }

}
