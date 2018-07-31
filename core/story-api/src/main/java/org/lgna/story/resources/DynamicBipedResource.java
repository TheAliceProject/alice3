package org.lgna.story.resources;

import org.alice.tweedle.file.ModelManifest;
import org.lgna.story.SBiped;
import org.lgna.story.implementation.BipedImp;

public class DynamicBipedResource extends DynamicResource<BipedImp, SBiped> implements BipedResource {

	public DynamicBipedResource(String className, String resourceName) {
		super(className, resourceName);
	}

	public DynamicBipedResource(ModelManifest modelManifest, ModelManifest.ModelVariant modelVariant) {
		super(modelManifest, modelVariant);
	}

	@Override
	public BipedImp createImplementation( SBiped abstraction ) {
		return new BipedImp(abstraction, this.getImplementationAndVisualFactory());
	}
}
