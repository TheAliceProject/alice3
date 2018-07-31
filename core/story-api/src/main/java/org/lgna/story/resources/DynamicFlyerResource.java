package org.lgna.story.resources;

import org.alice.tweedle.file.ModelManifest;
import org.lgna.story.JointedModelPose;
import org.lgna.story.SFlyer;
import org.lgna.story.implementation.FlyerImp;

public class DynamicFlyerResource extends DynamicResource<FlyerImp, SFlyer> implements FlyerResource {

	public DynamicFlyerResource(String modelName, String resourceName) {
		super(modelName, resourceName);
	}

	public DynamicFlyerResource(ModelManifest modelManifest, ModelManifest.ModelVariant modelVariant) {
		super(modelManifest, modelVariant);
	}

	@Override
	public FlyerImp createImplementation(SFlyer abstraction ) {
		return new FlyerImp(abstraction, this.getImplementationAndVisualFactory());
	}

	@Override
	public JointId[] getTailArray() {
		return new JointId[0];
	}

	@Override
	public JointId[] getNeckArray() {
		return new JointId[0];
	}

	@Override
	public JointedModelPose getSpreadWingsPose() {
		return null;
	}

	@Override
	public JointedModelPose getFoldWingsPose() {
		return null;
	}
}
