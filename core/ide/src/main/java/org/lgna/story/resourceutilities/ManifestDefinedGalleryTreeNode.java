package org.lgna.story.resourceutilities;

import org.alice.stageide.modelresource.DynamicResourceKey;
import org.alice.stageide.modelresource.ResourceKey;
import org.alice.tweedle.file.ModelManifest;
import org.lgna.story.resources.DynamicResource;

public class ManifestDefinedGalleryTreeNode extends GalleryResourceTreeNode {

  private final ModelManifest modelManifest;

  ManifestDefinedGalleryTreeNode(ModelManifest modelManifest) {
    super(modelManifest.description.name);
    this.modelManifest = modelManifest;
  }

  @Override
  ResourceKey createResourceKey() {
    return new DynamicResourceKey(DynamicResource.createDynamicResource(modelManifest, modelManifest.models.get(0)));
  }

  @Override
  public String toString() {
    return "User Model (" + name + ")";
  }
}
