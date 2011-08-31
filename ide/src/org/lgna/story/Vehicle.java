package org.lgna.story;

public class Vehicle extends Model {
	private final org.lgna.story.implementation.VehicleImplementation implementation;
	private java.util.Map< org.lgna.story.resources.JointId, Joint > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
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
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.VehicleResource.VehicleJointId.RIGHT_DOOR, this.implementation, this.map );
	}

	public Joint getLeftDoor() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.VehicleResource.VehicleJointId.LEFT_DOOR, this.implementation, this.map );
	}
}
