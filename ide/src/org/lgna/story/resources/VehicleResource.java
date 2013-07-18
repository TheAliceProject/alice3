package org.lgna.story.resources;

@org.lgna.project.annotations.ResourceTemplate( modelClass = org.lgna.story.SVehicle.class )
public interface VehicleResource extends JointedModelResource {

	public static final JointId ROOT = new JointId( null, VehicleResource.class );
	//	public static final JointId FRONT_RIGHT_DOOR = new JointId( ROOT, VehicleResource.class );
	//	public static final JointId FRONT_LEFT_DOOR = new JointId( ROOT, VehicleResource.class );
	public static final JointId BACK_WHEELS = new JointId( ROOT, VehicleResource.class );
	public static final JointId FRONT_RIGHT_WHEEL = new JointId( ROOT, VehicleResource.class );
	public static final JointId FRONT_LEFT_WHEEL = new JointId( ROOT, VehicleResource.class );

	public static final JointId[] JOINT_ID_ROOTS = { ROOT };

	//	public org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory< org.lgna.story.resources.VehicleResource > getImplementationAndVisualFactory();
	public org.lgna.story.implementation.VehicleImp createImplementation( org.lgna.story.SVehicle abstraction );
}
