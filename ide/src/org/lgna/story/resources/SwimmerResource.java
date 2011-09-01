package org.lgna.story.resources;

@org.lgna.project.annotations.ResourceTemplate( modelClass=org.lgna.story.Swimmer.class )
public interface SwimmerResource extends JointedModelResource {
	public static enum SwimmerJointId implements JointId {
		NECK( null ),
		
		RIGHT_PECTORAL_FIN(NECK),
		LEFT_PECTORAL_FIN(NECK),
		
		HEAD( NECK ),
		MOUTH(HEAD),
		LEFT_EYE(HEAD),
		RIGHT_EYE(HEAD),
		
		PELVIS_UPPER_BODY(null),
		SPINE_MIDDLE(PELVIS_UPPER_BODY),
		SPINE_UPPER(SPINE_MIDDLE),
		TOP_TAIL_FIN(SPINE_UPPER),
		BOTTOM_TAIL_FIN(SPINE_UPPER);
		
		private static SwimmerJointId[] roots = { NECK, PELVIS_UPPER_BODY };
		private SwimmerJointId parent;
		private java.util.List< JointId > children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		private SwimmerJointId( SwimmerJointId parent ) {
			this.parent = parent;
			if( this.parent != null ) {
				this.parent.children.add( this );
			}
		}
		public JointId getParent() {
			return this.parent;
		}
		public Iterable< JointId > getChildren() {
			return this.children;
		}
		public static SwimmerJointId[] getRoots() {
			return roots;
		}
	};
	public org.lgna.story.implementation.SwimmerImplementation createImplementation( org.lgna.story.Swimmer abstraction );
}
