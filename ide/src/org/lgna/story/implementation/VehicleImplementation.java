package org.lgna.story.implementation;

import org.lgna.story.resources.JointId;

public final class VehicleImplementation extends JointedModelImplementation< org.lgna.story.Vehicle, org.lgna.story.resources.VehicleResource > {
	public VehicleImplementation( org.lgna.story.Vehicle abstraction, JointImplementationAndVisualDataFactory< org.lgna.story.resources.VehicleResource > factory ) {
		super( abstraction, factory );
	}
	
	@Override
	public JointId[] getRootJointIds() {
		return org.lgna.story.resources.VehicleResource.JOINT_ID_ROOTS;
	}
}
