package org.lgna.story;

import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.Visibility;

public class SVehicle extends SJointedModel {
	private final org.lgna.story.implementation.VehicleImp implementation;

	@Override
	/* package-private */org.lgna.story.implementation.VehicleImp getImplementation() {
		return this.implementation;
	}

	public SVehicle( org.lgna.story.resources.VehicleResource resource ) {
		this.implementation = resource.createImplementation( this );
	}

	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public void driveTo( SThing entity ) {
		javax.swing.JOptionPane.showMessageDialog( null, "todo: driveTo" );
	}

	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public SJoint getRoot() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.VehicleResource.ROOT );
	}

	public SJoint getFrontRightWheel() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.VehicleResource.FRONT_RIGHT_WHEEL );
	}

	public SJoint getFrontLeftWheel() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.VehicleResource.FRONT_LEFT_WHEEL );
	}

	public SJoint getBackWheels() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.VehicleResource.BACK_WHEELS );
	}
}
