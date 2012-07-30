package org.lgna.story;

import org.lgna.project.annotations.*;

public class Swimmer extends JointedModel {
	private final org.lgna.story.implementation.SwimmerImp implementation;
	@Override
	/*package-private*/ org.lgna.story.implementation.SwimmerImp getImplementation() {
		return this.implementation;
	}
	public Swimmer( org.lgna.story.resources.SwimmerResource resource ) {
		this.implementation = resource.createImplementation( this );
	}
	
	@MethodTemplate(visibility=Visibility.TUCKED_AWAY)
	public void swimTo( Entity entity ) {
		javax.swing.JOptionPane.showMessageDialog( null, "todo: swimTo" );
	}
	@MethodTemplate(visibility=Visibility.TUCKED_AWAY)
	public Joint getRoot() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.ROOT);
	}
	public Joint getNeck() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.NECK);
	}
	public Joint getHead() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.HEAD);
	}
	public Joint getMouth() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.MOUTH);
	}
//	public Joint getLowerLip() {
//		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.LOWER_LIP);
//	}
	public Joint getLeftEye() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.LEFT_EYE);
	}
	public Joint getRightEye() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.RIGHT_EYE);
	}
	public Joint getFrontLeftFin() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.FRONT_LEFT_FIN);
	}
//	public Joint getFrontLeftFinTip() {
//		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.FRONT_LEFT_FIN_TIP);
//	}
	public Joint getFrontRightFin() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.FRONT_RIGHT_FIN);
	}
//	public Joint getFrontRightFinTip() {
//		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.FRONT_RIGHT_FIN_TIP);
//	}
	public Joint getSpineBase() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.SPINE_BASE);
	}
	public Joint getSpineMiddle() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.SPINE_MIDDLE);
	}
	public Joint getTail() {
		 return org.lgna.story.Joint.getJoint( this, org.lgna.story.resources.SwimmerResource.TAIL);
	}
}
	

