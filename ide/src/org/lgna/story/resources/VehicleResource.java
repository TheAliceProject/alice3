package org.lgna.story.resources;

@org.lgna.project.annotations.ResourceTemplate( modelClass=org.lgna.story.Vehicle.class )
public interface VehicleResource extends JointedModelResource {
	
	public static final JointId RIGHT_DOOR = new JointId( null, VehicleResource.class );
	public static final JointId LEFT_DOOR = new JointId(null, VehicleResource.class);
	
	public static final JointId[] JOINT_ID_ROOTS = { RIGHT_DOOR, LEFT_DOOR };

	
	public org.lgna.story.implementation.VehicleImplementation createImplementation( org.lgna.story.Vehicle abstraction );
}
