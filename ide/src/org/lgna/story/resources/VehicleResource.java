package org.lgna.story.resources;

@org.lgna.project.annotations.ResourceTemplate( modelClass=org.lgna.story.Vehicle.class )
public interface VehicleResource extends JointedModelResource {
	public static enum VehicleJointId implements JointId {
		RIGHT_DOOR( null ),
		LEFT_DOOR(null);
		
		private static VehicleJointId[] roots = { RIGHT_DOOR, LEFT_DOOR };
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
