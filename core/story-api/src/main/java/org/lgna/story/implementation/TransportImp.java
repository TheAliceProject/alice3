package org.lgna.story.implementation;

import org.lgna.story.resources.JointId;

public class TransportImp<T extends org.lgna.story.STransport, R extends org.lgna.story.resources.TransportResource> extends JointedModelImp {
	public TransportImp( org.lgna.story.STransport abstraction, JointImplementationAndVisualDataFactory<R> factory ) {
		super( abstraction, factory );
	}

	@Override
	public JointId[] getRootJointIds() {
		return ( (R)this.getResource() ).getRootJointIds();
	}
}
