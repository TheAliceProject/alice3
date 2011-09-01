package org.lgna.story;

public class Vehicle extends JointedModel {
	private final org.lgna.story.implementation.VehicleImplementation implementation;
	@Override
	/*package-private*/ org.lgna.story.implementation.VehicleImplementation getImplementation() {
		return this.implementation;
	}
	public Vehicle( org.lgna.story.resources.VehicleResource resource ) {
		this.implementation = resource.createImplementation( this );
	}
	
	public void driveTo( Entity entity ) {
	}
	
	public Joint getRightDoor() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.VehicleResource.VehicleJointId.RIGHT_DOOR );
	}

	public Joint getLeftDoor() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.VehicleResource.VehicleJointId.LEFT_DOOR );
	}
}
