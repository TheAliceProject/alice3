package org.lgna.story.resources;

import org.alice.tweedle.file.ModelManifest;
import org.lgna.story.SJointedModel;
import org.lgna.story.SProp;
import org.lgna.story.implementation.BasicJointedModelImp;

public class DynamicPropResource extends DynamicResource<BasicJointedModelImp, SProp> implements PropResource {

  public DynamicPropResource(String modelName, String resourceName) {
    super(modelName, resourceName);
  }

  public DynamicPropResource(ModelManifest modelManifest, ModelManifest.ModelVariant modelVariant) {
    super(modelManifest, modelVariant);
  }

  @Override
  public BasicJointedModelImp createImplementation(SProp abstraction) {
    return new BasicJointedModelImp(abstraction, this.getImplementationAndVisualFactory());
  }

  @Override
  public BasicJointedModelImp createImplementation(SJointedModel abstraction) {
    return new BasicJointedModelImp(abstraction, this.getImplementationAndVisualFactory());
  }
}
