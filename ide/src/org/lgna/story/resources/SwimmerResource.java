package org.lgna.story.resources;

@org.lgna.project.annotations.ResourceTemplate( modelClass=org.lgna.story.Swimmer.class )
public interface SwimmerResource extends JointedModelResource {
	public static final org.lgna.story.resources.JointId ROOT = new JointId( null, SwimmerResource.class );
	public static final org.lgna.story.resources.JointId NECK = new org.lgna.story.resources.JointId( ROOT, SwimmerResource.class );
	public static final org.lgna.story.resources.JointId HEAD = new org.lgna.story.resources.JointId( NECK, SwimmerResource.class );
	public static final org.lgna.story.resources.JointId MOUTH = new org.lgna.story.resources.JointId( HEAD, SwimmerResource.class );
//	public static final org.lgna.story.resources.JointId LOWER_LIP = new org.lgna.story.resources.JointId( MOUTH, SwimmerResource.class );
	public static final org.lgna.story.resources.JointId LEFT_EYE = new org.lgna.story.resources.JointId( HEAD, SwimmerResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_EYE = new org.lgna.story.resources.JointId( HEAD, SwimmerResource.class );
	public static final org.lgna.story.resources.JointId LEFT_PECTORAL_FIN = new org.lgna.story.resources.JointId( NECK, SwimmerResource.class );
//	public static final org.lgna.story.resources.JointId LEFT_PECTORAL_FIN_TIP = new org.lgna.story.resources.JointId( LEFT_PECTORAL_FIN, SwimmerResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_PECTORAL_FIN = new org.lgna.story.resources.JointId( NECK, SwimmerResource.class );
//	public static final org.lgna.story.resources.JointId RIGHT_PECTORAL_FIN_TIP = new org.lgna.story.resources.JointId( RIGHT_PECTORAL_FIN, SwimmerResource.class );
	public static final org.lgna.story.resources.JointId SPINE_BASE = new org.lgna.story.resources.JointId( ROOT, SwimmerResource.class );
	public static final org.lgna.story.resources.JointId SPINE_MIDDLE = new org.lgna.story.resources.JointId( SPINE_BASE, SwimmerResource.class );
	public static final org.lgna.story.resources.JointId TAIL = new org.lgna.story.resources.JointId( SPINE_MIDDLE, SwimmerResource.class );
//	public static final org.lgna.story.resources.JointId TOP_TAIL_FIN = new org.lgna.story.resources.JointId( SPINE_UPPER, SwimmerResource.class );
//	public static final org.lgna.story.resources.JointId BOTTOM_TAIL_FIN = new org.lgna.story.resources.JointId( SPINE_UPPER, SwimmerResource.class );

	public static org.lgna.story.resources.JointId[] JOINT_ID_ROOTS = { ROOT };
	
	public org.lgna.story.implementation.SwimmerImp createImplementation( org.lgna.story.Swimmer abstraction );
}
