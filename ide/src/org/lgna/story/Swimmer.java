package org.lgna.story;

public class Swimmer extends JointedModel {
	private final org.lgna.story.implementation.SwimmerImplementation implementation;
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
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.SPINE_MIDDLE );
	}
	public Joint getTail() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.SPINE_UPPER );
	}
	public Joint getNeck() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.NECK );
	}
	public Joint getHead() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.HEAD );
	}
	public Joint getMouth() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.MOUTH );
	}
	public Joint getLeftEye() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.LEFT_EYE );
	}
	public Joint getRightEye() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.RIGHT_EYE );
	}
	public Joint getRightPectoralFin() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.RIGHT_PECTORAL_FIN );
	}
	public Joint getLeftPectoralFin() {
		return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.LEFT_PECTORAL_FIN );
	}
}
	

