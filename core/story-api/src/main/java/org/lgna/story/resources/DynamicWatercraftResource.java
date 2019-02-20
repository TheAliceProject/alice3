package org.lgna.story.resources;

import org.alice.tweedle.file.ModelManifest;
import org.lgna.story.STransport;
import org.lgna.story.implementation.TransportImp;

public class DynamicWatercraftResource  extends DynamicResource<TransportImp, STransport> implements WatercraftResource {

	public DynamicWatercraftResource(String modelName, String resourceName) {
		super(modelName, resourceName);
	}

	public DynamicWatercraftResource(ModelManifest modelManifest, ModelManifest.ModelVariant modelVariant) {
		super(modelManifest, modelVariant);
	}

	@Override
	public TransportImp createImplementation(STransport abstraction) {
		return new TransportImp(abstraction, this.getImplementationAndVisualFactory());
	}
}
