package org.lgna.ik.core.enforcer;

import org.lgna.story.implementation.JointedModelImp;

public abstract class IkEnforcer {

	protected final JointedModelImp<?, ?> jointedModelImp;

	public IkEnforcer( JointedModelImp<?, ?> jointedModelImp ) {
		this.jointedModelImp = jointedModelImp;
	}
}
