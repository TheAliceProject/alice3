package org.lgna.story.resources;

import org.alice.tweedle.file.ModelManifest;
import org.lgna.story.SSwimmer;
import org.lgna.story.implementation.SwimmerImp;

public class DynamicFishResource extends DynamicResource<SwimmerImp, SSwimmer> implements FishResource {

	public DynamicFishResource(String modelName, String resourceName) {
		super(modelName, resourceName);
	}

	public DynamicFishResource(ModelManifest modelManifest, ModelManifest.ModelVariant modelVariant) {
		super(modelManifest, modelVariant);
	}

	@Override
	public SwimmerImp createImplementation(SSwimmer abstraction) {
		return new SwimmerImp(abstraction, this.getImplementationAndVisualFactory());
	}
}
