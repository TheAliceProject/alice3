package org.alice.stageide.modelresource;

import java.util.List;
import java.util.UUID;

public class UserDefinedResourceNode extends ResourceNode {
	public UserDefinedResourceNode(ResourceKey resourceKey, List<ResourceNode> children ) {
		super( UUID.fromString( "09657d49-d005-4330-97bd-85bff1b926eb" ), resourceKey, children );
	}

	@Override
	protected ResourceNodeTreeState getState() {
		return GroupBasedResourceNodeTreeState.getInstance();
	}
}
