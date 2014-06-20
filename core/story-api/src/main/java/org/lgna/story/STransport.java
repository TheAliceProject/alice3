package org.lgna.story;

public class STransport extends SJointedModel {
	private final org.lgna.story.implementation.TransportImp implementation;

	public STransport( org.lgna.story.resources.TransportResource resource ) {
		this.implementation = resource.createImplementation( this );
	}

	@Override
	org.lgna.story.implementation.TransportImp getImplementation() {
		return implementation;
	}

	public SJoint[] getWheels() {
		return org.lgna.story.SJoint.getJointArray( this, this.getImplementation().getResource().getWheels() );
	}
}
