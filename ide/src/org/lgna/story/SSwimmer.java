package org.lgna.story;

import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.Visibility;

public class SSwimmer extends SJointedModel {
	private final org.lgna.story.implementation.SwimmerImp implementation;

	@Override
	/* package-private */org.lgna.story.implementation.SwimmerImp getImplementation() {
		return this.implementation;
	}

	public SSwimmer( org.lgna.story.resources.SwimmerResource resource ) {
		this.implementation = resource.createImplementation( this );
	}

	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public void swimTo( SThing entity ) {
		javax.swing.JOptionPane.showMessageDialog( null, "todo: swimTo" );
	}

	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public SJoint getRoot() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.ROOT );
	}

	public SJoint getNeck() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.NECK );
	}

	public SJoint getHead() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.HEAD );
	}

	public SJoint getMouth() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.MOUTH );
	}

	//	public SJoint getLowerLip() {
	//		 return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.LOWER_LIP);
	//	}
	public SJoint getLeftEye() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.LEFT_EYE );
	}

	public SJoint getRightEye() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.RIGHT_EYE );
	}

	public SJoint getLeftEyelid() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.LEFT_EYELID );
	}

	public SJoint getRightEyelid() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.RIGHT_EYELID );
	}

	public SJoint getFrontLeftFin() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.FRONT_LEFT_FIN );
	}

	//	public SJoint getFrontLeftFinTip() {
	//		 return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.FRONT_LEFT_FIN_TIP);
	//	}
	public SJoint getFrontRightFin() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.FRONT_RIGHT_FIN );
	}

	//	public SJoint getFrontRightFinTip() {
	//		 return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.FRONT_RIGHT_FIN_TIP);
	//	}
	public SJoint getSpineBase() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.SPINE_BASE );
	}

	public SJoint getSpineMiddle() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.SPINE_MIDDLE );
	}

	public SJoint getTail() {
		return org.lgna.story.SJoint.getJoint( this, org.lgna.story.resources.SwimmerResource.TAIL );
	}
}
