package org.lgna.ik.core.enforcer;

public abstract class IkEnforcer {

	protected final org.lgna.story.implementation.JointedModelImp<?, ?> jointedModelImp;

	public IkEnforcer( org.lgna.story.implementation.JointedModelImp<?, ?> jointedModelImp ) {
		this.jointedModelImp = jointedModelImp;
	}
}
