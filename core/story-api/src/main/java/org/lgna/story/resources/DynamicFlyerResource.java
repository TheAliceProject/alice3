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
	public JointId[] getRootJointIds() {
		return FlyerResource.JOINT_ID_ROOTS;
	}

	@Override
	public FlyerImp createImplementation(SFlyer abstraction ) {
		return new FlyerImp(abstraction, this.getImplementationAndVisualFactory());
	}

	@Override
	public JointId[] getTailArray() {
		return DEFAULT_TAIL;
	}

	@Override
	public JointId[] getNeckArray() {
		return DEFAULT_NECK;
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
