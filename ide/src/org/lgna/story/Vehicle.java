package org.lgna.story;

public class Vehicle extends JointedModel {
	private final org.lgna.story.implementation.VehicleImp implementation;
	@Override
	/*package-private*/ org.lgna.story.implementation.VehicleImp getImplementation() {
		return this.implementation;
	}
	public Vehicle( org.lgna.story.resources.VehicleResource resource ) {
		this.implementation = resource.createImplementation( this );
	}
	
	public void driveTo( Entity entity ) {
	}
	
	public Joint getRightDoor() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.VehicleResource.RIGHT_DOOR );
	}

	public Joint getLeftDoor() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.VehicleResource.LEFT_DOOR );
	}
}
