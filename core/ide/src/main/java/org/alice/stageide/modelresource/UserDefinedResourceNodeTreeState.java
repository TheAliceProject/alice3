package org.alice.stageide.modelresource;

import java.util.UUID;

public class UserDefinedResourceNodeTreeState  extends ResourceNodeTreeState {
	private static class SingletonHolder {
		private static UserDefinedResourceNodeTreeState instance = new UserDefinedResourceNodeTreeState();
	}

	public static UserDefinedResourceNodeTreeState getInstance() {
		return UserDefinedResourceNodeTreeState.SingletonHolder.instance;
	}

	private UserDefinedResourceNodeTreeState() {
		super( UUID.fromString( "9d87e1b5-18d2-4d40-a730-d645c5d4aeb7" ), TreeUtilities.getTreeBasedOnUserResources());
	}

	@Override
	protected boolean isBreadcrumbButtonIconDesired() {
		return false;
	}
}
