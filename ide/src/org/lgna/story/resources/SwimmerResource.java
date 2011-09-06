package org.lgna.story.resources;

@org.lgna.project.annotations.ResourceTemplate( modelClass=org.lgna.story.Swimmer.class )
public interface SwimmerResource extends JointedModelResource {
	public static class SwimmerJointId extends JointId {
		public static final SwimmerJointId NECK = new SwimmerJointId( null );
		
		public static final SwimmerJointId RIGHT_PECTORAL_FIN = new SwimmerJointId( NECK);
		public static final SwimmerJointId LEFT_PECTORAL_FIN = new SwimmerJointId( NECK);
		
		public static final SwimmerJointId HEAD = new SwimmerJointId( NECK );
		public static final SwimmerJointId MOUTH = new SwimmerJointId( HEAD);
		public static final SwimmerJointId LEFT_EYE = new SwimmerJointId( HEAD);
		public static final SwimmerJointId RIGHT_EYE = new SwimmerJointId( HEAD);
		
		public static final SwimmerJointId PELVIS_UPPER_BODY = new SwimmerJointId( null);
		public static final SwimmerJointId SPINE_MIDDLE = new SwimmerJointId( PELVIS_UPPER_BODY);
		public static final SwimmerJointId SPINE_UPPER = new SwimmerJointId( SPINE_MIDDLE);
		public static final SwimmerJointId TOP_TAIL_FIN = new SwimmerJointId( SPINE_UPPER);
		public static final SwimmerJointId BOTTOM_TAIL_FIN = new SwimmerJointId( SPINE_UPPER);
		
		private static SwimmerJointId[] roots = { NECK, PELVIS_UPPER_BODY };
		
		protected SwimmerJointId( JointId parent ) {
			super(parent);
		}
		public static SwimmerJointId[] getRoots() {
			return roots;
		}
	};
	public org.lgna.story.implementation.SwimmerImplementation createImplementation( org.lgna.story.Swimmer abstraction );
}
