package org.lgna.story;

public class STransport extends SJointedModel {
	private final org.lgna.story.implementation.TransportImp implementation;

	public STransport( org.lgna.story.resources.TransportResource resource ) {
		this.implementation = resource.createImplementation( this );
	}

	@Override
	org.lgna.story.implementation.TransportImp getImplementation() {
		// TODO Auto-generated method stub
		return implementation;
	}
}
