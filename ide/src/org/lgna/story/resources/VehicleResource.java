package org.lgna.story.resources;

@org.lgna.project.annotations.ResourceTemplate( modelClass=org.lgna.story.Vehicle.class )
public interface VehicleResource extends JointedModelResource {
	public static class VehicleJointId extends JointId {
		public static final VehicleJointId RIGHT_DOOR = new VehicleJointId( null );
		public static final VehicleJointId LEFT_DOOR = new VehicleJointId(null);
		
		private static VehicleJointId[] roots = { RIGHT_DOOR, LEFT_DOOR };
		private VehicleJointId( JointId parent ) {
			super(parent);
		}
		public static VehicleJointId[] getRoots() {
			return roots;
		}
	};
	public org.lgna.story.implementation.VehicleImplementation createImplementation( org.lgna.story.Vehicle abstraction );
}
