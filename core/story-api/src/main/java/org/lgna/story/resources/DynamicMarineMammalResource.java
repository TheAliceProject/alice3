package org.lgna.story.resources;

import org.alice.tweedle.file.ModelManifest;
import org.lgna.story.SSwimmer;
import org.lgna.story.implementation.SwimmerImp;

public class DynamicMarineMammalResource extends DynamicResource<SwimmerImp, SSwimmer> implements MarineMammalResource {

  public DynamicMarineMammalResource(String modelName, String resourceName) {
    super(modelName, resourceName);
  }

  public DynamicMarineMammalResource(ModelManifest modelManifest, ModelManifest.ModelVariant modelVariant) {
    super(modelManifest, modelVariant);
  }

  @Override
  public JointId[] getRootJointIds() {
    return SwimmerResource.JOINT_ID_ROOTS;
  }

  @Override
  public SwimmerImp createImplementation(SSwimmer abstraction) {
    return new SwimmerImp(abstraction, this.getImplementationAndVisualFactory());
  }
}
