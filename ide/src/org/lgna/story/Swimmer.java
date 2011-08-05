package org.lgna.story;

public class Swimmer extends Model{
	private final org.lgna.story.implementation.SwimmerImplementation implementation;
	private java.util.Map< org.lgna.story.resources.JointId, Joint > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	@Override
	/*package-private*/ org.lgna.story.implementation.SwimmerImplementation getImplementation() {
		return this.implementation;
	}
	public Swimmer( org.lgna.story.resources.SwimmerResource resource ) {
		this.implementation = resource.createImplementation( this );
	}
	
	public void swimTo( Entity entity ) {
	}

	public Joint getSpineMiddle() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.SwimmerResource.SwimmerJointId.SPINE_MIDDLE, this.implementation, this.map );
	}
	public Joint getTail() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.SwimmerResource.SwimmerJointId.SPINE_UPPER, this.implementation, this.map );
	}
	public Joint getNeck() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.SwimmerResource.SwimmerJointId.NECK, this.implementation, this.map );
	}
	public Joint getHead() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.SwimmerResource.SwimmerJointId.HEAD, this.implementation, this.map );
	}
	public Joint getMouth() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.SwimmerResource.SwimmerJointId.MOUTH, this.implementation, this.map );
	}
	public Joint getLeftEye() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.SwimmerResource.SwimmerJointId.LEFT_EYE, this.implementation, this.map );
	}
	public Joint getRightEye() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.SwimmerResource.SwimmerJointId.RIGHT_EYE, this.implementation, this.map );
	}
	public Joint getRightPectoralFin() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.SwimmerResource.SwimmerJointId.RIGHT_PECTORAL_FIN, this.implementation, this.map );
	}
	public Joint getLeftPectoralFin() {
		return org.lgna.story.Joint.getJoint( org.lgna.story.resources.SwimmerResource.SwimmerJointId.LEFT_PECTORAL_FIN, this.implementation, this.map );
	}
}
	

