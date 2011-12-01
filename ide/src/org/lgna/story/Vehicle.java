package org.lgna.story;

import org.lgna.project.annotations.*;

public class Vehicle extends JointedModel {
	private final org.lgna.story.implementation.VehicleImp implementation;
	@Override
	/*package-private*/ org.lgna.story.implementation.VehicleImp getImplementation() {
		return this.implementation;
	}
	public Vehicle( org.lgna.story.resources.VehicleResource resource ) {
		this.implementation = resource.createImplementation( this );
	}
	
	@MethodTemplate(visibility=Visibility.TUCKED_AWAY)
	public void driveTo( Entity entity ) {
		javax.swing.JOptionPane.showMessageDialog( null, "todo: driveTo" );
	}
	
	public Joint getRightDoor() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.VehicleResource.RIGHT_DOOR );
	}

	public Joint getLeftDoor() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.VehicleResource.LEFT_DOOR );
	}
}
