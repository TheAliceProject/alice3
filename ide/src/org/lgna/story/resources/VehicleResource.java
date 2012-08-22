package org.lgna.story.resources;

@org.lgna.project.annotations.ResourceTemplate( modelClass = org.lgna.story.SVehicle.class )
public interface VehicleResource extends JointedModelResource {

	public static final JointId RIGHT_DOOR = new JointId( null, VehicleResource.class );
	public static final JointId LEFT_DOOR = new JointId( null, VehicleResource.class );

	public static final JointId[] JOINT_ID_ROOTS = { RIGHT_DOOR, LEFT_DOOR };

	//	public org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory< org.lgna.story.resources.VehicleResource > getImplementationAndVisualFactory();
	public org.lgna.story.implementation.VehicleImp createImplementation( org.lgna.story.SVehicle abstraction );
}
