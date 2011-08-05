package org.lgna.story.implementation.alice;

public class AliceSwimmerImplementation extends org.lgna.story.implementation.SwimmerImplementation {
	public AliceSwimmerImplementation( org.lgna.story.Swimmer abstraction, edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgSkeletonVisual, edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] texturedAppearances ) {
		super( abstraction, sgSkeletonVisual );
		sgSkeletonVisual.textures.setValue(texturedAppearances);
	}
	public edu.cmu.cs.dennisc.scenegraph.SkeletonVisual getSgSkeletonVisual() {
		return (edu.cmu.cs.dennisc.scenegraph.SkeletonVisual)this.getSgVisuals()[ 0 ];
	}
	@Override
	protected JointImplementation createJointImplementation( org.lgna.story.resources.JointId jointId ) {
		String key = jointId.toString();
		edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRoot = this.getSgSkeletonVisual().skeleton.getValue();
		edu.cmu.cs.dennisc.scenegraph.Joint sgJoint = sgSkeletonRoot.getJoint( key );
		return new JointImplementation( this, jointId, sgJoint );
	}
}
