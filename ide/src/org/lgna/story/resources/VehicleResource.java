package org.lgna.story.resources;

public interface VehicleResource extends ModelResource {
	public static enum VehicleJointId implements JointId {
		PELVIS_LOWER_BODY( null ),
		
		LEFT_HIP( PELVIS_LOWER_BODY ),
		LEFT_KNEE( LEFT_HIP ),
		LEFT_ANKLE( LEFT_KNEE ),
		
		RIGHT_HIP( PELVIS_LOWER_BODY ),
		RIGHT_KNEE( RIGHT_HIP ),
		RIGHT_ANKLE( RIGHT_KNEE ),
		
		PELVIS_UPPER_BODY( null ),
		
		SPINE_MIDDLE( PELVIS_UPPER_BODY ),
		SPINE_UPPER( SPINE_MIDDLE ),
		
		NECK( SPINE_UPPER ),
		HEAD( NECK ),
		MOUTH(HEAD),
		LEFT_EYE(HEAD),
		RIGHT_EYE(HEAD),
		
		RIGHT_CLAVICLE( SPINE_UPPER ),
		RIGHT_SHOULDER( RIGHT_CLAVICLE ),
		RIGHT_ELBOW( RIGHT_SHOULDER ),
		RIGHT_WRIST( RIGHT_ELBOW ),
		
		LEFT_CLAVICLE( SPINE_UPPER ),
		LEFT_SHOULDER( LEFT_CLAVICLE ),
		LEFT_ELBOW( LEFT_SHOULDER ),
		LEFT_WRIST( LEFT_ELBOW );
		
		private static VehicleJointId[] roots = { PELVIS_LOWER_BODY, PELVIS_UPPER_BODY };
		private VehicleJointId parent;
		private java.util.List< JointId > children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		private VehicleJointId( VehicleJointId parent ) {
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
		public static VehicleJointId[] getRoots() {
			return roots;
		}
	};
	public org.lgna.story.implementation.VehicleImplementation createImplementation( org.lgna.story.Vehicle abstraction );
}
