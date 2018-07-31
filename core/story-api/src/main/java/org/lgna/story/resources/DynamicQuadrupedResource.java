package org.lgna.story.resources;

import org.alice.tweedle.file.ModelManifest;
import org.lgna.story.SQuadruped;
import org.lgna.story.implementation.QuadrupedImp;

public class DynamicQuadrupedResource extends DynamicResource<QuadrupedImp, SQuadruped> implements QuadrupedResource {

	public DynamicQuadrupedResource(String modelName, String resourceName) {
		super(modelName, resourceName);
	}

	public DynamicQuadrupedResource(ModelManifest modelManifest, ModelManifest.ModelVariant modelVariant) {
		super(modelManifest, modelVariant);
	}

	@Override
	public QuadrupedImp createImplementation(SQuadruped abstraction ) {
		return new QuadrupedImp(abstraction, this.getImplementationAndVisualFactory());
	}

	@Override
	public JointId[] getTailArray() {
		return new JointId[0];
	}
}
