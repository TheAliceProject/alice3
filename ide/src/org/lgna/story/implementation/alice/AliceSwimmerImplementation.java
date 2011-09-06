package org.lgna.story.implementation.alice;

public class AliceSwimmerImplementation extends org.lgna.story.implementation.SwimmerImplementation {
	public AliceSwimmerImplementation( org.lgna.story.Swimmer abstraction, edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgSkeletonVisual, edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] texturedAppearances ) {
		super( abstraction, sgSkeletonVisual );
		sgSkeletonVisual.textures.setValue(texturedAppearances);
	}
}
