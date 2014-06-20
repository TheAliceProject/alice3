package org.lgna.story.implementation;

import org.lgna.story.resources.JointId;

public final class TransportImp extends JointedModelImp<org.lgna.story.STransport, org.lgna.story.resources.TransportResource> {
	public TransportImp( org.lgna.story.STransport abstraction, JointImplementationAndVisualDataFactory<org.lgna.story.resources.TransportResource> factory ) {
		super( abstraction, factory );
	}

	@Override
	public JointId[] getRootJointIds() {
		return this.getResource().getRootJointIds();
	}
}
