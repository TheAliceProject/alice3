package org.lgna.story.resources;

@org.lgna.project.annotations.ResourceTemplate( modelClass = org.lgna.story.STransport.class )
public interface TransportResource extends JointedModelResource {

	public org.lgna.story.resources.JointId[] getRootJointIds();

	public org.lgna.story.implementation.TransportImp createImplementation( org.lgna.story.STransport abstraction );

}
