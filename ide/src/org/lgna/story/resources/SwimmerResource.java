package org.lgna.story.resources;

@org.lgna.project.annotations.ResourceTemplate( modelClass=org.lgna.story.Swimmer.class )
public interface SwimmerResource extends JointedModelResource {
	public static final JointId NECK = new JointId( null, SwimmerResource.class );
	
	public static final JointId RIGHT_PECTORAL_FIN = new JointId( NECK, SwimmerResource.class);
	public static final JointId LEFT_PECTORAL_FIN = new JointId( NECK, SwimmerResource.class);
	
	public static final JointId HEAD = new JointId( NECK, SwimmerResource.class );
	public static final JointId MOUTH = new JointId( HEAD, SwimmerResource.class);
	public static final JointId LEFT_EYE = new JointId( HEAD, SwimmerResource.class);
	public static final JointId RIGHT_EYE = new JointId( HEAD, SwimmerResource.class);
	
	public static final JointId PELVIS_UPPER_BODY = new JointId( null, SwimmerResource.class);
	public static final JointId SPINE_MIDDLE = new JointId( PELVIS_UPPER_BODY, SwimmerResource.class);
	public static final JointId SPINE_UPPER = new JointId( SPINE_MIDDLE, SwimmerResource.class);
	public static final JointId TOP_TAIL_FIN = new JointId( SPINE_UPPER, SwimmerResource.class);
	public static final JointId BOTTOM_TAIL_FIN = new JointId( SPINE_UPPER, SwimmerResource.class);
	
	public static final JointId[] JOINT_ID_ROOTS = { NECK, PELVIS_UPPER_BODY };
	
	public org.lgna.story.implementation.SwimmerImp createImplementation( org.lgna.story.Swimmer abstraction );
}
